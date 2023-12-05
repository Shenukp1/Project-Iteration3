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
import com.thelocalmarketplace.hardware.Product;
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

    public void removeItem(Product item) {
        // Block further customer actions
        session.disable();

        // Remove the item from the session's cart
        int removal = remove(item);
         
        // Request customer to remove the item from bagging area
        // Once customer does that, WeightController will enable session
        if (removal == 0)
        	System.out.println("Please remove the item from the bagging area");
    }

    private int remove(Product item) {
        // Check if the cart contains the item
        if (session.Cart.contains(item)) {
            // Remove the item from the cart
        	session.Cart.remove(item);

            // Update the total price and weight
            long priceOfItem = item.getPrice();
            double weightOfItem = ((BarcodedProduct) item).getExpectedWeight();

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
