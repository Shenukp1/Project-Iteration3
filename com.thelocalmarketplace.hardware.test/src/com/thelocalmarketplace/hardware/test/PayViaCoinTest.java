package com.thelocalmarketplace.hardware.test;


import static org.junit.Assert.*;
import java.util.Currency;


import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import ca.ucalgary.seng300.simulation.SimulationException;
import control.SelfCheckoutLogic;
import payment.CoinController;
import payment.ChangeController;
import powerutility.PowerGrid;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.Products;
import testingUtilities.Wallet;


public class PayViaCoinTest implements DollarsAndCurrency{

 
	/*
	 * make three types of station to test, we'll have to test all three types for each kind of test.
	 * Maybe there's a fast way to plug in these objects than repeating a test method?
	 */
	SelfCheckoutStationBronze bronze;
	SelfCheckoutStationSilver silver;
	SelfCheckoutStationGold gold;
	/*
	 * each station needs its own logic instance for setup.
	 */
	SelfCheckoutLogic logicBronze;
	SelfCheckoutLogic logicSilver;
	SelfCheckoutLogic logicGold;

	/*wallet to add banknotes to a slot. may not be needed because of Dollars And Currency Utility
	 *  
	 */
	Wallet wallet=new Wallet(); 
	/*
	 * Products? where are usharabs products?
	 */
	Products products = new Products();

	@Rule
		public ExpectedException expectedMessage = ExpectedException.none();

		@Before
		public void testSetup (){
			
			/*
			 * set up cash to pay
			 */
			wallet.cash(2,3,4,5,1);
			wallet.coins(2,3,4,5,1);
			/* First, create an instance of the SelfCheckoutStation (Bronze, Silver, Gold).
			 */
			//bronze
			
			bronze.resetConfigurationToDefaults();
			bronze.configureCoinDenominations(coinDenominations);
			PowerGrid.engageUninterruptiblePowerSource();
			PowerGrid.instance().forcePowerRestore();
			SelfCheckoutStationBronze bronze = new SelfCheckoutStationBronze();
			bronze.plugIn(PowerGrid.instance());
			bronze.turnOn();
			
			//silver
			silver.resetConfigurationToDefaults();
			PowerGrid.engageUninterruptiblePowerSource();
			PowerGrid.instance().forcePowerRestore();
			SelfCheckoutStationSilver silver = new SelfCheckoutStationSilver();
			silver.plugIn(PowerGrid.instance());
			silver.turnOn();
			
			//gold
			gold.resetConfigurationToDefaults();
			PowerGrid.engageUninterruptiblePowerSource();
			PowerGrid.instance().forcePowerRestore();
			SelfCheckoutStationGold gold = new SelfCheckoutStationGold();
			gold.plugIn(PowerGrid.instance());
			gold.turnOn();
			
			/*
			 * Now that we have a station, we need to link the software to it. 
			 */
			
		 logicBronze = SelfCheckoutLogic.installOn(bronze);
		 logicSilver = SelfCheckoutLogic.installOn(silver);
		 logicGold = SelfCheckoutLogic.installOn(gold);
	
		 try {
			logicBronze.station.coinStorage.load(dollars, penny, nickle, dime, quarter);
		} catch (SimulationException | CashOverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
			logicBronze.station.configureCoinDenominations(coinDenominations);

			
		}
	
		//tests good coin listener
	
	@Test 
	public void testSuccessfulPayment() throws Exception {
		
	
		logicBronze.station.coinSlot.enable();
		logicBronze.coinController.onPayViaCoin();
		logicBronze.station.coinSlot.receive(dollars);
		
	}
	
	@Test(expected = DisabledException.class)
	public void testInvalidState() throws Exception {
		
		logicBronze.station.coinSlot.receive(dollars);
		}
	
	//tests bad coin listener
	@Test 
	public void invalidCoinTest() throws Exception {
		
		
		logicBronze.station.coinSlot.enable();
		logicBronze.station.coinSlot.receive(euros);
		expectedMessage.expect(SimulationException.class);
	    	expectedMessage.expectMessage("The coin(s) you entered is invalid. Please try again.");
	    	logicBronze.coinController.invalidCoinDetected(logicBronze.station.coinValidator);

	}


	@Test
	public void testPaymentWithChange() throws Exception {
		logicBronze.station.coinSlot.enable();
		logicBronze.coinController.onPayViaCoin();
		logicBronze.station.coinSlot.receive(dollars);
		
	}
	

}


