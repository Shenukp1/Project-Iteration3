package com.thelocalmarketplace.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import control.SelfCheckoutLogic;
import payment.CardController;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.Products;
import testingUtilities.Wallet;

public class PayViaCreditTest implements CardPayment {
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
/*
 * we need a calendar to use cardIssuer
 */
	Calendar calendar = Calendar.getInstance();
	
	CardController tempCardClass;
	
	  CardIssuer temp; 
	
	/*
	 * products
	 */
	Products products = new Products();
	@Rule
	public ExpectedException expectedMessage = ExpectedException.none();

	@Before
	public void setUp() {
		  calendar.add(Calendar.DAY_OF_MONTH, 7);
		  calendar.add(Calendar.YEAR, 2024);
		  calendar.add(Calendar.MONTH, 02);
		
		/*
		 * set up a kind of card to pay. Calendar is a pain for this card maybe ignore this card
		 */
		
		//creditcard.addCardData("232", "Joel", Calendar.getInstance()., "323", 500);
		/* First, create an instance of the SelfCheckoutStation (Bronze, Silver, Gold).
		 */
		//bronze
		bronze.resetConfigurationToDefaults();
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

		 
	logicBronze = SelfCheckoutLogic.installOn(bronze);
	 logicSilver = SelfCheckoutLogic.installOn(silver);
	 logicGold = SelfCheckoutLogic.installOn(gold);
	
	
	  
	
	 temp = new CardIssuer("TD trust", 12321);
		
		logicBronze.cardIssuer= temp;
	  logicBronze.cardIssuer = new CardIssuer("TD trust", 12321);
	 
		logicBronze.cardIssuer.addCardData(otherCreditCard.number, otherCreditCard.cardholder,
				calendar,otherCreditCard.cvv , 1000);
		temp.addCardData(otherCreditCard.number, otherCreditCard.cardholder,
				calendar,otherCreditCard.cvv , 1000);
		
		tempCardClass= new CardController(logicBronze.session, logicBronze.station, temp );
		logicBronze.creditController =tempCardClass;
	 logicBronze.session.enable();

	}


 /*
  * there is a chance of a swipe failing
  */
	@Test
	public void testSwipe() throws IOException {
		
		logicBronze.station.cardReader.enable();
		logicBronze.station.cardReader.enable();
	
		for (int i =0; i<100; i++) {
			try {
		logicBronze.station.cardReader.swipe(otherCreditCard);
			}
		catch(MagneticStripeFailureException e) {
		}
			}
	}

	@Test(expected = Exception.class)
	public void testIOExceptionDuringCardSwipe() throws IOException {
	    	// Simulate an IOException during the card swipe process
	    	logicBronze.station.cardReader.enable();
	    	
	    	logicBronze.station.cardReader.swipe(null); // Assuming a null card causes an IOException
	}
	
	@Test
	public void scanAndPay() throws IOException {
		logicBronze.session.Cart.add(products.beanBarcodedProduct);
		
		logicBronze.station.mainScanner.enable();
		logicBronze.barcodeController.aBarcodeHasBeenScanned(logicBronze.station.mainScanner, products.beanBarcode);
		logicBronze.station.mainScanner.scan(products.beanBarcodeItem);
	
		logicBronze.station.baggingArea.enable();
		logicBronze.station.baggingArea.addAnItem(products.beanBarcodeItem);
		
		
		
		try {
			logicBronze.station.printer.addPaper(500);
			logicBronze.station.printer.addInk(1000);
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		logicBronze.creditController.onPayWithCard();
		logicBronze.station.cardReader.enable();
		otherCreditCard.swipe();
		for (int i =0; i<100; i++) {
			try {
		logicBronze.station.cardReader.swipe(otherCreditCard);
		
			}
		catch(MagneticStripeFailureException e) {
		}
			}
		
	}
	

}
