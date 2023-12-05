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


import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Currency;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
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
public class PayViaCoinTest implements DollarsAndCurrency, CardPayment, CoinValidatorObserver {
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
public String notify="";
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

			logic.station.getCoinValidator().attach(this);
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
		
		notify="";
		logic.station.getCoinSlot().enable();
		
				logic.station.getCoinSlot().receive(euros);
	Assert.assertTrue(notify=="invalidCoinDetected");
	    	//expectedMessage.expectMessage("The coin(s) you entered is invalid. Please try again.");
	    	//logic.coinController.invalidCoinDetected(logic.station.getCoinValidator());

	}


	@Test
	public void testPaymentWithChange() throws Exception {
		logic.station.getCoinSlot().enable();
		logic.coinController.onPayViaCoin();
		logic.station.getCoinSlot().receive(dollars);
		
	}
	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		notify="invalidCoinDetected";
		
	}
	

}


