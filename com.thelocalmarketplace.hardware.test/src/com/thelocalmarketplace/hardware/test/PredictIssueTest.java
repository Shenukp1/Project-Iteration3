package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.OverloadedDevice;
import com.tdc.CashOverloadException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.Coin;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import ca.ucalgary.seng300.simulation.SimulationException;
import control.PredictIssueController;
import control.SelfCheckoutLogic;
import control.SessionController;
import powerutility.PowerGrid;

public class PredictIssueTest {

	SelfCheckoutStationBronze bronze;
	SelfCheckoutStationSilver silver;
	SelfCheckoutStationGold gold;
	
	SelfCheckoutStationBronze bronzeStub;
	SelfCheckoutLogicStub logicBronzeStub;

	SelfCheckoutLogic logicBronze;
	SelfCheckoutLogic logicSilver;
	SelfCheckoutLogic logicGold;
	
	SessionController sc;

	@Before
	public void setup() {
		bronze.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		bronze = new SelfCheckoutStationBronze();
		bronze.plugIn(PowerGrid.instance());
		bronze.turnOn();
		logicBronze = SelfCheckoutLogic.installOn(bronze);

		silver.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		silver = new SelfCheckoutStationSilver();
		silver.plugIn(PowerGrid.instance());
		silver.turnOn();
		logicSilver = SelfCheckoutLogic.installOn(silver);

		gold.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		gold = new SelfCheckoutStationGold();
		gold.plugIn(PowerGrid.instance());
		gold.turnOn();
		logicGold = SelfCheckoutLogic.installOn(gold);
	}

	// Fill both dispensers and storage units almost to max capacity
	// Bronze machine
	public void fillMachine() throws SimulationException, CashOverloadException {
		BigDecimal denoms = this.bronze.getCoinDenominations().get(0);
		Banknote banknote = new Banknote(Currency.getInstance(Locale.CANADA), new BigDecimal(10.00));
		Coin coin = new Coin(Currency.getInstance(Locale.CANADA), new BigDecimal(1.00));
		ICoinDispenser coinDispenser = this.bronze.getCoinDispensers().get(denoms);
		IBanknoteDispenser banknoteDispenser = this.bronze.getBanknoteDispensers().get(denoms);
		for (int i = 0; i < bronze.getCoinStorage().getCapacity() - 2; i++) {
			this.bronze.getCoinStorage().load(coin);
		}
		for (int i = 0; i < coinDispenser.getCapacity() - 2; i++) {
			coinDispenser.load(coin);
		}
		for (int i = 0; i < bronze.getBanknoteStorage().getCapacity() - 2; i++) {
			this.bronze.getBanknoteStorage().load(banknote);
		}
		for (int i = 0; i < banknoteDispenser.getCapacity() - 2; i++) {
			banknoteDispenser.load(banknote);
		}
	}

	/*
	 * Individual method tests
	 */
	
	
	public void fillPrinterSilver() throws OverloadedDevice {
		silver.getPrinter().addInk(100);
		silver.getPrinter().addPaper(200);
	}

	
	public void fillPrinterGold() throws OverloadedDevice {
		gold.getPrinter().addInk(100);
		gold.getPrinter().addPaper(200);
	}

	// Testing to see if banknotes are almost full in storage
	// empty machine
	@Test
	public void testNotAlmostFullBanknote() {
		logicBronze.predictController.predictFullBanknote();
		// Storage is empty so expected = false
		assertFalse(logicBronze.predictController.banknoteAlmostFull());
		assertEquals(logicBronze.predictController.numberOfIssues(), 0);
	}

	// Test to see if banknotes are almost full in storage
	// filled up machine
	@Test
	public void testAlmostFullBanknote() throws SimulationException, CashOverloadException {
		fillMachine();
		logicBronze.predictController.predictFullBanknote();
		// Storage is almost full expected = true
		// Should be one issue added to the attendant GUI
		assertTrue(logicBronze.predictController.banknoteAlmostFull());
		assertEquals(logicBronze.predictController.numberOfIssues(), 1);
	}

	// Testing to see if the banknote dispenser is almost empty
	// empty machine
	@Test
	public void testAlmostEmptyBanknote() {
		logicBronze.predictController.predictLowBanknote();
		// Dispensers are empty expected = true
		// Should have one issues sent to attendant GUI
		assertTrue(logicBronze.predictController.banknoteAlmostEmpty());
		assertEquals(logicBronze.predictController.numberOfIssues(), 1);
	}

	// Testing to see if banknote dispenser is almost empty
	// full machine
	@Test
	public void testNotAlmostEmptyBanknote() throws SimulationException, CashOverloadException {
		fillMachine();
		logicBronze.predictController.predictLowBanknote();
		// Dispensers are full expected = false
		// No issues should be sent to the attendant
		assertFalse(logicBronze.predictController.banknoteAlmostEmpty());
		assertEquals(logicBronze.predictController.numberOfIssues(), 0);
	}

	// Testing to see if coins are almost full in storage
	// empty machine
	@Test
	public void testNotAlmostCoin() {
		logicBronze.predictController.predictFullCoin();
		// Storage is empty so expected = false
		assertFalse(logicBronze.predictController.coinAlmostFull());
		assertEquals(logicBronze.predictController.numberOfIssues(), 0);
	}

