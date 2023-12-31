/*Group P3-6***
Andy Tang 10139121
Ayman Inayatali Momin 30192494
Darpal Patel 30088795
Dylan Dizon 30173525
Ellen Bowie 30191922
Emil Huseynov 30171501
Ishita Udasi 30170034
Jason Very 30222040
Jesse Leinan 00335214
Joel Parker 30021079
Kear Sang Heng 30087289
Khadeeja Abbas 30180776
Kian Sieppert 30134666
Michelle Le 30145965
Raja Muhammed Omar 30159575
Sean Gilluley 30143052
Shenuk Perera 30086618
Simrat Virk 30000516
Sina Salahshour 30177165
Tristan Van Decker 30160634
Usharab Khan 30157960
YiPing Zhang 30127823*/
package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
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
import ca.ucalgary.seng300.simulation.SimulationException;
import control.SelfCheckoutLogic;
import control.SessionController;
import powerutility.PowerGrid;

public class PredictIssueTest {

	SelfCheckoutStationBronze bronze;
	SelfCheckoutLogic logicBronze;

	SelfCheckoutStationBronze bronzeStub;
	SelfCheckoutLogicStub logicBronzeStub;
	SessionController sc;

	Banknote banknote;
	Coin coin;
	BigDecimal denoms;

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
		banknote = new Banknote(Currency.getInstance(Locale.CANADA), new BigDecimal(10.00));
		coin = new Coin(Currency.getInstance(Locale.CANADA), new BigDecimal(1.00));
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

	// Testing to see if issues are sent to attendant
	// When printer is full on paper
	@Test
	public void testFullPaper() throws OverloadedDevice {
		bronze.getPrinter().addPaper(1000);
		logicBronze.predictController.predictLowPaper();
		assertFalse(logicBronze.predictController.paperAlmostEmpty());
		assertEquals(logicBronze.predictController.numberOfIssues(), 0);
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
		logicBronze.predictController.predictLowPaper();
		assertTrue(logicBronze.predictController.paperAlmostEmpty());
	}

	// Testing to see if issues are sent to attendant
	// When printer is full on paper
	@Test
	public void testFullInk() throws OverloadedDevice {
		bronze.getPrinter().addInk(1000);
		logicBronze.predictController.predictLowInk();
		assertFalse(logicBronze.predictController.inkAlmostEmpty());
		assertEquals(logicBronze.predictController.numberOfIssues(), 0);
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
	/*
	 * Now Testing methods when session has not started Collectively
	 */

	@Test
	public void testAllPredictionsMultipleErrors()
			throws SimulationException, CashOverloadException, OverloadedDevice, EmptyDevice {
		bronzeStub.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		bronzeStub = new SelfCheckoutStationBronze();
		bronzeStub.plugIn(PowerGrid.instance());
		bronzeStub.turnOn();
		
		logicBronzeStub = SelfCheckoutLogicStub.installOn(bronzeStub);
		// Calling all methods with empty dispensers
		// Expected = 2 issues sent to attendant 
		assertEquals(logicBronzeStub.predictController.numberOfIssues(), 2);
		

	}

}
