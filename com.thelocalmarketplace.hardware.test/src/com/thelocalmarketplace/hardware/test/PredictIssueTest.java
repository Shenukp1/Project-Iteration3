package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import com.tdc.CashOverloadException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.Coin;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import ca.ucalgary.seng300.simulation.SimulationException;
import control.SelfCheckoutLogic;
import powerutility.PowerGrid;

public class PredictIssueTest {
	SelfCheckoutStationBronze bronze;
	SelfCheckoutStationSilver silver;
	SelfCheckoutStationGold gold;
	SelfCheckoutLogic logicBronze;
	SelfCheckoutLogic logicSilver;
	SelfCheckoutLogic logicgold;

	@Before
	public void setup() {
		bronze.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		bronze = new SelfCheckoutStationBronze();
		bronze.plugIn(PowerGrid.instance());
		bronze.turnOn();
		logicBronze = SelfCheckoutLogic.installOn(bronze);
	}

	// Fill both dispensers and storage units almost to max capacity
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

	// Testing to see if banknotes are almost full in storage
	// empty machine
	@Test
	public void testNotAlmostFullBanknote() {
		logicBronze.predictController.predictFullBanknote();
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
}
