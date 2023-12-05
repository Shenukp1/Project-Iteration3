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
