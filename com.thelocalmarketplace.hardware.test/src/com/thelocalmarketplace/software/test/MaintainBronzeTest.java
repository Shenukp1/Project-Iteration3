package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.tdc.NoCashAvailableException;
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

public class MaintainBronzeTest implements DollarsAndCurrency, CardPayment{
	
	private SelfCheckoutStationGold gold;
	private SelfCheckoutStationBronze bronze;
	private SelfCheckoutStationSilver silver;
	
	
	private SelfCheckoutLogic logicBronze;
	private SelfCheckoutLogic logicSilver;
	//private SelfCheckoutLogic logicGold;
	
	
	private ReceiptPrinterGold receiptPrinterGold;
	private ReceiptPrinterBronze receiptPrinterBronze;
	private ReceiptPrinterSilver ReceiptPrinterSilver;
	
	private CoinStorageUnit coinStorageUnit;
	private BanknoteStorageUnit banknoteStorageUnit;
	
	
	
	AttendantStation attendentStation;
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
		gold.configureCoinDispenserCapacity(10);
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		gold = new SelfCheckoutStationGold();
		gold.plugIn(PowerGrid.instance());
		gold.turnOn();
		
		
		//IDEA 2: to simulate disable
		logicBronze = new SelfCheckoutLogic(bronze);
		
		
		

	}


		
