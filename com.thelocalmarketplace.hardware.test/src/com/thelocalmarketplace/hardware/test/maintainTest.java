package com.thelocalmarketplace.hardware.test;
//LEGEND
// 1.Question marks mean I am not a 100% sure if this is testable or should be
// 2. "- I" means it must be tested
//WHAT I NEED TO DO

import static org.junit.Assert.assertTrue;

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
	
	AttendantStation attendentStation;
	//private Maintain maintainOne;
	private AbstractSelfCheckoutStation station;
	
	
	
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
		logicBronze = new SelfCheckoutLogic(bronze);
		logicBronze.session.enable();
		
		//Silver Station
		silver.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		silver = new SelfCheckoutStationSilver();
		silver.plugIn(PowerGrid.instance());
		silver.turnOn();
		logicSilver = new SelfCheckoutLogic(silver);
		logicSilver.session.enable();

		
		//Gold Station
		gold.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		gold = new SelfCheckoutStationGold();
		gold.plugIn(PowerGrid.instance());
		gold.turnOn();
		logicGold = new SelfCheckoutLogic(gold);
		logicGold.session.enable();

		

	}


		
//====================MAINTAIN INK===========================
	
	/*
	 * Ink is Low Gold Station
	 */
	@Test
	public void testGoldLowInk() {
		try {
			//System.out.println(maintainOne.getLowInkMessage()+"BEFORE");
			logicGold.maintain.maintainAddInk(104858);//having 104858 and blow gives you a low
			logicGold.maintain.maintainAddPaper(200);//this is to add a paper to write something on
			logicGold.maintain.print('c');// we write one thing. meaning that, we used 1 ink
			//System.out.println(maintainOne.getInkAdded());
			//System.out.println(maintainOne.getLowInkMessage()+"AFTER");
			assertTrue(logicGold.maintain.getLowInkMessage());	

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*
	 * Ink is Low Gold Station
	 */
	@Test
	public void testSilverLowInk() {
		try {
			//System.out.println(maintainOne.getLowInkMessage()+"BEFORE");
			logicSilver.maintain.maintainAddInk(2);//104858 this value is within the range of the hardware lowInk indication value
			logicSilver.maintain.maintainAddPaper(200);//this is to add a paper to write something on
			logicSilver.maintain.print('c');// we write one thing. meaning that, we used 1 ink
			//System.out.println(maintainOne.getInkAdded());
			//System.out.println(maintainOne.getLowInkMessage()+"AFTER");
			assertTrue(logicSilver.maintain.getLowInkMessage());	

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	/*
	 * Ink is Low Bronze Station
	 */
	@Test
	public void testBronzeLowInk() {
		try {
			//System.out.println(maintainOne.getLowInkMessage()+"BEFORE");
			logicBronze.maintain.maintainAddInk(2);//104858 this value is within the range of the hardware lowInk indication value
			logicBronze.maintain.maintainAddPaper(200);//this is to add a paper to write something on
			logicBronze.maintain.print('c');// we write one thing. meaning that, we used 1 ink
			//System.out.println(maintainOne.getInkAdded());
			//System.out.println(maintainOne.getLowInkMessage()+"AFTER");
			assertTrue(logicBronze.maintain.getLowInkMessage());	

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	/*
	 * Ink is empty Gold
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
