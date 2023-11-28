package payment;

import java.math.BigDecimal;
import java.util.Scanner;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderListener;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import control.SessionController;
import item.PrintController;

public class CardController implements CardReaderListener{

	private BigDecimal cartTotal; 												// Total value of customer's cart
	private SessionController session;											// The current session Payment Controller is linked to
	private AbstractSelfCheckoutStation station;							 	// The SCO station this class listens to
	private CardIssuer bank; 													// Bank of credit card
	private long holdNumber;													// Hold number of transaction
	private boolean successfulTransaction = false;				 		 		// Tracks if the transaction is successful

	
	public CardController(SessionController c_session, AbstractSelfCheckoutStation sco, CardIssuer bank) {
		this.station = sco;
		this.session = c_session;
		this.bank = bank;
		sco.cardReader.register(this);
		
	}
	
	/**
	 * This method will deal with the request for payment when customer makes it.
	 * Potentially done via GUI Button as customer goes to payment screen. We enable
	 * the card reader to allow customer to interact with card reader.
	 */
    
	public void onPayWithCard() {
		if (session == null || !session.isStarted()) {							// Check if a session is in progress
			System.err.println("Please start a session first");
			return;
	}
		if (session.Cart.size() == 0) {											// Customer hasn't scanned any items yet
			System.err.println("Please add items to pay for");
			return;
		}
		station.cardReader.enable();
	}
    

	/**
	 * Signals to bank the details of the credit card and the amount to be charged.
	 */

	public long signalHoldToBank(CardIssuer bank, String cardNumber, BigDecimal amountCharged) {
		double amount = amountCharged.doubleValue();
		long holdNumber = bank.authorizeHold(cardNumber, amount);
		return holdNumber;
  }

	/**
	 * Listens to the event where the data has been read from a card.
	 * 
	 * @param data
	 * The data that was read. Note that this data may be corrupted.
	 */

	@Override
	public void theDataFromACardHasBeenRead(CardData data) throws NullPointerSimulationException{
		String cardType = data.getType(); 						// Get card type
		String cardHolder = data.getCardholder(); 				// Get name on card
		String cardNumber = data.getNumber(); 					// Get card number
		cartTotal = session.getCartTotal();						// Get customer total

		if(cardType == null) {
			new NullPointerSimulationException("cardType");
		}
		else if(cardType != "credit" && cardType != "debit") {
			System.err.println("credit/debit card expected, type used is: " + cardType);
		}
		if(cardHolder == null) {
			new NullPointerSimulationException("cardHolder");
		}
		if(cardNumber == null) {
			new NullPointerSimulationException("cardNumber");
		}
		session.disable();
		holdNumber = signalHoldToBank(bank, cardNumber, cartTotal);

		if(holdNumber != -1) {
			successfulTransaction = bank.postTransaction(cardNumber, holdNumber, cartTotal.doubleValue());
		}
		else
			new NullPointerSimulationException("hold number");
		

		if(successfulTransaction) {
			session.setCartTotal(BigDecimal.ZERO);	
			System.out.println("You paid: $" + cartTotal);
			PrintController.printReceipt(station, session);
			// CLEAN UP SESSION (LATER ITERATION)
		}
		else {
			System.err.println("There was an error with payment, please try agian.");
		}
		successfulTransaction = false;		// To reset to start state.
		session.enable();
	};

	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {};

	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {};

	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {};

	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}

	public void aCardHasBeenSwiped() {}

	@Override
	public void aCardHasBeenInserted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theCardHasBeenRemoved() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aCardHasBeenTapped() {
		// TODO Auto-generated method stub
		
	};
}

