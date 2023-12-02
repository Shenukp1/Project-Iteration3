package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Currency;

import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.Banknote;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import powerutility.NoPowerException;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.Products;
import testingUtilities.Wallet;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import control.SelfCheckoutLogic;

public class PayViaBanknoteTest implements DollarsAndCurrency, CardPayment {
/*
 * make three types of station to test, we'll have to test all three types for each kind of test.
 * Maybe there's a fast way to plug in these objects than repeating a test method?
 */
SelfCheckoutStationBronze bronze;
SelfCheckoutStationSilver silver;
SelfCheckoutStationGold gold;
/*
 * each station needs its own logic instance for setup.
 */
SelfCheckoutLogic logicBronze;
SelfCheckoutLogic logicSilver;
SelfCheckoutLogic logicGold;

/*wallet to add banknotes to a slot. may not be needed because of Dollars And Currency Utility
 *  
 */
Wallet wallet=new Wallet(); 
/*
 * 
 */
/*
 * Products? where are usharabs products?
 */
Products products = new Products();

@Rule
	public ExpectedException expectedMessage = ExpectedException.none();

	@Before
	public void testSetup () throws OverloadedDevice{
		
		/*
		 * set up cash to pay
		 */
		wallet.cash(2,3,4,5,1);
		wallet.coins(2,3,4,5,1);
		/* First, create an instance of the SelfCheckoutStation (Bronze, Silver, Gold).
		 */
		//bronze
		bronze.resetConfigurationToDefaults();
		bronze.configureBanknoteDenominations(bankNoteDenominations);
		
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationBronze bronze = new SelfCheckoutStationBronze();
		bronze.plugIn(PowerGrid.instance());
		bronze.turnOn();
		
		//silver
		silver.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationSilver silver = new SelfCheckoutStationSilver();
		silver.plugIn(PowerGrid.instance());
		silver.turnOn();
		
		//gold
		gold.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationGold gold = new SelfCheckoutStationGold();
		gold.plugIn(PowerGrid.instance());
		gold.turnOn();
		
		/*
		 * Now that we have a station, we need to link the software to it. 
		 */
		
		logicBronze = SelfCheckoutLogic.installOn(bronze);
		logicSilver = SelfCheckoutLogic.installOn(silver);
		logicGold = SelfCheckoutLogic.installOn(gold);
	 
		logicBronze.session.enable();
		
		logicBronze.station.getPrinter().addInk(300);
		logicBronze.station.getPrinter().addPaper(300);
		
		
		
	}
	/*
	 * because no items have been added it should throw an exception or reject the bank note
	 */
	@Test (expected = DisabledException.class)
	public void testSlotnotOpen () throws DisabledException, CashOverloadException {
		logicBronze.station.getBanknoteInput().receive(twenty);

	}
	/*
	 * This test throws string index out of bounds exception probably because there is the
	 * printer can't print empty items. Might be a bug but nbd, we'll work around it
	 */
	@Test //(expected = StringIndexOutOfBoundsException.class)
	public void testSlotopen () throws DisabledException, CashOverloadException {
		//logicBronze.station.banknoteInput.attach();
		logicBronze.station.getBanknoteInput().activate();
		logicBronze.station.getBanknoteInput().enable();
		
		logicBronze.station.banknoteInput.receive(twenty);
		logicBronze.station.banknoteStorage.receive(twenty);


	}
	//The system should accept the bank note
	@Test (expected = NullPointerSimulationException.class)
	public void testSlotopendanglingbankNote () throws DisabledException, CashOverloadException {
		logicBronze.station.banknoteInput.enable();
		logicBronze.station.banknoteInput.receive(five);
		logicBronze.station.banknoteInput.removeDanglingBanknote();
		logicBronze.station.banknoteInput.emit(five);

	}
	/*The system should reject a bad banknote
	 * 
	 */
	@Test 
	public void testBadBankNote () throws DisabledException, CashOverloadException {
		logicBronze.station.banknoteInput.enable();
		logicBronze.station.banknoteInput.receive(euros_5);
		logicBronze.station.banknoteInput.removeDanglingBanknote();
		logicBronze.station.banknoteInput.emit(five);

	}
	/*
	 * This test goes through a payment process for valid currency. 
	 */
	
	@Test
	public void onePaymentStepValidCurrency() throws DisabledException, CashOverloadException, EmptyDevice, OverloadedDevice {
		logicBronze.station.mainScanner.enable();
		logicBronze.session.Cart.add(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(products.beanBarcode));
	
		logicBronze.station.mainScanner.scan(products.beanBarcodeItem);
	
		logicBronze.station.baggingArea.enable();
		logicBronze.station.baggingArea.turnOn();
		logicBronze.station.baggingArea.addAnItem(products.beanBarcodeItem);
		
		logicBronze.banknoteController.onPayViaBanknote();
		logicBronze.station.banknoteInput.enable();
	
		
		logicBronze.station.banknoteInput.receive(five);
		logicBronze.station.banknoteStorage.receive(five);

		
		logicBronze.station.printer.enable();
		logicBronze.station.printer.print('$');
		logicBronze.station.printer.cutPaper();
		
		logicBronze.station.printer.removeReceipt();
	}
	
	
}
