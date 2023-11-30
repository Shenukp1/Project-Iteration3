package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.ChipFailureException;
import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import control.SelfCheckoutLogic;
import payment.PaymentCardController;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.Products;

public class PaymentCardControllerTest implements CardPayment{
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
	
	PaymentCardController tempCardClass;
	
	  CardIssuer temp; 
	
	/*
	 * products
	 */
	Products products = new Products();
	
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

	@Before
	public void setUp() throws Exception {
		
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
		
		tempCardClass= new PaymentCardController(logicBronze.session, logicBronze.station, temp );
		logicBronze.creditController =tempCardClass;
		logicGold.creditController = tempCardClass;
	 logicBronze.session.enable();

	}

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
	
	@Test
	public void testTap() throws IOException {
		
		logicBronze.station.cardReader.enable();
		logicBronze.station.cardReader.enable();
	
		for (int i =0; i<100; i++) {
			try {
		logicBronze.station.cardReader.tap(otherCreditCard);
			}
		catch(ChipFailureException e) {
		}
			}
	}
	
	@Test
	public void testInsert() throws IOException {
		
		logicBronze.station.cardReader.enable();
	
		for (int i =0; i<100; i++) {
			try {
		logicBronze.station.cardReader.insert(otherCreditCard, "911");
		logicBronze.station.cardReader.remove();
			}
		catch(ChipFailureException e) {
			logicBronze.station.cardReader.remove();// remove card even after chip failure
		}
			}
	}
	

	
	@Test
	public void payBeforeSessionStarts() throws IOException {
		
        System.setErr(new PrintStream(errContent));
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
		tempCardClass= new PaymentCardController(null, logicBronze.station, temp );
		logicBronze.creditController =tempCardClass;
		logicBronze.creditController.onPayWithCard();
		
        assertTrue(errContent.toString().contains("Please start a session first"));		
        System.setErr(originalErr);
			
		
	}
	
	@Test
	public void payWithEmptyCart() throws IOException {
		
        System.setErr(new PrintStream(errContent));
		
		logicGold.station.mainScanner.enable();
		logicGold.barcodeController.aBarcodeHasBeenScanned(logicBronze.station.mainScanner, products.beanBarcode);
		logicGold.station.mainScanner.scan(products.beanBarcodeItem);
	
		logicGold.station.baggingArea.enable();
		
		
		
		try {
			logicGold.station.printer.addPaper(500);
			logicGold.station.printer.addInk(1000);
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}

		logicGold.creditController.onPayWithCard();
		
        assertTrue(errContent.toString().contains("Please add items to pay for"));		
        System.setErr(originalErr);
			
		
	}
	
	@Test
	public void scanAndPaywithSwipe() throws IOException {
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
		otherCreditCard.swipe();
		for (int i =0; i<100; i++) {
			try {
		logicBronze.station.cardReader.swipe(otherCreditCard);
		
			}
		catch(MagneticStripeFailureException e) {
		}
			}
		
	}
	@Test
	public void scanAndPaywithTap() throws IOException {
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
		otherCreditCard.swipe();
		for (int i =0; i<100; i++) {
			try {
		logicBronze.station.cardReader.tap(otherCreditCard);
		
			}
		catch(ChipFailureException e) {
		}
			}
		
	}
	
	@Test
	public void scanAndPaywithInsert() throws IOException {
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
		otherCreditCard.swipe();
		for (int i =0; i<100; i++) {
			try {
		logicBronze.station.cardReader.insert(otherCreditCard, "911");
		logicBronze.station.cardReader.remove();
		
			}
		catch(ChipFailureException e) {
			logicBronze.station.cardReader.remove();

		}
			}
		
	}
	
	
	@Test(expected = NullPointerSimulationException.class)
	public void corruptCardData1() {
		
		CardDataStub cardDataStub = new CardDataStub(null, "122222", "JJ");
		try {
		logicBronze.creditController.theDataFromACardHasBeenRead(cardDataStub);}
		catch (NullPointerSimulationException e) {
			assertEquals("Null is not a valid cardType.", e.getMessage());
			
			throw e;
		}
		
	}
	
	@Test
	public void corruptCardData2() {
		
		String typeString = "test";
		System.setErr(new PrintStream(errContent));
		
		CardDataStub cardDataStub = new CardDataStub("test", "122222", "JJ");
		
		logicBronze.creditController.theDataFromACardHasBeenRead(cardDataStub);
		assertTrue(errContent.toString().contains("credit/debit card expected, type used is:"));		
      System.setErr(originalErr);
		
	}
	
	@Test(expected = NullPointerSimulationException.class)
	public void corruptCardData3() {
		
		CardDataStub cardDataStub = new CardDataStub("credit", "122222", null);
		try {
		logicBronze.creditController.theDataFromACardHasBeenRead(cardDataStub);}
		catch (NullPointerSimulationException e) {
			System.out.println(e.getMessage());
			assertEquals("Null is not a valid cardHolder.", e.getMessage());
			
			throw e;
		}

	}
	
	@Test(expected = NullPointerSimulationException.class)
	public void corruptCardData4() {
		
		CardDataStub cardDataStub = new CardDataStub("credit", null, "JJ");
		try {
		logicBronze.creditController.theDataFromACardHasBeenRead(cardDataStub);}
		catch (NullPointerSimulationException e) {
			System.out.println(e.getMessage());
			assertEquals("Null is not a valid cardNumber.", e.getMessage());
			
			throw e;
		}}
	@After
	public void tearDown() throws Exception {
	}

	
	
	

}
