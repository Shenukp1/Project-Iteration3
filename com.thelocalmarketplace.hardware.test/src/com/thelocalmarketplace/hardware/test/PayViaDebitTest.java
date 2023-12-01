package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
import powerutility.NoPowerException;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.Products;
import testingUtilities.Wallet;

@RunWith(Parameterized.class)
public class PayViaDebitTest implements CardPayment, DollarsAndCurrency {

	Products products = new Products();
	Calendar calendar = Calendar.getInstance();
	CardController tempCardClass;
	CardIssuer temp;

	/*
	 * make three types of station to test, we'll have to test all three types for
	 * each kind of test. Maybe there's a fast way to plug in these objects than
	 * repeating a test method?
	 */
	static SelfCheckoutStationBronze bronze;
	static SelfCheckoutStationSilver silver;
	static SelfCheckoutStationGold gold;
	public AbstractSelfCheckoutStation station;
	/*
	 * each station needs its own logic instance for setup.
	 */
	SelfCheckoutLogic logic;

	@Rule
	public ExpectedException expectedMessage = ExpectedException.none();

	@Parameterized.Parameters
	public static Collection<AbstractSelfCheckoutStation[]> data() {
		// bronze
		bronze.resetConfigurationToDefaults();
		bronze.configureBanknoteDenominations(bankNoteDenominations);

		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationBronze bronze = new SelfCheckoutStationBronze();
		bronze.plugIn(PowerGrid.instance());
		bronze.turnOn();

		// silver
		silver.resetConfigurationToDefaults();
		silver.configureBanknoteDenominations(bankNoteDenominations);

		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationSilver silver = new SelfCheckoutStationSilver();
		silver.plugIn(PowerGrid.instance());
		silver.turnOn();

		// gold
		gold.resetConfigurationToDefaults();
		gold.configureBanknoteDenominations(bankNoteDenominations);

		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationGold gold = new SelfCheckoutStationGold();
		gold.plugIn(PowerGrid.instance());
		gold.turnOn();

		return Arrays.asList(new AbstractSelfCheckoutStation[][] { { bronze }, { silver }, { gold },
				// Add more instances as needed
		});
	}

	public PayViaDebitTest(AbstractSelfCheckoutStation station) {
		this.station = station;
	}

	@Before
	public void setup() {

		logic = SelfCheckoutLogic.installOn(station);

		temp = new CardIssuer("TD trust", 12321);

		logic.cardIssuer = temp;
		logic.cardIssuer = new CardIssuer("TD trust", 12321);

		calendar.add(Calendar.DAY_OF_MONTH, 5);
		calendar.add(Calendar.YEAR, 2025);
		calendar.add(Calendar.MONTH, 10);

		temp = new CardIssuer("CIBC", 12321);
		temp.addCardData(otherDebitCard.number, otherDebitCard.cardholder, calendar, otherDebitCard.cvv, 1000);

		tempCardClass = new CardController(logic.session, logic.station, temp);
		logic.creditController = tempCardClass;

		logic.session.enable();
	}

	@Test
	public void testSwipeDebit() throws IOException {
		logic.station.getCardReader().enable();
		for (int i = 0; i < 100; i++) {
			try {
				logic.station.getCardReader().swipe(otherDebitCard);
			} catch (MagneticStripeFailureException e) {
			}
		}
	}

	@Test
	public void testScanningAddingAndDebitCardPayment() throws IOException {
		// Simulate scanning and adding items to the cart
		logic.station.getMainScanner().enable();
		logic.barcodeController.AddItemFromBarcode(logic.session, products.beanBarcode );
		logic.station.getMainScanner().scan(products.beanBarcodeItem);
		logic.station.getScanningArea().enable();
		logic.station.getScanningArea().addAnItem(products.beanBarcodeItem);

		// Simulate the debit card payment process
		CardIssuer debitCardIssuer = PayViaDebitTest.debitcard;
		long holdNumber = debitCardIssuer.authorizeHold("1234567890123456", 100.0);

		assertNotNull("Hold number should be generated", holdNumber);

		// The payment process should be successful
		boolean successfulTransaction = debitCardIssuer.postTransaction("1234567890123456", holdNumber, 100.0);
		assertTrue("Transaction is successful", successfulTransaction);

		// Simulate the card swipe process
		tempCardClass = new CardController(logic.session, logic.station, temp);
		logic.creditController = tempCardClass;
		logic.creditController.onPayWithCard();

		logic.station.getCardReader().enable();
		otherDebitCard.swipe();

		for (int i = 0; i < 100; i++) {
			try {
				logic.station.getCardReader().swipe(otherDebitCard);
			} catch (MagneticStripeFailureException e) {
				// Handle the exception if needed
			}
		}
	}

	@Test
	public void testInsufficientFundsDebitCardPayment() throws IOException {
		// Simulate scanning and adding items to the cart
		logic.station.getMainScanner().enable();
		logic.barcodeController.AddItemFromBarcode(logic.session, products.beanBarcode );
		logic.station.getMainScanner().scan(products.beanBarcodeItem);
		logic.station.getScanningArea().enable();
		logic.station.getScanningArea().addAnItem(products.beanBarcodeItem);

		// Simulate the debit card payment process
		CardIssuer debitCardIssuer = PayViaDebitTest.debitcard;
		long holdNumber = debitCardIssuer.authorizeHold("1234567890123456", 100.0);

		assertNotNull("Hold number should be generated", holdNumber);

		boolean successfulTransaction = debitCardIssuer.postTransaction("1234567890123456", holdNumber, 100.0);
		assertTrue("Transaction is successful", successfulTransaction);

		// Simulate the card swipe process
		tempCardClass = new CardController(logic.session, logic.station, temp);
		logic.creditController = tempCardClass;
		logic.creditController.onPayWithCard();

		logic.station.getCardReader().enable();
		otherDebitCard.swipe();

		for (int i = 0; i < 100; i++) {
			try {
				logic.station.getCardReader().swipe(otherDebitCard);
			} catch (MagneticStripeFailureException e) {
				// Handle the exception if needed
			}
		}
	}

	@Test(expected = NullPointerException.class)
	public void testIOExceptionDuringCardSwipe() throws IOException {
		// Simulate an IOException during the card swipe process
		logic.station.getCardReader().enable();
		logic.station.getCardReader().swipe(null); // Assuming a null card causes an IOException
	}
}
