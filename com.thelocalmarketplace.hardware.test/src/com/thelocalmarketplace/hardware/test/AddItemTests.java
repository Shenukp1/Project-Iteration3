package com.thelocalmarketplace.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import item.HandleBulkyItem;
import item.RemoveItemController;
import powerutility.NoPowerException;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.Products;
import testingUtilities.Wallet;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import control.SelfCheckoutLogic;

public class AddItemTests implements DollarsAndCurrency, CardPayment {
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
		bronze.configureScaleSensitivity(0.0001);
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
	 
		logicBronze.station.printer.addInk(300);
		logicBronze.station.printer.addPaper(300);
		
		//I use custom bank notes from interface
		
		
	}
	
/*
 * add item to scale
 */
@Test
	public void addItemToScaleTest() {
		logicBronze.station.handheldScanner.scan(products.beanBarcodeItem);
		logicBronze.station.scanningArea.addAnItem(products.beanBarcodeItem);
		logicBronze.station.scanningArea.removeAnItem(products.beanBarcodeItem);
		logicBronze.station.baggingArea.addAnItem(products.beanBarcodeItem);
		//logicBronze.weightController.
	}
/*
 * overload scale then remove overloaded item
 */
@Test
public void addHugeItemToScaleAndRemoveTest() {
	logicBronze.station.handheldScanner.scan(products.bigItem);
	logicBronze.station.scanningArea.addAnItem(products.bigItem);
	logicBronze.station.scanningArea.removeAnItem(products.bigItem);
	logicBronze.station.baggingArea.addAnItem(products.bigItem);
	logicBronze.station.baggingArea.removeAnItem(products.bigItem);
	//logicBronze.weightController.
}
/*
 * add bulky item to cart?
 */
@Test
public void addBulkyItemToCart() {
	
	logicBronze.session.BulkyItems = new ArrayList<Product>();
	logicBronze.session.BulkyItems.add(products.beanBarcodedProduct);
	logicBronze.session.Cart.add(products.beanBarcodedProduct);
	RemoveItemController remover= new RemoveItemController(logicBronze.session, bronze);
	remover.removeItem(products.beanBarcodedProduct);
	//logicBronze.session.(,products.beanBarcodedProduct);
	//logicBronze.weightController.
}
/*
 * handle a bulky item
 */
@Test
public void handleBulkyItem() {
	
	HandleBulkyItem handler = new HandleBulkyItem();
	logicBronze.session.BulkyItems = new ArrayList<Product>();
	logicBronze.session.BulkyItems.add(products.bigProduct);
	logicBronze.session.Cart.add(products.bigProduct);
	
	handler.doNotBagItem(logicBronze.session, products.bigProduct);
	//logicBronze.session.(,products.beanBarcodedProduct);
	//logicBronze.weightController.
}


}