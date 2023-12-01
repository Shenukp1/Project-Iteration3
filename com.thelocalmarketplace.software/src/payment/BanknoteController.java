package payment;

import java.math.BigDecimal;
import java.util.Currency;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteInsertionSlot;
import com.tdc.banknote.BanknoteInsertionSlotObserver;
import com.tdc.banknote.BanknoteValidator;
import com.tdc.banknote.BanknoteValidatorObserver;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;

import control.SessionController;

public class BanknoteController implements BanknoteValidatorObserver{

	
/**
 * The customer has indicated that they want to pay with a banknote for their order.
 * Scenario:
1. Customer: Inserts a banknote in the System.
2. System: Reduces the remaining amount due by the value of the inserted banknote.
3. System: Signals to the Customer the updated amount due after the insertion of the banknote.
4. System: If the remaining amount due is greater than 0, go to 1.
5. System: If the remaining amount due is less than 0, dispense the amount of change due.
6. Once payment in full is made and change returned to the customer, see Print Receipt.

Exceptions:
1. If the Customer inserts a banknote that is deemed unacceptable, this will be returned to the
customer without involving the System, presumably handled in hardware.

2. If insufficient change is available, the Attendant should be signaled as to the change still due to the
Customer and the station should be suspended so that maintenance can be conducted on it.
 */
	private BigDecimal CartTotal; 						// Total value of customer's cart
    private BigDecimal NewCartTotal; 					// Remaining value of customer's cart
	private SessionController session;					// The current session Payment Controller is linked to
	private AbstractSelfCheckoutStation station;		// The SCO station this class listens to
	
	public BanknoteController(SessionController c_session, AbstractSelfCheckoutStation sco) {
		station = sco;
		session = c_session;
		sco.getBanknoteValidator().attach(this);
	}
	
	/**
     * This method will deal with the request for payment when customer makes it.
     * Potentially done via GUI Button as customer goes to payment screen. We enable
     * the banknote insertion slot to allow customer to insert the banknote.
     */
    
    public void onPayViaBanknote() {
    	if (session == null || !session.isStarted()) {							// Check if a session is in progress
			System.err.println("Please start a session first");
			return;
		}
    	if (session.Cart.size() == 0) {											// Customer hasn't scanned any items yet
    		System.err.println("Please add items to pay for");
			return;
    	}
    	station.getBanknoteInput().enable();
    }
    
	/**
	 * Event triggered when BanknoteValidator detects a valid bank note inserted.
	 * @param validator
	 * 			the BanknoteValidator object which validated the banknote
	 * @param currency
	 * 			the currency of the bank note inserted
	 * @param denomination
	 * 			the denomination of the bank note inserted 
	 */
	@Override
	public void goodBanknote(BanknoteValidator validator, Currency currency, BigDecimal denomination) {
		CartTotal = session.getCartTotal();										// Get customer total
    	NewCartTotal = CartTotal.subtract(denomination);						// Subtract the value of the banknote
    	session.setCartTotal(NewCartTotal);										// Update customer total
    	System.out.println("You paid: $" + denomination + ". Remaining: $" + NewCartTotal);
    	    	
    	ChangeController.checkAllItemPaid(station, session, NewCartTotal, "banknote"); 	// Check if all items are paid for
	}
	
	@Override
	public void badBanknote(BanknoteValidator validator) {
		System.err.println("The cash you entered is invalid. Please try again.");
	}
	
	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {}

	
	
}
