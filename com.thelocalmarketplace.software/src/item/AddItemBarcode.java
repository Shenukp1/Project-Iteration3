package item;

import java.math.BigDecimal;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import control.SelfCheckoutLogic;
import control.SessionController;

/**
 * This class will be used to listen to events triggered by the Handheld BarcodeScanner.
 * It will then add appropriate item details to customer's session.
 */

public class AddItemBarcode {
	
	// private SessionController session;
	// private AbstractSelfCheckoutStation station;
	
	
	public static String AddItemFromBarcode(SessionController session, Barcode scannedBarcode) {
		// Get product from barcode in our database
		BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(scannedBarcode);
		
		if (product == null) {														// Product not found in our database
			session.enable();
			return "Error: Product not found";
		}
		if (ProductDatabases.INVENTORY.get(product) <= 0){
			return "Error: Product not available";
		}
		
		double initialWeight = session.getCartWeight();								// Retrieve cart weight before adding item
		double ItemWeight = product.getExpectedWeight();							// Get expected weight of item
		
		BigDecimal CartTotal = session.getCartTotal();								// Retrieve cart total before adding item
		BigDecimal ItemPrice = new BigDecimal(product.getPrice());					// Get item price
		
		session.setCartWeight(initialWeight + ItemWeight);							// Update cart weight
		session.setCartTotal(CartTotal.add(ItemPrice));								// Update cart price
		session.Cart.add(product);													// Add product to our list of products
		return "Success: Product added to cart";
	}
	
	
	// ** WILL BE REMOVED ** //
	public AddItemBarcode(SessionController c_session, AbstractSelfCheckoutStation sco) {
		//station = sco;
		//session = c_session;
	}
}
