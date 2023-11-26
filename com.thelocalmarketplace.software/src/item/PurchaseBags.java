package item;
/*
 * This class utilizes the addItem by PLU code method to add dispensible bags to an order
 * this also allows the company to keep an inventory of # bags sold.
 */
import javax.swing.JOptionPane;
/*/
 * TODO Open Issue: Should the CheckoutStation track the number of dispensible bags it has left and disable when out of stock?
 */
/*
 *  TODO Connect GUI button that will utilize actionListener to run buyBags();
 *  ex. 
 *  create button:
 *  buyBagsButton = new JButton("Purchase Bags");
 *  
 *  create Listener object and override its method to do what we want:
 *  public void SetUpButtonListeners() {
 *  ActionListener purchaseBagsListener = new ActionListener() {
 *  	@Override
 *  	public void actionPerformed(ActionEvent e) {
 *  		PurchaseBags.buyBags();
 *  		}
 *  	};
 *  
 *  register listener to the button:
 *  buyBagsButton.addActionListener(purchaseBagsListener);
 *  }
 *  
 */

public class PurchaseBags {
	private int numBags;
	
	public PurchaseBags() {
		this.numBags = 0;
	}
	
	// prompts the user to enter how many bags to purchase
	public void buyBags() {
		//TODO Implement disable method here to prevent scanning/paying during buyBags().
		String input = JOptionPane.showInputDialog("Enter number of bags to purchase.");
		try {
			int bagsToBuy = Integer.parseInt(input);
			if (bagsToBuy > 0) {
				for (int i = 0; i < bagsToBuy; i++) {
					/*
					 *  TODO use AddItem implementation to add bags from database.
					 *  will increase expected weight by expected weight of each bag.
					 *  will increase order price by price of each bag.
					 *  
					 */
				}
				numBags += bagsToBuy;
				JOptionPane.showMessageDialog(null, bagsToBuy + "bags have been added to your order!");
				//TODO Implement enable method here.
			} else {
				JOptionPane.showMessageDialog(null, "Please enter a valid number (greater than 0).");
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid number.");
		}
	}
		
	//Optional: call to display how many bags have been purchased. Not required. If not, Numbags can be removed.
	//TODO How to decrement numBags when removeItem() method is called
	public int getNumberOfBags() {
        return numBags;
    }

}

