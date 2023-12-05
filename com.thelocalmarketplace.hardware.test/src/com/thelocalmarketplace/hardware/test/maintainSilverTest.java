package com.thelocalmarketplace.hardware.test;
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

public class maintainSilverTest implements DollarsAndCurrency, CardPayment {

	private SelfCheckoutStationSilver silver;

	private SelfCheckoutLogic logicSilver;

	private ReceiptPrinterSilver ReceiptPrinterSilver;

	private CoinStorageUnit coinStorageUnit;
	private BanknoteStorageUnit banknoteStorageUnit;

	AttendantStation attendentStation;
	// private Maintain maintainOne;
	private AbstractSelfCheckoutStation station;

	@Before
	public void testSetup() throws Exception {

		silver.resetConfigurationToDefaults();
		silver.configureBanknoteDenominations(bankNoteDenominations);
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		silver = new SelfCheckoutStationSilver();
		silver.plugIn(PowerGrid.instance());
		silver.turnOn();
		logicSilver = SelfCheckoutLogic.installOn(silver);
	}

//====================MAINTAIN INK===========================

	/*
	 * Ink is Low in Gold Station and trigger maintenance
	 * 
	 * 
	 */
	@Test
	public void testSilverLowInkMaintain() throws OverloadedDevice {
		logicSilver.maintain.setInitial(104858, 200);// what the station starts with
		logicSilver.maintain.print('c');// printing 1 char, removes 1 ink
		// now it should be in the state of lowInk. thus, maintenance should trigger.
		// should be true
		System.out.println(logicSilver.maintain.getMaintenance());
		assertTrue(logicSilver.maintain.getMaintenance());

	}

