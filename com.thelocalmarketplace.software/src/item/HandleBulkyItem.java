package item;

import com.thelocalmarketplace.hardware.BarcodedProduct;
import control.SessionController;

public class HandleBulkyItem {
	
	// Simulates customer choosing to not bag an item, accessible through a UI later iteration
	public static void doNotBagItem(SessionController session, BarcodedProduct product) {
		session.disable();
		// Checks cart is not empty
		if (!session.Cart.isEmpty()) {
			if (session.Cart.contains(product)) {
                // LATER ITERATION: Attendant approval required. In this iteration, it happens automatically
                double itemWeight = product.getExpectedWeight();
                session.setBulkyWeight(session.getBulkyWeight() + itemWeight);
                session.BulkyItems.add(product);
            } else{
                // if product is not in cart
                System.err.println("Product information error.");
            }
        } else {
            System.err.println("No items in cart. Please scan a product.");
        }
		session.enable();
    }
}
	


