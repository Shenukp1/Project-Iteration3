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
