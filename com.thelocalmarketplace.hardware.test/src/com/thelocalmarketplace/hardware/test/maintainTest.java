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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.jjjwelectronics.OverloadedDevice;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
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
	
	private Maintain maintain;
	
	
	
	
	
	@Before
	public void testSetup() throws Exception {
		
		bronze.resetConfigurationToDefaults();
		bronze.configureBanknoteDenominations(bankNoteDenominations);
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationBronze bronzeStation = new SelfCheckoutStationBronze();
		bronzeStation.plugIn(PowerGrid.instance());
		bronzeStation.turnOn();
		
		silver.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationSilver silverStation = new SelfCheckoutStationSilver();
		silverStation.plugIn(PowerGrid.instance());
		silverStation.turnOn();
		
		gold.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationGold goldStation = new SelfCheckoutStationGold();
		goldStation.plugIn(PowerGrid.instance());
		goldStation.turnOn();
		
		logicBronze = SelfCheckoutLogic.installOn(bronzeStation);
		logicBronze.session.enable();
		logicBronze.station.getPrinter().addInk(300);
		logicBronze.station.getPrinter().addPaper(100);
		
		
		//Do what is up for silver
		logicSilver = SelfCheckoutLogic.installOn(silverStation);
		
		logicGold = SelfCheckoutLogic.installOn(goldStation);
		logicGold.session.enable();
		logicGold.station.getPrinter().addInk(300);
		System.out.println(logicGold.station.getPrinter().inkRemaining());
		logicGold.station.getPrinter().addPaper(100);
		
		
		
	}


		
//====================MAINTAIN INK===========================
	
	/*
	 * Ink is Low
	 */
	@Test
	public void testLowInk() {
		System.out.println("hello");
		System.out.println(logicGold.station.getPrinter().inkRemaining());

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