	/*
	 * Fix Ink is Low in Gold Station
	 * 
	 */
	@Test
	public void testFixSilverLowInk() {
		try {
			logicSilver.maintain.setInitial(104858, 200);// what the station starts with
			logicSilver.maintain.print('c');// this simulate that low ink has been reached
			logicSilver.maintain.maintainAddInk(10);// now i should be able to add ink

			assertFalse(logicSilver.maintain.getLowInkMessage());// getLowInkMessage should flip lowInkMeesage from true
																	// to False

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Ink is empty in Gold station
	 */
	@Test
	public void testEmptyInkSilver() {
		try {
			// how the station is setup
			// setup in this test was done in a way that their was 1 ink in the system.
			// setup by
			logicSilver.maintain.setInitial(1, 200);
			logicSilver.maintain.maintainAddInk(0);

			logicSilver.maintain.print('c');// using 1 ink. meaning their will be 0 ink left. aka, empty.

			assertTrue(logicSilver.maintain.getOutOfInkMessage()); // getOutOfInkMessage() will return true because the
																	// notifyInkEmpty

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Ink Spill occurred
	 */
	@Test(expected = OverloadedDevice.class)
	public void testInkSpillageSilver() throws OverloadedDevice {
		logicSilver.maintain.setInitial(1, 200);// already has 1 ink
		logicSilver.maintain.maintenanceStart();
		logicSilver.maintain.maintainAddInk(logicSilver.maintain.getMaxInkValue());// adding max value. makes it 1 more
																					// than
																					// the max should cause overloaded
																					// device
		logicSilver.maintain.maintenanceStart();

	}

	/*
	 * Attendent adds Ink? - change detected Asserting true if notifyAdd is called
	 * 
	 */
	@Test
	public void testAddedInkSilver() {
		try {
			logicSilver.maintain.setInitial(1, 200);
			logicSilver.maintain.maintainAddInk(2);// putting it to 2 means that we called the notified inkAdded
			// Checking if the false value of getInkAddedMessage() change to true
			assertTrue(logicSilver.maintain.getInkAddedMessage()); // getInkAddedMessage() will return true because the
																	// notifyInkAdded

		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Attendent removes to much Ink? - change detected have some field value in
	 * maintain that is equal to the ink quantity that is add. then remove the feild
	 * value by 1 everytime you print 1 char because that is equal to removal of 1
	 * ink Or assume gold printer is similar to that of bronze based on doc and then
	 * just call the gold ink remaining
	 * 
	 */
	@Test(expected = InvalidArgumentSimulationException.class)
	public void testRemoveInkSilver() throws OverloadedDevice, InvalidArgumentSimulationException {
		logicSilver.maintain.setInitial(-11, 200);

	}

	// ===========================PAPER=================================

	/*
	 * Ink is Low in Silver Station and trigger maintenance
	 * 
	 * 
	 */
	@Test
	public void testSilverLowPaperMaintain() throws OverloadedDevice {
		logicSilver.maintain.setInitial(104860, 103);// what the station starts with. 102> is consider low paper
		logicSilver.maintain.print('\n');// using 1 paper. meaning their will be 102 paper left. aka, low on paper
		// now it should be in the state of LowPaper. thus, maintenance should trigger.
		// should be true
		System.out.println(logicSilver.maintain.getMaintenance());
		assertTrue(logicSilver.maintain.getMaintenance());

	}
	// Get Low Paper Message is true

	/*
	 * Fix Paper is Low in Gold Station
	 * 
	 * 
	 */
	@Test
	public void testFixSilverLowPaperMaintain() throws OverloadedDevice {
		logicSilver.maintain.setInitial(104860, 103);// what the station starts with. 102> is consider low paper

		logicSilver.maintain.print('\n');// using 1 paper. meaning their will be 102 paper left. aka, low on paper

		// now it should be in the state of lowInk. thus, maintenance should trigger.
		// should be true
		// then allow us to add paper. fixing the issue
		logicSilver.maintain.maintainAddPaper(10);
		System.out.println("PaperB: " + logicSilver.maintain.getPaperAdded());

		// assert false because getLowPaperMessage should be false since there is no low
		// paper
		assertFalse(logicSilver.maintain.getLowPaperMessage());

	}

	/*
	 * Paper is empty in Gold station
	 */
	@Test
	public void testEmptyPaperSilver() {
		try {
			// how the station is setup
			// setup in this test was done in a way that their was 1 ink in the system.
			// setup by
			logicSilver.maintain.setInitial(100000, 1);
			System.out.println("Paper: " + logicSilver.maintain.getPaperAdded());

			logicSilver.maintain.print('\n');// using 1 paper. meaning their will be 0 paper left. aka, empty.
			System.out.println("Paper: " + logicSilver.maintain.getPaperAdded());

			assertTrue(logicSilver.maintain.getOutOfPaperMessage()); // getOutOfInkMessage() will return true because
																		// the
																		// notifyInkEmpty

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

		logicSilver.maintain.setInitial(104860, 2);// 2 papers are inside
		logicSilver.maintain.print('\n');// prints paper, updating the system. putting it maintaince mode since it has
											// only 1 paper

		logicSilver.maintain.maintainAddPaper(logicSilver.maintain.getMaxPaperValue());// adding max amount of paper =
																						// 1+max
																						// = overload

	}

	// ===========================Coin=================================

//======================SILVER STATION TEST============================

//======================BRONZE STATION TEST============================

	// =====================COIN TEST=======================

	// low coin count
	@Test
	public void testSilverLowCoinMaintain()
			throws OverloadedDevice, SimulationException, CashOverloadException, DisabledException {

		// for i in dispenser.getCapacity(), load a coin into dispenser. This will get
		// it to the threshold.
		// int dispenserCapacity = logicGold.maintain.dollarDispenser.getCapacity();

		logicSilver.maintain.setCoins(dollars);
		logicSilver.maintain.setCoins(dollars);

		// now it should be in the state of high coin count. thus, maintenance should
		// trigger. should be true

		System.out.println(logicSilver.maintain.getMaintenance());
		assertTrue(logicSilver.maintain.getMaintenance());

	}

	@Test
	public void testSilverFixLowCoin()
			throws OverloadedDevice, SimulationException, CashOverloadException, DisabledException {

		// for i in dispenser.getCapacity(), load a coin into dispenser. This will get
		// it to the threshold.
		logicSilver.maintain.setCoins(dollars);
		logicSilver.maintain.setCoins(dollars);

		logicSilver.maintain.maintainAddCoin(dollars); // receiving one coin.

		System.out.println(logicSilver.maintain.getMaintenance());
		assertFalse(logicSilver.maintain.getMaintenance());

	}

	// high coin count
	@Test
	public void testSilverHighCoinMaintain()
			throws OverloadedDevice, SimulationException, CashOverloadException, DisabledException {

		logicSilver.maintain.setCoins(dollars, dollars, dollars, dollars, dollars, dollars, dollars, dollars);

		System.out.println(logicSilver.maintain.getMaintenance());
		assertTrue(logicSilver.maintain.getMaintenance());

	}

	@Test
	public void testSilverFixHighCoin() throws OverloadedDevice, SimulationException, CashOverloadException,
			DisabledException, NoCashAvailableException {

		// for i in dispenser.getCapacity(), load a coin into dispenser. This will get
		// it to the threshold.
		logicSilver.maintain.setCoins(dollars, dollars, dollars, dollars, dollars, dollars, dollars, dollars);

		System.out.println("fix: " + logicSilver.maintain.getMaintenance());
		logicSilver.maintain.maintainEmitCoin(dollars);
		// unloaded all coins from station because it was too high, now adding three
		// coins to be within normal range.
		logicSilver.maintain.maintainAddCoin(dollars);
		logicSilver.maintain.maintainAddCoin(dollars);
		logicSilver.maintain.maintainAddCoin(dollars);

		System.out.println(logicSilver.maintain.getMaintenance());
		assertFalse(logicSilver.maintain.getMaintenance());

	}

//=========================BANKNOTE TEST=====================================\

	// low banknote count
	@Test
	public void testSilverLowBanknoteMaintain() throws CashOverloadException {

		logicSilver.maintain.setBanknotes(ten);
		logicSilver.maintain.setBanknotes(ten);

		// now it should be in the state of high coin count. thus, maintenance should
		// trigger. should be true

		System.out.println(logicSilver.maintain.getMaintenance());
		assertTrue(logicSilver.maintain.getMaintenance());

	}

}