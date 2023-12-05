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
import java.math.BigDecimal;
import java.util.ArrayList;

import com.thelocalmarketplace.hardware.Product;

/**
 * Will use this class to keep track of all details about a specific session
 * i.e Items in cart, price total, total weight of items.
 * The class will be instantiated in SelfCheckoutLogic.
 */

public class SessionController {
	
	private Boolean SessionStarted = false;
	private Boolean SessionDisabled = false;
	
	private BigDecimal CartTotal;
	private double CartWeight;
	private double BulkyWeight;
	private double BagWeight;
	private SelfCheckoutLogic logic;
	public String membership;
	public ArrayList<Product> Cart;
	public ArrayList<Product> BulkyItems;
	
	/**
	 * Links the session controller to the self checkout logic
	 * @param SCL the software logic instance that we need to link it to
	 */
	public SessionController(SelfCheckoutLogic SCL) {
		logic = SCL;
	}
	
	/**
	 * Simulates the start of a session and initializes cart value to 0 and weight to 0.
	 * Also generates an empty ArrayList to store cart products.
	 */
	public void start () {
		if (!SessionStarted) {
			SessionStarted = true;
			setCartTotal(new BigDecimal(0));
			setCartWeight(0);
			setBulkyWeight(0);
			setBagWeight(0);
			Cart = new ArrayList<Product>();
			BulkyItems = new ArrayList<Product>();
		} else {
			System.err.println("A session is already in progress");
		}
	}

	public Boolean isStarted() {
		return SessionStarted;
	}
	
	public BigDecimal getCartTotal() {
		return CartTotal;
	}
	
	public void setCartTotal(BigDecimal newValue) {
		CartTotal = newValue;
	}

	public double getCartWeight() {
		return CartWeight;
	}
	
	public double getBulkyWeight() {
		return BulkyWeight;
	}
	
	public double getBagWeight() {
		return BagWeight;
	}

	public void setCartWeight(double newWeight) {
		CartWeight = newWeight;
	}
	
	public void setBulkyWeight(double newWeight) {
		BulkyWeight = newWeight;
	}
	
	public void setBagWeight(double newWeight) {
		BagWeight = newWeight;
	}
	
	
	public void disable(){
		SessionDisabled = true;
	}
	
	public void enable(){
		SessionDisabled = false;
	}
	
	public Boolean isDisabled() {
		return SessionDisabled;
	}
	
}
