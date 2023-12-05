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
package control;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import payment.CoinController;
import payment.PaymentCardController;
import payment.BanknoteController;
import item.AddItemBarcode;
import item.AddItemController;
import item.EnterMembership;
import item.PrintController;
import item.RemoveItemController;

/**
 * This class links all the controllers to an instance of the SelfCheckoutStations.
 * All the controllers are registered to the hardware of the station as listeners
 * and respond to events accordingly.
 */

public class SelfCheckoutLogic {
	
	// Software simulation of SelfCheckoutStation hardware
	public AbstractSelfCheckoutStation station;
	
	// Controller to manage payments
	public CoinController coinController;
	
	// Controller to manage banknote payments
	public BanknoteController banknoteController;
	
	// Controller to manage credit card payments
	public PaymentCardController creditController;
	
	// Controller to manage add item cases
	public AddItemController addItemController;
	
	// Controller to manage remove item cases
	public RemoveItemController removeItemController;
		
	// Controller to manage session
	public SessionController session;
	
	// Controller to manage scale weight
	public WeightController weightController;
	
	// Controller to manage printing
	public PrintController printController;
	private int linesUsed = 0;
	
	// Controller to manage predicting issues
	public PredictIssueController predictController;
	
	//	Card Issuer
	public CardIssuer cardIssuer;
	
	// Controller for input membership number
	public EnterMembership	enterMembership;
	
	/**
	 * This method links our software to our hardware (simulation) and initializes 
	 * all the controllers that we need.
	 * @param scs The AbstractSelfCheckoutStation hardware we need to link it to 
	 * (currently simulated when a customer presses "Start Session")
	 * @return the instance of the software logic
	 */
	
	public static SelfCheckoutLogic installOn(AbstractSelfCheckoutStation scs) {
		return new SelfCheckoutLogic(scs);
	}
	
	/**
	 * Every customer session will have one logic instance.
	 * @param scs The SelfCheckoutStation hardware we need to link it to 
	 * (currently simulated when a customer presses "Start Session")
	 */
	public SelfCheckoutLogic(AbstractSelfCheckoutStation scs) {
		station = scs;
		session = new SessionController(this);
		session.start();
		
		addItemController = new AddItemController(session, scs);
		removeItemController = new RemoveItemController(session, scs);
		weightController = new WeightController(session, scs);
		banknoteController = new BanknoteController(session, scs);
		coinController = new CoinController(session, scs);
	//	creditController = new CardController(session, scs, cardIssuer);
		printController = new PrintController(session, scs);
		linesUsed += printController.getLinesUsed();
		predictController = new PredictIssueController(session, scs, linesUsed);
		enterMembership = new EnterMembership(session,scs);
		
		// Disable banknote insertion slot so customer does not insert banknotes
		// before going to the payment page.
		scs.getBanknoteInput().disable();
		// Same for coin insertion slot
		scs.getCoinSlot().disable();
		// Same for debit/credit card reader
		scs.getCardReader().disable();
		
	}
}
