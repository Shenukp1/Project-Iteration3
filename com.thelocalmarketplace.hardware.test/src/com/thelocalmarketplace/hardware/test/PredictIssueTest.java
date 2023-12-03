package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import com.jjjwelectronics.EmptyDevice;
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
import control.CheckForPrinterIssues;
import control.PredictIssueController;
import control.SelfCheckoutLogic;
import control.SessionController;
import powerutility.PowerGrid;

public class PredictIssueTest {
	SelfCheckoutStationBronze bronze;
	SelfCheckoutLogic logicBronze;

	/*
	 * Since receipt printer is bronze for all stations and dispensers + storage are
	 * universal we are only testing bronze station
	 */
	@Before
	public void setup() throws OverloadedDevice {
		bronze.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		bronze = new SelfCheckoutStationBronze();
		bronze.plugIn(PowerGrid.instance());
		bronze.turnOn();
		logicBronze = SelfCheckoutLogic.installOn(bronze);
	}

	// Fill both dispensers and storage units almost to max capacity
	// Bronze machine
	public void fillMachine() throws SimulationException, CashOverloadException {
		Banknote banknote = new Banknote(Currency.getInstance(Locale.CANADA), new BigDecimal(10.00));
		Coin coin = new Coin(Currency.getInstance(Locale.CANADA), new BigDecimal(1.00));
		for (int i = 0; i < bronze.getCoinStorage().getCapacity() - 2; i++) {
			this.bronze.getCoinStorage().load(coin);
		}
		for (int i = 0; i < bronze.getCoinDenominations().size(); i++) {
			BigDecimal coinDenoms = this.bronze.getCoinDenominations().get(i);
			ICoinDispenser coinDispenser = this.bronze.getCoinDispensers().get(coinDenoms);
			for (int j = 0; j < coinDispenser.getCapacity() - 2; j++) {
				coinDispenser.load(coin);
			}
		}
		for (int i = 0; i < bronze.getBanknoteStorage().getCapacity() - 2; i++) {
			this.bronze.getBanknoteStorage().load(banknote);
		}
		for (int j = 0; j < bronze.getBanknoteDenominations().length; j++) {
			BigDecimal noteDenoms = this.bronze.getBanknoteDenominations()[j];
			IBanknoteDispenser banknoteDispenser = this.bronze.getBanknoteDispensers().get(noteDenoms);
			for (int i = 0; i < banknoteDispenser.getCapacity() - 2; i++) {
				banknoteDispenser.load(banknote);
			}
		}
	}

	/*
	 * Individual method tests
	 */
	// Testing to see if banknotes are almost full in storage
	// empty machine
	@Test
	public void testNotAlmostFullBanknote() {
		// Storage is empty so expected = false
		assertFalse(logicBronze.predictController.banknoteAlmostFull());
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
	}

	// Testing to see if the banknote dispenser is almost empty
	// empty machine
	@Test
	public void testAlmostEmptyBanknote() {
		// Dispensers are empty expected = true
		// Should have one issues sent to attendant GUI
		assertTrue(logicBronze.predictController.banknoteAlmostEmpty());
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
	}

	// Testing to see if coins are almost full in storage
	// empty machine
	@Test
	public void testNotAlmostCoin() {
		// Storage is empty so expected = false
		assertFalse(logicBronze.predictController.coinAlmostFull());
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
	}

	// Testing to see if the coin dispensers are almost empty
	// empty machine
	@Test
	public void testAlmostEmptyCoin() {
		// Dispensers are empty expected = true
		// Should have one issues sent to attendant GUI
		assertTrue(logicBronze.predictController.coinAlmostEmpty());
	}

	// Testing to see if coin dispensers are almost empty
	// full machine
	@Test
	public void testNotAlmostEmptyCoin() throws SimulationException, CashOverloadException {
		fillMachine();
		logicBronze.predictController.predictFullCoin();
		// Dispensers are full expected = false
		// No issues should be sent to the attendant
		assertFalse(logicBronze.predictController.coinAlmostEmpty());
	}

	// Testing to see if issues are sent to attendant
	// When printer is full on paper
	@Test
	public void testFullPaper() throws OverloadedDevice {
		bronze.getPrinter().addPaper(1024);
		assertFalse(logicBronze.predictController.paperAlmostEmpty());
	}

	// Testing to see if issues are sent to attendant
	// When printer is low on paper
	@Test
	public void testLowPaper() throws OverloadedDevice, EmptyDevice {
		bronze.getPrinter().addPaper(1024);
		bronze.getPrinter().addInk(2000);
		for (int i = 0; i < 1020; i++) {
			bronze.getPrinter().print('c');
			bronze.getPrinter().print('\n');
		}
		assertTrue(logicBronze.predictController.paperAlmostEmpty());
	}

	// Testing to see if issues are sent to attendant
	// When printer is full on paper
	@Test
	public void testFullInk() throws OverloadedDevice {
//		bronze.getPrinter().addInk(1000);
//		logicBronze.predictController.predictLowInk();
//		assertFalse(logicBronze.predictController.inkAlmostEmpty());
//		assertEquals(logicBronze.predictController.numberOfIssues(), 0);
	}

	// Testing to see if issues are sent to attendant
	// When printer is low on paper
	@Test
	public void testLowInk() throws OverloadedDevice, EmptyDevice {
		// Would have to print over a million characters to test if ink is low?
		// Maybe have ink low based off how many receipts are printed
		bronze.getPrinter().addPaper(1024);
		bronze.getPrinter().addInk(1048576);
		for (int i = 0; i < 1020; i++) {
			for (int j = 0; j < 60; j++) {
				bronze.getPrinter().print('c');
			}
			bronze.getPrinter().print('\n');
		}
		logicBronze.predictController.predictLowInk();
		assertTrue(logicBronze.predictController.inkAlmostEmpty());
		assertEquals(logicBronze.predictController.numberOfIssues(), 1);
	}
}
