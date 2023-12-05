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
import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;

import control.SessionController;


/** This class implements the payment controller that will respond to the event 
 * where a customer wants to proceed to the payment page and when the customer
 * inserts coin in the coin slot.
 */

public class CoinController implements CoinValidatorObserver{
    private BigDecimal CartTotal; 						// Total value of customer's cart
    private BigDecimal NewCartTotal; 					// Remaining value of customer's cart
    private SessionController session;					// The current session Payment Controller is linked to
    private AbstractSelfCheckoutStation station;		// The SCO station this class listens to
    
    public CoinController(SessionController c_session, AbstractSelfCheckoutStation sco) {
    	session = c_session;
    	station = sco;
		sco.getCoinValidator().attach(this);
    }

    /**
     * This method will deal with the request for payment when customer makes it.
     * Potentially done via GUI Button as customer goes to payment screen. We enable
     * the coin insertion slot to allow customer to insert the coin.
     */
    
    public void onPayViaCoin() {
    	if (session == null || !session.isStarted()) {							// Check if a session is in progress
			System.err.println("Please start a session first");
			return;
		}
    	if (session.Cart.size() == 0) {											// Customer hasn't scanned any items yet
    		System.err.println("Please add items to pay for");
			return;
    	}
    	station.getCoinSlot().enable();
    }
    
    /**
     * Event triggered when a valid coin is detected by the coinValidator
     * Updates the cart total. Returns if customer isn't on the payment screen
     * yet. The physical return of the coin will be in later iteration perhaps.
     */
    
    public void validCoinDetected(CoinValidator validator, BigDecimal Coin) {
    	CartTotal = session.getCartTotal();										// Get customer total
    	NewCartTotal = CartTotal.subtract(Coin);								// Subtract the value of the coin
    	session.setCartTotal(NewCartTotal);										// Update customer total
    	
    	System.out.println("You paid: $" + Coin + ". Remaining: $" + NewCartTotal);

        ChangeController.checkAllItemPaid(station, session, NewCartTotal, "coin"); 		// Check if all items are paid for 
    }

    public void invalidCoinDetected(CoinValidator validator) {	
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
