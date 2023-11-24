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

public class BarcodeController implements BarcodeScannerListener{
	
	private SessionController session;
	private AbstractSelfCheckoutStation station;
	
	public BarcodeController(SessionController c_session, AbstractSelfCheckoutStation sco) {
		station = sco;
		session = c_session;
		station.handheldScanner.register(this);
	}
	
	/**
	 * When a customer scans a barcode, 'aBarcodeHasBeenScanned' event is triggered.
	 * This method deals with that event by getting product price and weight and 
	 * adds it to the session details.
	 * @param barcodeScanner
	 * 			the barcode scanner that was used to scan the product
	 * @param scannedBarcode
	 * 			the barcode that was scanned from the product
	 */
	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode scannedBarcode) {
		if (session == null || !session.isStarted()) {								// Check if a session is in progress
			System.err.println("Please start a session first");
			return;
		}
		if (session.isDisabled()) {
			System.err.println("Please wait for the system to be enabled");			// Make sure customer interaction is enabled
			return;
		}
		
		session.disable();															// Block further customer interaction
		barcodeScanner.disable();													// Disable the scanner
																					// Get product from barcode in our database
		BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(scannedBarcode);
		
		if (product == null) {														// Product not found in our database
			System.err.println("Product not found");
			barcodeScanner.enable();
			session.enable();
			return;
		}
		
		double initialWeight = session.getCartWeight();								// Retrieve cart weight before adding item
		double ItemWeight = product.getExpectedWeight();							// Get expected weight of item
		
		BigDecimal CartTotal = session.getCartTotal();								// Retrieve cart total before adding item
		BigDecimal ItemPrice = new BigDecimal(product.getPrice());					// Get item price
		
		session.setCartWeight(initialWeight + ItemWeight);							// Update cart weight
		session.setCartTotal(CartTotal.add(ItemPrice));								// Update cart price
		session.Cart.add(product);													// Add product to our list of products
		
		// LATER ITERATION: Customer will be asked whether or not
		// they would like to bag the item. Call to handle bulky
		// item will be made accordingly.
		barcodeScanner.enable();													// Enable the scanner
	}
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}
}
