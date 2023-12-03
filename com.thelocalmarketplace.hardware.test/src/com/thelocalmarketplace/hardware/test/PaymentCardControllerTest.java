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
import item.AddItemCatalogue;
import payment.PaymentCardController;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.LoadProductDatabases;
import testingUtilities.Products;

public class PaymentCardControllerTest implements DollarsAndCurrency, CardPayment, LoadProductDatabases{
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
	
	
	  
	
	temp = new CardIssuer("TD trust", 12321);//set bank
		
	logicBronze.cardIssuer = new CardIssuer("TD trust", 12321);//set bank for station logic
	 
	logicBronze.cardIssuer.addCardData(otherCreditCard.number, otherCreditCard.cardholder,
				calendar,otherCreditCard.cvv , 1000);//adding card data for cards
	temp.addCardData(otherCreditCard.number, otherCreditCard.cardholder,
				calendar,otherCreditCard.cvv , 1000);
	tempCardClass= new PaymentCardController(logicBronze.session, logicBronze.station, temp );
	logicBronze.creditController =tempCardClass;//assign payment controller for card
	logicGold.creditController = tempCardClass;
	logicBronze.session.enable();//enable session

	}

	/**
	 * Tests swipe 100 times since there is a chance of swipe failing 
	 * @throws IOException
	 */
	@Test
	public void testSwipe() throws IOException {
		//enable card reader
		logicBronze.station.getCardReader().enable();
	
		for (int i =0; i<100; i++) {
			try {
		logicBronze.station.getCardReader().swipe(otherCreditCard);
			}
		catch(MagneticStripeFailureException e) {
		}
			}
	}
	
	/**
	 * Tests tap 100 times since there is a chance of tap failing 
	 * @throws IOException
	 */
	@Test
	public void testTap() throws IOException {
		//enable card reader
		logicBronze.station.getCardReader().enable();
	
		for (int i =0; i<100; i++) {
			try {
		logicBronze.station.getCardReader().tap(otherCreditCard);
			}
		catch(ChipFailureException e) {
		}
			}
	}
	
	/**
	 * Tests swipe 100 times since there is a chance of insert failing 
	 * @throws IOException
	 */
	@Test
	public void testInsert() throws IOException {
		//enable card reader
		logicBronze.station.getCardReader().enable();
	
		for (int i =0; i<100; i++) {
			try {
		logicBronze.station.getCardReader().insert(otherCreditCard, "911");
		logicBronze.station.getCardReader().remove();
			}
		catch(ChipFailureException e) {
			logicBronze.station.getCardReader().remove();// remove card even after chip failure
		}
			}
	}
	

	/**
	 * Tests system behavior if customer tries to pay before session starts
	 * @throws IOException
	 */
	@Test
	public void payBeforeSessionStarts() throws IOException {
		
        System.setErr(new PrintStream(errContent));

		tempCardClass= new PaymentCardController(null, logicBronze.station, temp );
		logicBronze.creditController =tempCardClass;
		logicBronze.creditController.onPayWithCard();
		
        assertTrue(errContent.toString().contains("Please start a session first"));		
        System.setErr(originalErr);
			
		
	}
	/**
	 * Tests system behaviour if customer tries to pay before adding item to card
	 * @throws IOException
	 */
	
	@Test
	public void payWithEmptyCart() throws IOException {
		
        System.setErr(new PrintStream(errContent));
		

		logicGold.creditController.onPayWithCard();
		
        assertTrue(errContent.toString().contains("Please add items to pay for"));		
        System.setErr(originalErr);
			
		
	}
	
	/**
	 * Tests that customer can pay for item with swipe
	 * @throws IOException
	 */
	@Test
	public void scanAndPaywithSwipe() throws IOException {
		//add item from catalogue for testing 

		AddItemCatalogue.AddItemFromCatalogue(logicBronze.session,
				beer.barcodedProduct, beer.bigDecimalMass);
		System.out.println("Cart Total: "+logicBronze.session.getCartTotal());
		//bag item
		logicBronze.station.getBaggingArea().enable();
		logicBronze.station.getBaggingArea().addAnItem(beer.barcodedItem);
		
		
		
		try {
			logicBronze.station.getPrinter().addPaper(500); //add paper and ink
			logicBronze.station.getPrinter().addInk(1000);
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		logicBronze.creditController.onPayWithCard();
		otherCreditCard.swipe();// swipe card

		int i = 0;
		for (i =0; i<100; i++) {
			try {
		logicBronze.station.getCardReader().swipe(otherCreditCard); // try swipe
		assertTrue(logicBronze.creditController.getSuccess());//if its successful, break
		break;
		
			}
		catch(MagneticStripeFailureException e) {
		}
			
		catch (AssertionError e) {
	    // Log the assertion failure, but continue with the loop
			//System.out.println("Assertion failed, retrying...");
			}
		}
		//System.out.println(i);
		assertTrue("Swipe was unsuccessful", i<100); // if the loop finishes executing without breaking, transaction was unsuccessful
	}
	@Test
	public void scanAndPaywithTap() throws IOException {
		//add item from catalogue for testing 

		AddItemCatalogue.AddItemFromCatalogue(logicBronze.session,
				beer.barcodedProduct, beer.bigDecimalMass);
		//bag item
		logicBronze.station.getBaggingArea().enable();
		logicBronze.station.getBaggingArea().addAnItem(beer.barcodedItem);
		
		
		
		try {
			logicBronze.station.getPrinter().addPaper(500); //add paper and ink
			logicBronze.station.getPrinter().addInk(1000);
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		logicBronze.creditController.onPayWithCard();	//enable card payment
		
		otherCreditCard.tap();//tap card
		
		int i= 0;
		for ( i =0; i<100; i++) {
			try {
		logicBronze.station.getCardReader().tap(otherCreditCard);
		assertTrue(logicBronze.creditController.getSuccess());
		break;
		}
		catch(ChipFailureException e) {
		}
		catch (AssertionError e) {
	     // Log the assertion failure, but continue with the loop
			System.out.println("Assertion failed, retrying...");
			}}	
		assertTrue("Tap was unsuccessful", i<100); // if the loop finishes executing without breaking, transaction was unsuccessful

		
	}
	
	@Test
	public void scanAndPaywithInsert() throws IOException {
		logicBronze.session.Cart.add(products.beanBarcodedProduct);
		//add item from catalogue for testing 
		AddItemCatalogue.AddItemFromCatalogue(logicBronze.session,
				beer.barcodedProduct, beer.bigDecimalMass);
		//bagItem
		logicBronze.station.getBaggingArea().enable();
		logicBronze.station.getBaggingArea().addAnItem(beer.barcodedItem);
		
		
		
		try {
			logicBronze.station.getPrinter().addPaper(500);
			logicBronze.station.getPrinter().addInk(1000);
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		logicBronze.creditController.onPayWithCard();
		otherCreditCard.insert("911");// insert
		int i= 0;
		for ( i =0; i<100; i++) {
			try {
		logicBronze.station.getCardReader().insert(otherCreditCard, "911");
		logicBronze.station.getCardReader().remove();
		assertTrue(logicBronze.creditController.getSuccess());
		break;
		
			}
		catch(ChipFailureException e) {
			logicBronze.station.getCardReader().remove();

		}
		catch (AssertionError e) {
            // Log the assertion failure, but continue with the loop
            System.out.println("Assertion failed, retrying...");
        }}
		assertTrue("Insert was unsuccessful", i<100); // if the loop finishes executing without breaking, transaction was unsuccessful

	}
	
	
	/**
	 * This tests the behaviour of the system when cardData is corrupted
	 * Here the card type is null
	 */
	@Test(expected = NullPointerSimulationException.class)
	public void corruptCardData1() {
		// use a stub to feed corrupt card data to system
		CardDataStub cardDataStub = new CardDataStub(null, "122222", "JJ");
		try {
		logicBronze.creditController.theDataFromACardHasBeenRead(cardDataStub);}
		catch (NullPointerSimulationException e) {
			assertEquals("Null is not a valid cardType.", e.getMessage());
			
			throw e;
		}
		
	}
	
	/**
	 * This tests the behaviour of the system when cardData is corrupted
	 * Here the card type is invalid
	 */
	@Test
	public void corruptCardData2() {
		
		String typeString = "test";
		System.setErr(new PrintStream(errContent));
		// use a stub to feed corrupt card data to system
		CardDataStub cardDataStub = new CardDataStub("test", "122222", "JJ");
		
		logicBronze.creditController.theDataFromACardHasBeenRead(cardDataStub);
		assertTrue(errContent.toString().contains("credit/debit card expected, type used is:"));		
      System.setErr(originalErr);
		
	}
	
	/**
	 * This tests the behaviour of the system when cardData is corrupted
	 * Here the card holder's name  is null
	 */
	
	@Test(expected = NullPointerSimulationException.class)
	public void corruptCardData3() {
		// use a stub to feed corrupt card data to system
		CardDataStub cardDataStub = new CardDataStub("credit", "122222", null);
		try {
		logicBronze.creditController.theDataFromACardHasBeenRead(cardDataStub);}
		catch (NullPointerSimulationException e) {
			System.out.println(e.getMessage());
			assertEquals("Null is not a valid cardHolder.", e.getMessage());
			
			throw e;
		}

	}
	
	/**
	 * This tests the behaviour of the system when cardData is corrupted
	 * Here the card number is null
	 */
	@Test(expected = NullPointerSimulationException.class)
	public void corruptCardData4() {
		// use a stub to feed corrupt card data to system
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
