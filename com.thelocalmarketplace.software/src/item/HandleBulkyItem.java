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
	


