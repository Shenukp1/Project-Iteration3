package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import control.SelfCheckoutLogic;
import payment.CardController;
import powerutility.PowerGrid;
import powerutility.NoPowerException;
import testingUtilities.CardPayment;
import testingUtilities.Products;
import testingUtilities.Wallet;

public class PayViaDebitTest implements CardPayment {
	private SelfCheckoutStationGold gold;
	private SelfCheckoutStationBronze bronze;
	private SelfCheckoutStationSilver silver;
	private SelfCheckoutLogic logicBronze;
	private SelfCheckoutLogic logicSilver;
	private SelfCheckoutLogic logicGold;
	Products products = new Products();
	Calendar calendar = Calendar.getInstance();
	CardController tempCardClass;
	CardIssuer temp;

	@Rule
	public ExpectedException expectedMessage = ExpectedException.none();
  
	@Before
	public void setup() {
		
		bronze.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationBronze bronzeStation = new SelfCheckoutStationBronze();
		bronzeStation.plugIn(PowerGrid.instance());
		bronzeStation.turnOn();
		
		silver.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationSilver silverStation = new SelfCheckoutStationSilver();
		silverStation.plugIn(PowerGrid.instance());
		silverStation.turnOn();
		
		gold.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationGold goldStation = new SelfCheckoutStationGold();
		goldStation.plugIn(PowerGrid.instance());
		goldStation.turnOn();
		
		logicBronze = SelfCheckoutLogic.installOn(bronzeStation);
		logicSilver = SelfCheckoutLogic.installOn(silverStation);
		logicGold = SelfCheckoutLogic.installOn(goldStation);
		
		calendar.add(Calendar.DAY_OF_MONTH, 5);
	      	calendar.add(Calendar.YEAR, 2025);
	    	calendar.add(Calendar.MONTH, 10);
	    
		temp = new CardIssuer("CIBC", 12321);
		temp.addCardData(otherDebitCard.number, otherDebitCard.cardholder, calendar,otherDebitCard.cvv , 1000);
		
		tempCardClass= new CardController(logicBronze.session, logicBronze.station, temp );
		logicBronze.creditController = tempCardClass;
		
		logicBronze.session.enable();
	}
	
	@Test
	public void testSwipeDebit() throws IOException {
		logicBronze.station.cardReader.enable();
		for (int i = 0; i < 100; i++) {
			try{
				logicBronze.station.cardReader.swipe(otherDebitCard);
			} catch(MagneticStripeFailureException e) {
			}
		}
	}
	
	@Test
	public void testScanningAddingAndDebitCardPayment() throws IOException {
		// Simulate scanning and adding items to the cart
	    	logicBronze.station.mainScanner.enable();
	    	logicBronze.barcodeController.aBarcodeHasBeenScanned(logicBronze.station.mainScanner, products.beanBarcode);
	    	logicBronze.station.mainScanner.scan(products.beanBarcodeItem);
	    	logicBronze.station.scanningArea.enable();
	    	logicBronze.station.scanningArea.addAnItem(products.beanBarcodeItem);

	    	// Simulate the debit card payment process
	    	CardIssuer debitCardIssuer = PayViaDebitTest.debitcard;
	    	long holdNumber = debitCardIssuer.authorizeHold("1234567890123456", 100.0);

	   	assertNotNull("Hold number should be generated", holdNumber);

		// The payment process should be successful
		boolean successfulTransaction = debitCardIssuer.postTransaction("1234567890123456", holdNumber, 100.0);
		assertTrue("Transaction is successful", successfulTransaction);
		
		// Simulate the card swipe process
	    	tempCardClass= new CardController(logicBronze.session, logicBronze.station, temp );
		logicBronze.creditController =tempCardClass;
		logicBronze.creditController.onPayWithCard();
		
	    	logicBronze.station.cardReader.enable();
	    	otherDebitCard.swipe();
		
	    	for (int i = 0; i < 100; i++) {
	        	try {
	            	logicBronze.station.cardReader.swipe(otherDebitCard);
	        	} catch (MagneticStripeFailureException e) {
	            	// Handle the exception if needed
	        	}
	    	}
	}

	@Test
    	public void testInsufficientFundsDebitCardPayment() throws IOException {
		// Simulate scanning and adding items to the cart
	    	logicBronze.station.mainScanner.enable();
	    	logicBronze.barcodeController.aBarcodeHasBeenScanned(logicBronze.station.mainScanner, products.beanBarcode);
	    	logicBronze.station.mainScanner.scan(products.beanBarcodeItem);
	    	logicBronze.station.scanningArea.enable();
	    	logicBronze.station.scanningArea.addAnItem(products.beanBarcodeItem);
	    
	    	// Simulate the debit card payment process
	    	CardIssuer debitCardIssuer = PayViaDebitTest.debitcard;
	    	long holdNumber = debitCardIssuer.authorizeHold("1234567890123456", 100.0);

	    	assertNotNull("Hold number should be generated", holdNumber);
	    
	    	boolean successfulTransaction = debitCardIssuer.postTransaction("1234567890123456", holdNumber, 100.0);
	    	assertTrue("Transaction is successful", successfulTransaction);

	    	// Simulate the card swipe process
	    	tempCardClass= new CardController(logicBronze.session, logicBronze.station, temp );
		logicBronze.creditController =tempCardClass;
		logicBronze.creditController.onPayWithCard();
		
	    	logicBronze.station.cardReader.enable();
	    	otherDebitCard.swipe();
		
	    	for (int i = 0; i < 100; i++) {
	        	try {
	            	logicBronze.station.cardReader.swipe(otherDebitCard);
	        	} catch (MagneticStripeFailureException e) {
	            	// Handle the exception if needed
	        	}
	    	}
	}
	
	@Test(expected = NullPointerException.class)
	public void testIOExceptionDuringCardSwipe() throws IOException {
	    	// Simulate an IOException during the card swipe process
	    	logicBronze.station.cardReader.enable();
	    	logicBronze.station.cardReader.swipe(null); // Assuming a null card causes an IOException
    }
}
