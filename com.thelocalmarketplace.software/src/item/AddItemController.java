package item;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;

import control.SessionController;

public class AddItemController implements BarcodeScannerListener{
 /**
 * Abstract class for all use cases that add an item to customer's cart
 */
	private SessionController session;
	private AbstractSelfCheckoutStation station;
	
	public AddItemController(SessionController session, AbstractSelfCheckoutStation station) {
		this.station = station;
		this.session = session;
		// Register this controller as a listener to the barcode scanner
		station.getHandheldScanner().register(this);
		station.getMainScanner().register(this);
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
		// Check if a session is in progress
		if (session == null || !session.isStarted()) {
			System.err.println("Please start a session first");
			return;
		}
		// Make sure customer interaction is enabled
		if (session.isDisabled()) {
			System.err.println("Please wait for the system to be enabled");			
			return;
		}
		session.disable();
//		barcodeScanner.disable();

		// This message (potentially) will be displayed to the customer on GUI
		String message = AddItemBarcode.AddItemFromBarcode(session, scannedBarcode);
		
		session.enable();
		/** Customer should be asked whether or not
		 they would like to bag the item. Call to handle bulky
		 item will be made accordingly. **/
		
		// Enable the scanner
//		barcodeScanner.enable();
	}
	
	/**
	 * GUI will call this method when customer wants to item by a text search.
	 * @param textToSearch the text we want to search for
	 */
	public BarcodedProduct textSearch(String textToSearch, BigDecimal productWeight) {
		// This message (potentially) will be displayed to the customer on GUI
		BarcodedProduct message = AddItemText.AddItemFromText(session, textToSearch, productWeight);
		return message;
	}
	
	/**
	 * GUI will call this method when customer wants to item by a text search.
	 * @param PLUtoAdd the PLU code we want to add
	 * @param productWeight the weight customer has on bagging area for that item
	 */
	public void pluSearch(PriceLookUpCode PLUtoAdd, BigDecimal productWeight) {
		session.disable();
		// This message (potentially) will be displayed to the customer on GUI
		String message = AddItemPLU.AddItemFromPLU(session, PLUtoAdd, productWeight);
		session.enable();
	}
	
	/**
	 * GUI will call this method when a customer has selected a product to add
	 * from the catalogue they were provided earlier. 
	 * @param product that has to be added to cart
	 * @param productWeight the weight customer has on bagging area for that item
	 */
	public void catalogueAdd(Product product, BigDecimal productWeight) {
		session.disable();
		// This message (potentially) will be displayed to the customer on GUI
		String message = AddItemCatalogue.AddItemFromCatalogue(session, product, productWeight);
		session.enable();
	}
	
	/**
	 * GUI will call this method when customer wants to view catalogue and add
	 * item from it. 
	 * @return ArrayList of Products containing all our products
	 */
	public ArrayList<Product> getProductCatalogue() {
		return AddItemCatalogue.getCatalogue();
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
}
