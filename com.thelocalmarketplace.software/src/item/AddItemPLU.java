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
