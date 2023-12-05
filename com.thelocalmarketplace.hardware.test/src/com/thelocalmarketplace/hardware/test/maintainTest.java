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
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.coin.CoinStorageUnit;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.AttendantStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import attendant.Maintain;
import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;
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
			logicGold.maintain.print('c');//this simulate that low ink has been reached 
			logicGold.maintain.maintainAddInk(10);//now i should be able to add ink

	
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
			logicGold.maintain.maintainAddInk(0);
			
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
		logicGold.maintain.setInitial(1,200);// already has 1 ink
		logicGold.maintain.maintenanceStart();
		logicGold.maintain.maintainAddInk(logicGold.maintain.getMaxInkValue());//adding max value. makes it 1 more than the max should cause overloaded device
		logicGold.maintain.maintenanceStart();
		

		
		
	}
	
	
	
	
	/*
	 * Attendent adds Ink? - change detected
	 * Asserting true if notifyAdd is called
	 * 
	 */
    @Test
	public void testAddedInkGold() {
		try {
			logicGold.maintain.setInitial(1,200);
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
			logicGold.maintain.setInitial(-11,200);

			
		
	}
    
    //===========================PAPER=================================

   
    

	/*
	 * Ink is Low in Gold Station and trigger maintenance 
	 * 
	 * 
	 */
	@Test
	public void testGoldLowPaperMaintain() throws OverloadedDevice {
		logicGold.maintain.setInitial(104860,103);// what the station starts with. 102> is consider low paper
		logicGold.maintain.print('\n');// using 1 paper. meaning their will be 102 paper left. aka, low on paper
		//now it should be in the state of LowPaper. thus, maintenance should trigger. should be true
		System.out.println(logicGold.maintain.getMaintenance());
		assertTrue(logicGold.maintain.getMaintenance());
		

	}
	//Get Low Paper Message is true
	
	
	
	
	/*
	 * Fix Paper is Low in Gold Station 
	 * 
	 * 
	 */
	@Test
	public void testFixGoldLowPaperMaintain() throws OverloadedDevice {
		logicGold.maintain.setInitial(104860,103);// what the station starts with. 102> is consider low paper

		logicGold.maintain.print('\n');// using 1 paper. meaning their will be 102 paper left. aka, low on paper

		//now it should be in the state of lowInk. thus, maintenance should trigger. should be true
		//then allow us to add paper. fixing the issue
		logicGold.maintain.maintainAddPaper(10);
		System.out.println("PaperB: "+ logicGold.maintain.getPaperAdded());

		//assert false because getLowPaperMessage should be false since there is no low paper
		assertFalse(logicGold.maintain.getLowPaperMessage());
		

	}
	
	/*
	 * Paper is empty in Gold station
	 */
	@Test
	public void testEmptyPaperGold() {
		try {
			//how the station is setup
			//setup in this test was done in a way that their was 1 ink in the system. setup by
			logicGold.maintain.setInitial(100000,1);
			System.out.println("Paper: "+ logicGold.maintain.getPaperAdded());

			logicGold.maintain.print('\n');// using 1 paper. meaning their will be 0 paper left. aka, empty.
			System.out.println("Paper: "+ logicGold.maintain.getPaperAdded());

			assertTrue(logicGold.maintain.getOutOfPaperMessage()); //getOutOfInkMessage() will return true because the notifyInkEmpty

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Paper Jam caused by to much paper added in Gold Station
	 */
	@Test(expected = OverloadedDevice.class)
	public void testPaperJamGold() throws InvalidArgumentSimulationException, OverloadedDevice {
		
		logicGold.maintain.setInitial(104860,2);//2 papers are inside
		logicGold.maintain.print('\n');// prints paper, updating the system. putting it maintaince mode since it has only 1 paper
		
		
		
		logicGold.maintain.maintainAddPaper(logicGold.maintain.getMaxPaperValue());//adding max amount of paper = 1+max = overload


	}
	
	
	
    //===========================Coin=================================
	
	

	
//======================SILVER STATION TEST============================
	

	
//======================BRONZE STATION TEST============================

	

	//=====================COIN TEST=======================
	    
    //high coin count
    @Test
	public void testGoldHighCoinMaintain() throws OverloadedDevice, SimulationException, CashOverloadException, DisabledException {
    	
    	// for i in dispenser.getCapacity(), load a coin into dispenser. This will get it to the threshold.
    	int dispenserCapacity = logicGold.maintain.dollarDispenser.getCapacity();
    	for (int i = 0; i < dispenserCapacity; i++) {
    		logicGold.maintain.setCoins(nickle);
    	}
    	
		logicGold.maintain.receiveOneCoin(nickle);;// receiving one coin.
		//now it should be in the state of high coin count. thus, maintenance should trigger. should be true
		
		System.out.println(logicGold.maintain.getMaintenance());
		assertTrue(logicGold.maintain.getMaintenance());
    	
    }
		
    
    //low coin count
    
    //normal coin count
    
    //unloading coins
    
    //loading coins
	
	
}
