package com.thelocalmarketplace.hardware.test;
//LEGEND
// 1.Question marks mean I am not a 100% sure if this is testable or should be
// 2. "- I" means it must be tested
//WHAT I NEED TO DO

import static org.junit.Assert.assertFalse;
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
import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
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
		
		
		//IDEA 2: to simulate disable
		logicGold = new SelfCheckoutLogic(gold);
		

		

	}


		
//====================MAINTAIN INK===========================
	
	
	/*
	 * Ink is Low in Gold Station and trigger maintenance 
	 * 
	 * 
	 */
	@Test
	public void testGoldLowInkMaintain() throws OverloadedDevice {
		logicGold.maintain.setInitial(104858,200);// what the station starts with
		logicGold.maintain.print('c');// printing 1 char, removes 1 ink
		//now it should be in the state of lowInk. thus, maintenance should trigger. should be true
		System.out.println(logicGold.maintain.getMaintenance());
		assertTrue(logicGold.maintain.getMaintenance());
		

	}
	
	/*
	 * Fix Ink is Low in Gold Station 
	 * 
	 */
	@Test
	public void testFixGoldLowInk() {
		try {
			logicGold.maintain.setInitial(104858,200);// what the station starts with
			System.out.print(logicGold.maintain.getInkAdded());
			logicGold.maintain.print('c');//this simulate that low ink has been reached 
			logicGold.maintain.maintainAddInk(10);//now i should be able to add ink
			System.out.print(logicGold.maintain.getInkAdded());

	
			assertFalse(logicGold.maintain.getLowInkMessage());// getLowInkMessage should flip lowInkMeesage from true to False
			

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	


	/*
	 * Ink is empty in Gold station
	 */
	@Test
	public void testEmptyInkGold() {
		try {
			//how the station is setup
			//setup in this test was done in a way that their was 1 ink in the system. setup by
			logicGold.maintain.setInitial(1,200);
			
			logicGold.maintain.maintainAddPaper(200);
			logicGold.maintain.print('c');// using 1 ink. meaning their will be 0 ink left. aka, empty.
		
			assertTrue(logicGold.maintain.getOutOfInkMessage()); //getOutOfInkMessage() will return true because the notifyInkEmpty

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
		
	/*
	 * Ink Spill occurred 
	 */
    @Test(expected = OverloadedDevice.class)
	public void testInkSpillageGold() throws OverloadedDevice {
		logicGold.maintain.setInitial(1,200);
		logicGold.maintain.maintainAddInk(logicGold.maintain.getMaxInkValue()+1);//making it 1 more than the max should cause overloaded device

		
		
	}
	
	
	
	
	/*
	 * Attendent adds Ink? - change detected
	 * Asserting true if notifyAdd is called
	 * 
	 */
    @Test
	public void testAddedInkGold() {
		try {
		
			logicGold.maintain.maintainAddInk(2);//putting it to 2 means that we called the notified inkAdded
			//Checking if the false value of getInkAddedMessage() change to true 
			assertTrue(logicGold.maintain.getInkAddedMessage()); //getInkAddedMessage() will return true because the notifyInkAdded

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Attendent removes to much Ink? - change detected
	 * have some field value in maintain that is equal to the ink quantity that is add.
	 * then remove the feild value by 1 everytime you print 1 char because that is equal to removal of 1 ink
	 * Or assume gold printer is similar to that of bronze based on doc and then just call the gold ink remaining 
	 * 
	 */
    @Test(expected = InvalidArgumentSimulationException.class)
    public void testRemoveInkGold() throws OverloadedDevice, InvalidArgumentSimulationException{
	
			logicGold.maintain.maintainAddInk(10);// added 10 ink

			logicGold.maintain.maintainAddInk(-1);// removing 1 ink (-1)

		
	}
    
	
	/*
	 * Cant add Ink when printer is enabled or shouldnt?
	 */
 
		
	/*
	 * Opening and closing hardware? - how?
	 */
	
//======================SILVER STATION TEST============================
	

	
//======================BRONZE STATION TEST============================

	

	
	
}
