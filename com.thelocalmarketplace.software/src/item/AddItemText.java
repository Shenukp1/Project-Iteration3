package item;

import java.math.BigDecimal;

import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import control.SessionController;

public class AddItemText {

/**
 * Implementation of the use case where a customer wants to add item to
 * their cart by doing a text search. This class receives the text and 
 * searches through product catalogue.
 */
	
	/**
	 * 
	 * @param session The session to which we want to add the item
	 * @param textToSearch The text we are looking to match
	 * @param productWeight If it's not a per unit product, provide the weight of
	 * 						item in bagging area.
	 * @return success/failure message
	 */
	public static BarcodedProduct AddItemFromText(SessionController session, String textToSearch, BigDecimal productWeight) {
		BarcodedProduct product = null;
		for (BarcodedProduct bp : ProductDatabases.BARCODED_PRODUCT_DATABASE.values()) {
			if (bp.getDescription().contains(textToSearch)) {
				product = bp;
			}
		}
		// If product wasn't found in barcoded product database
		/*if (product == null) {
			for (PLUCodedProduct pp : ProductDatabases.PLU_PRODUCT_DATABASE.values()) {
				if (pp.getDescription().contains(textToSearch)) {
					product = pp;
				}
			}
		}*/
		
		// Product doesn't exist in the database
		if (product == null) {
			return null;
		}
		
		// Product is out of stock
		if (ProductDatabases.INVENTORY.get(product) <= 0){
			return null;
		}
		
		BigDecimal CartTotal = session.getCartTotal();								// Retrieve cart total before adding item
		BigDecimal ItemPrice = new BigDecimal(product.getPrice());					// Get item price
		
		double initialWeight = session.getCartWeight();								// Retrieve cart weight before adding item
		double ItemWeight = 0;
		if (product instanceof BarcodedProduct) {
			ItemWeight = ((BarcodedProduct) product).getExpectedWeight();			// Get expected weight of item
		}
		
		if (!product.isPerUnit()) {
//			ItemWeight = ItemPrice.multiply(productWeight).doubleValue();			// Get expected weight of item
		}
		
		session.setCartWeight(initialWeight + ItemWeight);							// Update cart weight
		session.setCartTotal(CartTotal.add(ItemPrice));								// Update cart price
		session.Cart.add(product);
		return product;
	}
}
