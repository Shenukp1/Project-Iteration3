package item;
/*
 * This class utilizes the addItem by PLU code method to add dispensible bags to an order
 * this also allows the company to keep an inventory of # bags sold.
 */
import javax.swing.JOptionPane;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.bag.ReusableBagDispenserListener;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

import control.SessionController;
/*/
 *  TODO GUI TEAM: Connect GUI button that will utilize actionListener to run buyBags();
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
//Note:  When adding reusable bags to product database the weight is 5 grams, or 5,000,000 micrograms as specified by the ReusableBag class.
public class PurchaseBags implements ReusableBagDispenserListener {
	private SessionController session;
	private AbstractSelfCheckoutStation station;
	private Barcode bagsBarcode; // This can be set to whatever barcode bags are assigned to in the product database
	
	public PurchaseBags(AbstractSelfCheckoutStation station, SessionController session) {
		this.station = station;
		this.session = session;
		station.getReusableBagDispenser().register(this);
		
	}
	// prompts the user to enter how many bags to purchase
	public void buyBags() throws EmptyDevice {
		station.getHandheldScanner().disable();
		station.getMainScanner().disable();
		session.disable();
		String input = JOptionPane.showInputDialog("Enter number of bags to purchase.");
		try {
			int bagsToBuy = Integer.parseInt(input);
			if (bagsToBuy > 0) {
				for (int i = 0; i < bagsToBuy; i++) {
					AddItemBarcode.AddItemFromBarcode(session, bagsBarcode);
					station.getReusableBagDispenser().dispense();
				}
				JOptionPane.showMessageDialog(null, bagsToBuy + " bags have been added to your order!");
			} else {
				JOptionPane.showMessageDialog(null, "Please enter a valid number (greater than 0).");
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid number.");
		}
		finally {
			station.getHandheldScanner().enable();
			station.getMainScanner().enable();
			session.enable();
		}
	}
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
	}
	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
	}
	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
	}
	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
	}

	@Override
	public void aBagHasBeenDispensedByTheDispenser() {
		
	}
	@Override
	public void theDispenserIsOutOfBags() {
		String attendantBagMSG = "Station dispenser is out of bags";
		
	}
	@Override
	public void bagsHaveBeenLoadedIntoTheDispenser(int count) {
		String attendantBagLoadMSG = "Bags have been loaded";
	}
}