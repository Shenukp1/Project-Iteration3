package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.card.ChipFailureException;
import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import control.SelfCheckoutLogic;
import control.SessionController;
import item.AddItemBarcode;
import item.AddItemCatalogue;
import payment.PaymentCardController;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.LoadProductDatabases;
import testingUtilities.Products;
import testingUtilities.Wallet;
@RunWith(Parameterized.class)
public class PaymentCardControllerTest implements CardPayment, DollarsAndCurrency, LoadProductDatabases {
	/*
	 * make three types of station to test, we'll have to test all three types for each kind of test.
	 * Maybe there's a fast way to plug in these objects than repeating a test method?
	 */
	static SelfCheckoutStationBronze bronze;
	static SelfCheckoutStationSilver silver;
	static SelfCheckoutStationGold gold;
	public AbstractSelfCheckoutStation station;
	/*
	 * each station needs its own logic instance for setup.
	 */
	SelfCheckoutLogic logic;

/*
 * we need a calendar to use cardIssuer
 */
	Calendar calendar = Calendar.getInstance();
	CardIssuer temp; 
	PaymentCardController tempCardClass;
	/*
	 * products
	 */
	Products products = new Products();
	
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    
@Parameterized.Parameters
public static Collection<AbstractSelfCheckoutStation[]> data() {
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
	
    return Arrays.asList(new AbstractSelfCheckoutStation[][] {
            { bronze },
            { silver  },
            { gold  },
            // Add more instances as needed
        });
}
public PaymentCardControllerTest(AbstractSelfCheckoutStation station) {
    this.station = station;
}
	@Before
	public void setUp() {
		  calendar.add(Calendar.DAY_OF_MONTH, 7);
		  calendar.add(Calendar.YEAR, 2024);
		  calendar.add(Calendar.MONTH, 02);

		 
		  logic = SelfCheckoutLogic.installOn(station);

		  temp = new CardIssuer("TD trust", 12321);
		  logic.session.enable();

		
		
		  logic.cardIssuer = new CardIssuer("TD trust", 12321);
	 
		  logic.cardIssuer.addCardData(otherCreditCard.number, otherCreditCard.cardholder,
				calendar,otherCreditCard.cvv , 1000);
		  temp.addCardData(otherCreditCard.number, otherCreditCard.cardholder,
				calendar,otherCreditCard.cvv , 1000);
		  logic.creditController = new PaymentCardController(logic.session, logic.station, temp );
		  logic.session.Cart.clear();

	}
	/**
	 * Tests swipe 100 times since there is a chance of swipe failing 
	 * @throws IOException
	 */
	@Test
	public void testSwipe() throws IOException {
		//enable card reader
		logic.station.getCardReader().enable();
	
		for (int i =0; i<100; i++) {
			try {
		logic.station.getCardReader().swipe(otherCreditCard);
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
		logic.station.getCardReader().enable();
	
		for (int i =0; i<100; i++) {
			try {
		logic.station.getCardReader().tap(otherCreditCard);
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
		logic.station.getCardReader().enable();
	
		for (int i =0; i<100; i++) {
			try {
		logic.station.getCardReader().insert(otherCreditCard, "911");
		logic.station.getCardReader().remove();
			}
		catch(ChipFailureException e) {
			logic.station.getCardReader().remove();// remove card even after chip failure
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
        
		AbstractSelfCheckoutStation stationTest;
		if( station instanceof SelfCheckoutStationBronze) {
		stationTest = new SelfCheckoutStationBronze();}
		else if (station instanceof SelfCheckoutStationSilver) {
			stationTest = new SelfCheckoutStationSilver();
			
		}
		else {
			stationTest = new SelfCheckoutStationGold();
		}
		
        
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		stationTest.plugIn(PowerGrid.instance());
		stationTest.turnOn();		
		SelfCheckoutLogic testLogic = new SelfCheckoutLogic(stationTest);
		SelfCheckoutLogic.installOn(stationTest);

		PaymentCardController tempCardClass2 = new PaymentCardController(null, testLogic.station, temp );
		logic.creditController =tempCardClass2;
		logic.creditController.onPayWithCard();
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
		

		logic.creditController.onPayWithCard();
		
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

		AddItemCatalogue.AddItemFromCatalogue(logic.session,
				beer.barcodedProduct, beer.bigDecimalMass);
		System.out.println("Cart Total: "+logic.session.getCartTotal());
		//bag item
		logic.station.getBaggingArea().enable();
		logic.station.getBaggingArea().addAnItem(beer.barcodedItem);
		
		
		
		try {
			logic.station.getPrinter().addPaper(500); //add paper and ink
			logic.station.getPrinter().addInk(1000);
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		logic.creditController.onPayWithCard();
		otherCreditCard.swipe();// swipe card

		int i = 0;
		for (i =0; i<100; i++) {
			try {
		logic.station.getCardReader().swipe(otherCreditCard); // try swipe
		assertTrue(logic.creditController.getSuccess());//if its successful, break
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

		AddItemCatalogue.AddItemFromCatalogue(logic.session,
				beer.barcodedProduct, beer.bigDecimalMass);
		//bag item
		logic.station.getBaggingArea().enable();
		
		
		
		try {
			logic.station.getPrinter().addPaper(500); //add paper and ink
			logic.station.getPrinter().addInk(1000);
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		logic.creditController.onPayWithCard();	//enable card payment
		
		otherCreditCard.tap();//tap card
		
		int i= 0;
		for ( i =0; i<100; i++) {
			try {
		logic.station.getCardReader().tap(otherCreditCard);
		assertTrue(logic.creditController.getSuccess());
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
		logic.session.Cart.add(products.beanBarcodedProduct);
		//add item from catalogue for testing 
		AddItemCatalogue.AddItemFromCatalogue(logic.session,
				beer.barcodedProduct, beer.bigDecimalMass);
		//bagItem
		logic.station.getBaggingArea().enable();
		
		
		
		try {
			logic.station.getPrinter().addPaper(500);
			logic.station.getPrinter().addInk(1000);
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		logic.creditController.onPayWithCard();
		otherCreditCard.insert("911");// insert
		int i= 0;
		for ( i =0; i<100; i++) {
			try {
		logic.station.getCardReader().insert(otherCreditCard, "911");
		logic.station.getCardReader().remove();
		assertTrue(logic.creditController.getSuccess());
		break;
		
			}
		catch(ChipFailureException e) {
			logic.station.getCardReader().remove();

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
		logic.creditController.theDataFromACardHasBeenRead(cardDataStub);}
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
		
		logic.creditController.theDataFromACardHasBeenRead(cardDataStub);
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
		logic.creditController.theDataFromACardHasBeenRead(cardDataStub);}
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
		logic.creditController.theDataFromACardHasBeenRead(cardDataStub);}
		catch (NullPointerSimulationException e) {
			System.out.println(e.getMessage());
			assertEquals("Null is not a valid cardNumber.", e.getMessage());
			
			throw e;
		}}
	@After
	public void tearDown() throws Exception {
		try {
			logic.station.getCardReader().remove();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
