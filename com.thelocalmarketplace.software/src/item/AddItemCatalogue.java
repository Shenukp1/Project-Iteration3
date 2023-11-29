package item;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import control.SessionController;

public class AddItemCatalogue {
	
	/**
	 * This method allows GUI to get a catalogue of products to display to the customer
	 * @return an ArrayList of products
	 */
	protected static ArrayList<Product> getCatalogue() {
		ArrayList<Product> result = new ArrayList<>();
		// Add all our BarcodedProducts
		for (BarcodedProduct p : ProductDatabases.BARCODED_PRODUCT_DATABASE.values()) {
			result.add(p);
		}
		// Add all our PLUCodedProducts
		for (PLUCodedProduct p : ProductDatabases.PLU_PRODUCT_DATABASE.values()) {
			result.add(p);
		}
		// Return the result
		return result;
	}
	
	/**
	 * 
	 * @param session The session to add the product to
	 * @param product The product we want to add
	 * @param productWeight If product is not sold per unit, it's weight. 0 otherwise.
	 * @return Success/Failure message
	 */
	public static String AddItemFromCatalogue(SessionController session, Product product, BigDecimal productWeight) {
		if (ProductDatabases.INVENTORY.get(product) <= 0){
			return "Error: Product not available";
		}
		
		BigDecimal CartTotal = session.getCartTotal();								// Retrieve cart total before adding item
		BigDecimal ItemPrice = new BigDecimal(product.getPrice());					// Get item price
		
		double initialWeight = session.getCartWeight();								// Retrieve cart weight before adding item
		double ItemWeight = 0;
		if (product instanceof BarcodedProduct) {
			ItemWeight = ((BarcodedProduct) product).getExpectedWeight();			// Get expected weight of item
		}
		
		if (!product.isPerUnit()) {
			ItemWeight = ItemPrice.multiply(productWeight).doubleValue();			// Get expected weight of item
		}
		
		session.setCartWeight(initialWeight + ItemWeight);							// Update cart weight
		session.setCartTotal(CartTotal.add(ItemPrice));								// Update cart price
		session.Cart.add(product);													// Add product to our list of products
		return "Success: Product added to cart";
	}
}
