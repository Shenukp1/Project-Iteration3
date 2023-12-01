package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import control.SelfCheckoutLogic;
import payment.CardController;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.Products;
import testingUtilities.Wallet;
@RunWith(Parameterized.class)
public class PayViaCreditTest implements CardPayment, DollarsAndCurrency {
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
	
	CardController tempCardClass;
	
	  CardIssuer temp; 
	
	/*
	 * products
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
public PayViaCreditTest(AbstractSelfCheckoutStation station) {
    this.station = station;
}
	@Before
	public void setUp() {
		  calendar.add(Calendar.DAY_OF_MONTH, 7);
		  calendar.add(Calendar.YEAR, 2024);
		  calendar.add(Calendar.MONTH, 02);

		 
	logic = SelfCheckoutLogic.installOn(station);

	 temp = new CardIssuer("TD trust", 12321);
		
		logic.cardIssuer= temp;
	  logic.cardIssuer = new CardIssuer("TD trust", 12321);
	 
		logic.cardIssuer.addCardData(otherCreditCard.number, otherCreditCard.cardholder,
				calendar,otherCreditCard.cvv , 1000);
		temp.addCardData(otherCreditCard.number, otherCreditCard.cardholder,
				calendar,otherCreditCard.cvv , 1000);
		
		tempCardClass= new CardController(logic.session, logic.station, temp );
		logic.creditController =tempCardClass;
	 logic.session.enable();

	}


 /*
  * there is a chance of a swipe failing
  */
	@Test
	public void testSwipe() throws IOException {
		
		logic.station.getCardReader().enable();
		logic.station.getCardReader().enable();
	
		for (int i =0; i<100; i++) {
			try {
		logic.station.getCardReader().swipe(otherCreditCard);
			}
		catch(MagneticStripeFailureException e) {
		}
			}
	}

	@Test(expected = Exception.class)
	public void testIOExceptionDuringCardSwipe() throws IOException {
	    	// Simulate an IOException during the card swipe process
	    	logic.station.getCardReader().enable();
	    	
	    	logic.station.getCardReader().swipe(null); // Assuming a null card causes an IOException
	}
	
	@Test
	public void scanAndPay() throws IOException {
		logic.session.Cart.add(products.beanBarcodedProduct);
		
		logic.station.getMainScanner().enable();
		
		logic.barcodeController.AddItemFromBarcode(logic.session, products.beanBarcode);
		logic.station.getMainScanner().scan(products.beanBarcodeItem);
	
		logic.station.getBaggingArea().enable();
		logic.station.getBaggingArea().addAnItem(products.beanBarcodeItem);
		
		
		
		try {
			logic.station.getPrinter().addPaper(500);
			logic.station.getPrinter().addInk(1000);
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		logic.creditController.onPayWithCard();
		logic.station.getCardReader().enable();
		otherCreditCard.swipe();
		for (int i =0; i<100; i++) {
			try {
		logic.station.getCardReader().swipe(otherCreditCard);
		
			}
		catch(MagneticStripeFailureException e) {
		}
			}
		
	}
	

}
