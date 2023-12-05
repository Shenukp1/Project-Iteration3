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
package payment;

import java.math.BigDecimal;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderListener;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import control.SessionController;
import item.PrintController;

public class PaymentCardController implements CardReaderListener{

	private BigDecimal cartTotal; 												// Total value of customer's cart
	private SessionController session;											// The current session Payment Controller is linked to
	private AbstractSelfCheckoutStation station;							 	// The SCO station this class listens to
	private CardIssuer bank; 													// Bank of credit card
	private long holdNumber;													// Hold number of transaction
	private boolean successfulTransaction = false;				 		 		// Tracks if the transaction is successful
	private boolean isSuccess= false;

	
	public PaymentCardController(SessionController c_session, AbstractSelfCheckoutStation sco, CardIssuer bank) {
		this.station = sco;
		this.session = c_session;
		this.bank = bank;
		System.out.println("Intializing card controller");
		if (bank == null) {
			System.out.println("Bank was null");
		}
		sco.getCardReader().register(this);
		
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
		station.getCardReader().enable();
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
			throw new NullPointerSimulationException("cardType");
		}
		else if(cardType != "credit" && cardType != "debit") {
			System.err.println("credit/debit card expected, type used is: " + cardType);
		}
		if(cardHolder == null) {
			throw new NullPointerSimulationException("cardHolder");
		}
		if(cardNumber == null) {
			throw new NullPointerSimulationException("cardNumber");
		}
		session.disable();
		holdNumber = signalHoldToBank(bank, cardNumber, cartTotal);

		if(holdNumber != -1) {
			//System.out.println(holdNumber);
			successfulTransaction = bank.postTransaction(cardNumber, holdNumber, cartTotal.doubleValue());
		}
		else
			new NullPointerSimulationException("hold number");


		if(successfulTransaction) {
			session.setCartTotal(BigDecimal.ZERO);	
			System.out.println("You paid: $" + cartTotal);
			PrintController.printReceipt(station, session);
			isSuccess = true;
			// CLEAN UP SESSION (LATER ITERATION)
		}
		else {
			System.err.println("There was an error with payment, please try agian.");
			isSuccess = false;
		}
		successfulTransaction = false;		// To reset to start state.
		session.enable();
	};
	
	public boolean getSuccess() {

		return isSuccess;
	}
	
	public BigDecimal getTotal() {
		return cartTotal;
	}

	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {};

	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {};

	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {};

	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}

	public void aCardHasBeenSwiped() {}

	public void aCardHasBeenInserted() {}

	public void theCardHasBeenRemoved() {}

	public void aCardHasBeenTapped() {}
}

