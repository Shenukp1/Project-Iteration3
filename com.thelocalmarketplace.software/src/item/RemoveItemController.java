package item;

import java.math.BigDecimal;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import control.SessionController;
import control.WeightController;

public class RemoveItemController {
    private SessionController session;
    private AbstractSelfCheckoutStation station;

    public RemoveItemController(SessionController c_session, AbstractSelfCheckoutStation sco) {
        station = sco;
        session = c_session;
    }

    public void removeItem(BarcodedProduct item) {
        // Block further customer actions
        session.disable();

        // Remove the item from the session's cart
        int removal = remove(item);
         
        // Request customer to remove the item from bagging area
        // Once customer does that, WeightController will enable session
        if (removal == 0)
        	System.out.println("Please remove the item from the bagging area");
    }

    private int remove(BarcodedProduct item) {
        // Check if the cart contains the item
        if (session.Cart.contains(item)) {
            // Remove the item from the cart
        	session.Cart.remove(item);

            // Update the total price and weight
            long priceOfItem = item.getPrice();
            double weightOfItem = item.getExpectedWeight();

            // Convert priceOfItem to BigDecimal
            BigDecimal priceOfItemBigDecimal = BigDecimal.valueOf(priceOfItem);

            // Subtract the price and weight of the removed item from the cart's total
            session.setCartTotal(session.getCartTotal().subtract(priceOfItemBigDecimal));
            session.setCartWeight(session.getCartWeight() - weightOfItem);
            
            // If it was a bulky item, remove it from our list
            if (session.BulkyItems.contains(item)) {
            	session.BulkyItems.remove(item);
            	session.setBulkyWeight(session.getBulkyWeight()-weightOfItem);
            }
            return 0;
        } 
        else {
            System.err.println("Item not found in the cart");
            return -1;
        }
    }
}
