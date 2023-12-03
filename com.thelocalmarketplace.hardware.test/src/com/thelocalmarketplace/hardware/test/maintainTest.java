package com.thelocalmarketplace.hardware.test;
//LEGEND
// 1.Question marks mean I am not a 100% sure if this is testable or should be
// 2. "- I" means it must be tested
//WHAT I NEED TO DO

// 1. Maintain Ink Test
// 	1.1 Ink is low - I
// 	1.2 Ink is empty - I
//	1.3 Detect adding Ink - same as 1.5?
//	1.4 Excess Ink caused spillage? -I
// 	1.5 Attendent adds Ink? - similar to detecting change?
//	1.6 Open hardware? 
//	1.7 Closes hardware?
//	1.8 disabled before adding to printer - I
//


// 2. Maintain Paper Test
//	2.1 Paper is low
//	2.2 Paper is empty
//	2.3 Paper Detect any change
//	2.4 Paper caused Jam or other damages?

// 3. Maintain Coin Test
//	3.1 Coin level is low
//	3.2 Coin Level is High
//	3.3 Paper Detect any change
//	3.4 Paper caused Jam or other damages?

// 4. Maintain Banknote Test

// 5. Attendent doing things - not to sure 




import java.util.Arrays;
import java.util.Collection;
import java.util.prefs.BackingStoreException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.ReceiptPrinterBronze;
import com.jjjwelectronics.printer.ReceiptPrinterGold;
import com.jjjwelectronics.printer.ReceiptPrinterSilver;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.coin.CoinStorageUnit;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.AttendantStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import attendant.Maintain;
import control.SelfCheckoutLogic;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.Products;

public class maintainTest implements DollarsAndCurrency, CardPayment{
	
	private SelfCheckoutStationGold gold;
	private SelfCheckoutStationBronze bronze;
	private SelfCheckoutStationSilver silver;
	
	
	private SelfCheckoutLogic logicBronze;
	private SelfCheckoutLogic logicSilver;
	private SelfCheckoutLogic logicGold;
	
	
	private ReceiptPrinterGold receiptPrinterGold;
	private ReceiptPrinterBronze receiptPrinterBronze;
	private ReceiptPrinterSilver ReceiptPrinterSilver;
	
	private CoinStorageUnit coinStorageUnit;
	private BanknoteStorageUnit banknoteStorageUnit;
	private Maintain maintain;
	
	AttendantStation attendentStation;
	
	
	
	@Before
	public void testSetup() throws Exception {
		//Creating instance of SelfCheckoutStation(Bronze,Silver,Gold)

		//Bronze Station
		bronze.resetConfigurationToDefaults();
		bronze.configureBanknoteDenominations(bankNoteDenominations);
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		bronze = new SelfCheckoutStationBronze();
		bronze.plugIn(PowerGrid.instance());
		bronze.turnOn();
		
		//Silver Station
		silver.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		silver = new SelfCheckoutStationSilver();
		silver.plugIn(PowerGrid.instance());
		silver.turnOn();
		
		//Gold Station
		gold = new SelfCheckoutStationGold();
		gold.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		gold.plugIn(PowerGrid.instance());
		gold.turnOn();
		maintain = new Maintain(gold);
		gold.setSupervisor(attendentStation);
		gold.getPrinter().addInk(300);
		gold.getPrinter().print((char) 1);
		//BankNote initalization 
		gold.configureBanknoteStorageUnitCapacity(10);// Max capacity is 10
		

		
//		banknoteStorageUnit = new BanknoteStorageUnit(10);// Max capacity is 10
//		coinStorageUnit = new CoinStorageUnit(10);// Max capacity is 10 

		
		
		
	}


		
//====================MAINTAIN INK===========================
	
	/*
	 * Ink is Low
	 */
	@Test
	public void testGoldLowInk() {
		
		System.out.println("hello");
		

	}
	
	/*
	 * Ink is empty
	 */
		
	/*
	 * Ink Spill occurred 
	 */
	
	/*
	 * Attendent adds Ink? - change detected
	 * 
	 */

	/*
	 * Attendent removes to much Ink? - change detected
	 * 
	 */
	
	/*
	 * Cant add Ink when printer is enabled or shouldnt?
	 */
		
	/*
	 * Opening and closing hardware? - how?
	 */
	
}
