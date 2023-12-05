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
	
	public static String AddItemFromBarcode(SessionController session, Barcode scannedBarcode) {
		// Get product from barcode in our database
		BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(scannedBarcode);
		
		if (product == null) {														// Product not found in our database
			session.enable();
			return "Error: Product not found";
		}
		int quantity = ProductDatabases.INVENTORY.get(product);
		if (quantity <= 0){
			return "Error: Product not available";
		}
		
		double initialWeight = session.getCartWeight();								// Retrieve cart weight before adding item
		double ItemWeight = product.getExpectedWeight();							// Get expected weight of item
		
		BigDecimal CartTotal = session.getCartTotal();								// Retrieve cart total before adding item
		BigDecimal ItemPrice = new BigDecimal(product.getPrice());					// Get item price
		
		session.setCartWeight(initialWeight + ItemWeight);							// Update cart weight
		session.setCartTotal(CartTotal.add(ItemPrice));								// Update cart price
		session.Cart.add(product);													// Add product to our list of products
		ProductDatabases.INVENTORY.put(product, quantity-1);
		return "Success: Product added to cart";
	}
}
