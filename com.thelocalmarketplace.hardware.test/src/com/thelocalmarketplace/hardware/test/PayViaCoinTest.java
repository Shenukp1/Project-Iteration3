package com.thelocalmarketplace.hardware.test;


import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Currency;


import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;


import ca.ucalgary.seng300.simulation.SimulationException;
import control.SelfCheckoutLogic;
import payment.CoinController;
import payment.ChangeController;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.Products;
import testingUtilities.Wallet;


@RunWith(Parameterized.class)
public class PayViaCoinTest implements DollarsAndCurrency, CardPayment {
/*
 * make three types of station to test, we'll have to test all three types for each kind of test.
 * Maybe there's a fast way to plug in these objects than repeating a test method?
 */
public static SelfCheckoutStationBronze bronze;
public static SelfCheckoutStationSilver silver;
public static SelfCheckoutStationGold gold;

private AbstractSelfCheckoutStation station;
/*
 * Each station needs 1 logic
 */
SelfCheckoutLogic logic;

/*
 * listener objects
 */

/*
 * Products? where are usharabs products?
 */
Products products = new Products();



@Rule
	public ExpectedException expectedMessage = ExpectedException.none();
@Parameterized.Parameters
public static Collection<AbstractSelfCheckoutStation[]> data() {
	//bronze
	bronze.resetConfigurationToDefaults();
	bronze.configureBanknoteDenominations(bankNoteDenominations);
	
	PowerGrid.engageUninterruptiblePowerSource();
	PowerGrid.instance().forcePowerRestore();
	SelfCheckoutStationBronze bronze = new SelfCheckoutStationBronze();
	bronze.plugIn(PowerGrid.instance());
	bronze.turnOn();
	
	
	
	//silver
	silver.resetConfigurationToDefaults();
	silver.configureBanknoteDenominations(bankNoteDenominations);
	
	
	PowerGrid.engageUninterruptiblePowerSource();
	PowerGrid.instance().forcePowerRestore();
	SelfCheckoutStationSilver silver = new SelfCheckoutStationSilver();
	silver.plugIn(PowerGrid.instance());
	silver.turnOn();
	
	//gold
	gold.resetConfigurationToDefaults();
	gold.configureBanknoteDenominations(bankNoteDenominations);
	
	PowerGrid.engageUninterruptiblePowerSource();
	PowerGrid.instance().forcePowerRestore();
	SelfCheckoutStationGold gold = new SelfCheckoutStationGold();
	gold.plugIn(PowerGrid.instance());
	gold.turnOn();
	
    return Arrays.asList(new AbstractSelfCheckoutStation[][] {
            { bronze },
            { silver  },
            { gold  },
            // Add more instances as needed
        });
}
public PayViaCoinTest(AbstractSelfCheckoutStation station) {
    this.station = station;
}
		@Before
		public void testSetup (){
	
			
			/*
			 * Now that we have a station, we need to link the software to it. 
			 */
			
		 logic= SelfCheckoutLogic.installOn(station);
	
	
		 try {
			logic.station.getCoinStorage().load(dollars, penny, nickle, dime, quarter);
		} catch (SimulationException | CashOverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
			logic.station.configureCoinDenominations(coinDenominations);

			
		}
	
		//tests good coin listener
	
	@Test 
	public void testSuccessfulPayment() throws Exception {
		
	
		logic.station.getCoinSlot().enable();
		logic.coinController.onPayViaCoin();
		logic.station.getCoinSlot().receive(dollars);
		
	}
	
	@Test(expected = DisabledException.class)
	public void testInvalidState() throws Exception {
		
		logic.station.getCoinSlot().receive(dollars);
		}
	
	//tests bad coin listener
	@Test 
	public void invalidCoinTest() throws Exception {
		
		
		logic.station.getCoinSlot().enable();
		logic.station.getCoinSlot().receive(euros);
		expectedMessage.expect(SimulationException.class);
	    	expectedMessage.expectMessage("The coin(s) you entered is invalid. Please try again.");
	    	logic.coinController.invalidCoinDetected(logic.station.getCoinValidator());

	}


	@Test
	public void testPaymentWithChange() throws Exception {
		logic.station.getCoinSlot().enable();
		logic.coinController.onPayViaCoin();
		logic.station.getCoinSlot().receive(dollars);
		
	}
	

}


