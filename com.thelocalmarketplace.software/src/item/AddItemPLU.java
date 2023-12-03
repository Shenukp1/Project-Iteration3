package item;

import java.math.BigDecimal;

import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import control.SessionController;

public class AddItemPLU {
	/**
	 * Implementation of the use case where a customer wants to add item to
	 * their cart by a PLU code. We search the PLU Coded product database
	 * and add the item accordingly.
	 */
	public static String AddItemFromPLU(SessionController session, PriceLookUpCode PLUtoAdd, BigDecimal productWeight) {
		// Find product in database
		PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(PLUtoAdd);
		
		// If product doesn't exist in database
		if (product == null) {
			return "Error: Product not found";
		}
		
		// If product isn't in stock
		if (ProductDatabases.INVENTORY.get(product) <= 0){
			return "Error: Product not available";
		}
		
		// Retrieve cart total before adding item
		BigDecimal CartTotal = session.getCartTotal();
		// Get item price
		BigDecimal ItemPrice = new BigDecimal(product.getPrice());
		
		// Retrieve cart weight before adding item
		double initialWeight = session.getCartWeight();
		// Get expected weight of item
		double ItemWeight = ItemPrice.multiply(productWeight).doubleValue();
		
		
		session.setCartWeight(initialWeight + ItemWeight);							// Update cart weight
		session.setCartTotal(CartTotal.add(ItemPrice));								// Update cart price
		session.Cart.add(product);													// Add product to our list of products
		return "Success: Product added to cart";
	}
}