//====================MAINTAIN INK===========================
	
	
	/*
	 * Ink is Low in Bronze Station and trigger maintenance 
	 * 
	 * 
	 */
	@Test
	public void testBronzeLowInkMaintain() throws OverloadedDevice {
		logicBronze.maintain.setInitial(104858,200);// what the station starts with
		logicBronze.maintain.print('c');// printing 1 char, removes 1 ink
		//now it should be in the state of lowInk. thus, maintenance should trigger. should be true
		System.out.println(logicBronze.maintain.getMaintenance());
		assertTrue(logicBronze.maintain.getMaintenance());
		

	}
	
	/*
	 * Fix Ink is Low in Bronze Station 
	 * 
	 */
	@Test
	public void testFixBronzeLowInk() {
		try {
			logicBronze.maintain.setInitial(104858,200);// what the station starts with
			logicBronze.maintain.print('c');//this simulate that low ink has been reached 
			logicBronze.maintain.maintainAddInk(10);//now i should be able to add ink

	
			assertFalse(logicBronze.maintain.getLowInkMessage());// getLowInkMessage should flip lowInkMeesage from true to False
			

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	


	/*
	 * Ink is empty in Bronze station
	 */
	@Test
	public void testEmptyInkBronze() {
		try {
			//how the station is setup
			//setup in this test was done in a way that their was 1 ink in the system. setup by
			logicBronze.maintain.setInitial(1,200);
			logicBronze.maintain.maintainAddInk(0);
			
			logicBronze.maintain.print('c');// using 1 ink. meaning their will be 0 ink left. aka, empty.
		
			assertTrue(logicBronze.maintain.getOutOfInkMessage()); //getOutOfInkMessage() will return true because the notifyInkEmpty

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
		
	/*
	 * Ink Spill occurred Bronze
	 */
    @Test(expected = OverloadedDevice.class)
	public void testInkSpillageBronze() throws OverloadedDevice {
		logicBronze.maintain.setInitial(1,200);// already has 1 ink
		logicBronze.maintain.maintenanceStart();
		logicBronze.maintain.maintainAddInk(logicBronze.maintain.getMaxInkValue());//adding max value. makes it 1 more than the max should cause overloaded device
		logicBronze.maintain.maintenanceStart();
		

		
		
	}
	
	
	
	
	/*
	 * Attendent adds Ink? - change detected
	 * Asserting true if notifyAdd is called
	 * 
	 */
    @Test
	public void testAddedInkBronze() {
		try {
			logicBronze.maintain.setInitial(1,200);
			logicBronze.maintain.maintainAddInk(2);//putting it to 2 means that we called the notified inkAdded
			//Checking if the false value of getInkAddedMessage() change to true 
			assertTrue(logicBronze.maintain.getInkAddedMessage()); //getInkAddedMessage() will return true because the notifyInkAdded

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
    public void testRemoveInkBronze() throws OverloadedDevice, InvalidArgumentSimulationException{
			logicBronze.maintain.setInitial(-11,200);

			
		
	}
    
    //===========================PAPER=================================

   
    

	/*
	 * Ink is Low in Bronze Station and trigger maintenance 
	 * 
	 * 
	 */
	@Test
	public void testBronzeLowPaperMaintain() throws OverloadedDevice {
		logicBronze.maintain.setInitial(104860,103);// what the station starts with. 102> is consider low paper
		logicBronze.maintain.print('\n');// using 1 paper. meaning their will be 102 paper left. aka, low on paper
		//now it should be in the state of LowPaper. thus, maintenance should trigger. should be true
		System.out.println(logicBronze.maintain.getMaintenance());
		assertTrue(logicBronze.maintain.getMaintenance());
		

	}
	//Get Low Paper Message is true
	
	
	
	
	/*
	 * Fix Paper is Low in Bronze Station 
	 * 
	 * 
	 */
	@Test
	public void testFixBronzeLowPaperMaintain() throws OverloadedDevice {
		logicBronze.maintain.setInitial(104860,103);// what the station starts with. 102> is consider low paper

		logicBronze.maintain.print('\n');// using 1 paper. meaning their will be 102 paper left. aka, low on paper

		//now it should be in the state of lowInk. thus, maintenance should trigger. should be true
		//then allow us to add paper. fixing the issue
		logicBronze.maintain.maintainAddPaper(10);
		System.out.println("PaperB: "+ logicBronze.maintain.getPaperAdded());

		//assert false because getLowPaperMessage should be false since there is no low paper
		assertFalse(logicBronze.maintain.getLowPaperMessage());
		

	}
	
	/*
	 * Paper is empty in Bronze station
	 */
	@Test
	public void testEmptyPaperBronze() {
		try {
			//how the station is setup
			//setup in this test was done in a way that their was 1 ink in the system. setup by
			logicBronze.maintain.setInitial(100000,1);
			System.out.println("Paper: "+ logicBronze.maintain.getPaperAdded());

			logicBronze.maintain.print('\n');// using 1 paper. meaning their will be 0 paper left. aka, empty.
			System.out.println("Paper: "+ logicBronze.maintain.getPaperAdded());

			assertTrue(logicBronze.maintain.getOutOfPaperMessage()); //getOutOfInkMessage() will return true because the notifyInkEmpty

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Paper Jam caused by to much paper added in Bronze Station
	 */
	@Test(expected = OverloadedDevice.class)
	public void testPaperJamBronze() throws InvalidArgumentSimulationException, OverloadedDevice {
		
		logicBronze.maintain.setInitial(104860,2);//2 papers are inside
		logicBronze.maintain.print('\n');// prints paper, updating the system. putting it maintaince mode since it has only 1 paper
		
		
		
		logicBronze.maintain.maintainAddPaper(logicBronze.maintain.getMaxPaperValue());//adding max amount of paper = 1+max = overload


	}
	
	
	
    //===========================Coin=================================
	
	

	
//======================SILVER STATION TEST============================
	

	
//======================BRONZE STATION TEST============================

	

	//=====================COIN TEST=======================

    //low coin count Bronze
    @Test
	public void testBronzeLowCoinMaintain() throws OverloadedDevice, SimulationException, CashOverloadException, DisabledException {
    	
    	// for i in dispenser.getCapacity(), load a coin into dispenser. This will get it to the threshold.
    	//int dispenserCapacity = logicGold.maintain.dollarDispenser.getCapacity();
    	
    	logicBronze.maintain.setCoins(dollars);
    	logicBronze.maintain.setCoins(dollars);
    	
		//now it should be in the state of high coin count. thus, maintenance should trigger. should be true
		
		System.out.println(logicBronze.maintain.getMaintenance());
		assertTrue(logicBronze.maintain.getMaintenance());
		
    	
    }
    
    // attendant loads coin to fix low coin count Bronze
    @Test
	public void testBronzeFixLowCoin() throws OverloadedDevice, SimulationException, CashOverloadException, DisabledException {
    	
    	// for i in dispenser.getCapacity(), load a coin into dispenser. This will get it to the threshold.
    	logicBronze.maintain.setCoins(dollars);
    	logicBronze.maintain.setCoins(dollars);
    	
    	
		logicBronze.maintain.maintainAddCoin(dollars); // receiving one coin.
		
		System.out.println(logicBronze.maintain.getMaintenance());
		assertFalse(logicBronze.maintain.getMaintenance());
		
    	
    }
	
    
    //high coin count Bronze
    @Test
	public void testBronzeHighCoinMaintain() throws OverloadedDevice, SimulationException, CashOverloadException, DisabledException {
    	
    	logicBronze.maintain.setCoins(dollars, dollars, dollars, dollars, dollars, dollars, dollars, dollars);
		
		System.out.println(logicBronze.maintain.getMaintenance());
		assertTrue(logicBronze.maintain.getMaintenance());
    	
    }
    

    // attendant unloads coins to fix high coin count Bronze
    @Test
   	public void testBronzeFixHighCoin() throws OverloadedDevice, SimulationException, CashOverloadException, DisabledException, NoCashAvailableException {
       	
       	// for i in dispenser.getCapacity(), load a coin into dispenser. This will get it to the threshold.
    	logicBronze.maintain.setCoins(dollars, dollars, dollars, dollars, dollars, dollars, dollars, dollars);
    	
    	System.out.println("fix: "+ logicBronze.maintain.getMaintenance());
    	logicBronze.maintain.maintainEmitCoin(dollars);
    	// unloaded all coins from station because it was too high, now adding three coins to be within normal range.
    	logicBronze.maintain.maintainAddCoin(dollars);
    	logicBronze.maintain.maintainAddCoin(dollars);
    	logicBronze.maintain.maintainAddCoin(dollars);
   		
   		System.out.println(logicBronze.maintain.getMaintenance());
   		assertFalse(logicBronze.maintain.getMaintenance());
   		
       	
       }
    
//=========================BANKNOTE TEST=====================================\
    
  // low banknote count Bronze
    @Test
	public void testBronzeLowBanknoteMaintain() throws CashOverloadException {
    	
    	logicBronze.maintain.setBanknotes(ten);
    	logicBronze.maintain.setBanknotes(ten);
    	
		//now it should be in the state of high coin count. thus, maintenance should trigger. should be true
		
		System.out.println(logicBronze.maintain.getMaintenance());
		assertTrue(logicBronze.maintain.getMaintenance());
		
    	
    }
	
}