	// Test to see if coins are almost full in storage
	// filled up machine
	@Test
	public void testAlmostFullCoin() throws SimulationException, CashOverloadException {
		fillMachine();
		logicBronze.predictController.predictFullCoin();
		// Storage is almost full expected = true
		// Should be one issue added to the attendant GUI
		assertTrue(logicBronze.predictController.coinAlmostFull());
		assertEquals(logicBronze.predictController.numberOfIssues(), 1);
	}

	// Testing to see if the coin dispensers are almost empty
	// empty machine
	@Test
	public void testAlmostEmptyCoin() {
		logicBronze.predictController.predictLowCoin();
		// Dispensers are empty expected = true
		// Should have one issues sent to attendant GUI
		assertTrue(logicBronze.predictController.coinAlmostEmpty());
		assertEquals(logicBronze.predictController.numberOfIssues(), 1);
	}

	// Testing to see if coin dispensers are almost empty
	// full machine
	@Test
	public void testNotAlmostEmptyCoin() throws SimulationException, CashOverloadException {
		fillMachine();
		logicBronze.predictController.predictLowCoin();
		// Dispensers are full expected = false
		// No issues should be sent to the attendant
		assertFalse(logicBronze.predictController.coinAlmostEmpty());
		assertEquals(logicBronze.predictController.numberOfIssues(), 0);
	}
	
	/*
	 * Now Testing methods when session has not started
	 * Collectively
	 */

	@Test
	public void testAllPredictionsNoErrors() {
		bronzeStub.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		bronzeStub = new SelfCheckoutStationBronze();
		bronzeStub.plugIn(PowerGrid.instance());
		bronzeStub.turnOn();
		logicBronzeStub = SelfCheckoutLogicStub.installOn(bronzeStub);
		sc = new SessionController(logicBronzeStub);
		
		PredictIssueController predictIssueController = new PredictIssueController(sc, bronzeStub);
		
		assertEquals(logicBronzeStub.predictController.numberOfIssues(), 2);
		
	}
//	// Testing to see if any errors are thrown
//	// when printer has both ink and paper
//	// Expected = false
//	@Test
//	public void testFullprinterSilver() throws OverloadedDevice {
//		fillPrinterSilver();
//		logicSilver.predictController.predictLowInk();
//		logicSilver.predictController.predictLowPaper();
//		// Both paper and ink are filled up
//		// expected = fasle. No issues sent to attendant
//		assertFalse(logicSilver.predictController.paperAlmostEmpty());
//		assertFalse(logicSilver.predictController.inkAlmostEmpty());
//		assertEquals(logicBronze.predictController.numberOfIssues(), 0);
//	}
//
//	// Testing to see if any errors are thrown
//	// when printer does not have enough ink or paper
//	// Expected = True
//	@Test
//	public void testAlmostEmptyprinterSilver() throws OverloadedDevice {
//		silver.getPrinter().addInk(3);
//		silver.getPrinter().addPaper(3);
//		logicSilver.predictController.predictLowInk();
//		logicSilver.predictController.predictLowPaper();
//		// Both paper and ink are filled up
//		// Two issues sent to attendant
//		assertTrue(logicSilver.predictController.paperAlmostEmpty());
//		assertTrue(logicSilver.predictController.inkAlmostEmpty());
//		assertEquals(logicSilver.predictController.numberOfIssues(), 2);
//	}
//
//	// Testing to see if any errors are thrown
//	// when printer has both ink and paper
//	// Expected = false
//	@Test
//	public void testFullprinterGold() throws OverloadedDevice {
//		fillPrinterGold();
//		logicGold.predictController.predictLowInk();
//		logicGold.predictController.predictLowPaper();
//		// Both paper and ink are filled up
//		// No issues sent to attendant
//		assertFalse(logicGold.predictController.paperAlmostEmpty());
//		assertFalse(logicGold.predictController.inkAlmostEmpty());
//		assertEquals(logicGold.predictController.numberOfIssues(), 0);
//	}
//
//	// Testing to see if any errors are thrown
//	// when printer does not have enough ink or paper
//	// Expected = True
//	@Test
//	public void testAlmostEmptyprinterGold() throws OverloadedDevice {
//		gold.getPrinter().addInk(3);
//		gold.getPrinter().addPaper(3);
//		logicGold.predictController.predictLowInk();
//		logicGold.predictController.predictLowPaper();
//		// Both paper and ink are filled up
//		// two issues sent to attendant
//		assertTrue(logicGold.predictController.paperAlmostEmpty());
//		assertTrue(logicGold.predictController.inkAlmostEmpty());
//		assertEquals(logicGold.predictController.numberOfIssues(), 2);
//	}
//
//	// Tests to check if the bronze printer
//	// Issues get ignored since no support
//	// Expected = false
//	@Test
//	public void testBronzePrinterIgnored() {
//		logicBronze.predictController.predictLowInk();
//		logicBronze.predictController.predictLowPaper();
//		// Errors should be silently ignored
//		assertFalse(logicBronze.predictController.paperAlmostEmpty());
//		assertFalse(logicGold.predictController.inkAlmostEmpty());
//		assertEquals(logicBronze.predictController.numberOfIssues(), 0);
//
//	}

}
