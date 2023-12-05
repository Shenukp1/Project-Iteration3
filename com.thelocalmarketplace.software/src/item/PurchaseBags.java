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
/*
 * This class utilizes the addItem by Barcode method to add dispensible bags to an order
 * checks if there are enough bags for the order in silver and gold hardware.
 */
import javax.swing.JOptionPane;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.bag.ReusableBagDispenserBronze;
import com.jjjwelectronics.bag.ReusableBagDispenserGold;
import com.jjjwelectronics.bag.ReusableBagDispenserListener;
import com.jjjwelectronics.bag.ReusableBagDispenserSilver;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

import GUI.LoadProductDatabases;
import control.SessionController;

//Note:  When adding reusable bags to product database the weight is 5 grams, or 5,000,000 micrograms as specified by the ReusableBag class.
public class PurchaseBags implements ReusableBagDispenserListener {
	private SessionController session;
	private AbstractSelfCheckoutStation station;
	private Barcode bagsBarcode = LoadProductDatabases.bag.itemBarcode;
	private int bagsToBuy;
	
	public PurchaseBags(AbstractSelfCheckoutStation station, SessionController session) {
		this.station = station;
		this.session = session;
		this.bagsToBuy = 0;
		station.getReusableBagDispenser().register(this);
		
	}
	// adds # of bags set by user based on which type of bag dispenser
	public void buyBags() throws EmptyDevice{
	    station.getHandheldScanner().disable();
	    station.getMainScanner().disable();
	    session.disable();
	    
	    //Bronze does not have a check for remaining quantity of bags in device
	    if (station.getReusableBagDispenser() instanceof ReusableBagDispenserBronze) {
	        try {
	            for (int i = 0; i < bagsToBuy; i++) {
	                station.getReusableBagDispenser().dispense();
	                AddItemBarcode.AddItemFromBarcode(session, bagsBarcode);
	            }
	            // JOptionPane.showMessageDialog(null, bagsToBuy + " bags have been added to your order!");
	        } catch (NumberFormatException e) {
	            // JOptionPane.showMessageDialog(null, "Error. Something went wrong.");
	        } finally {
	        	station.getHandheldScanner().enable();
	    	    station.getMainScanner().enable();
	    	    session.enable();
	        }
	        
	    // Silver and Gold check number of bags remaining before adding to order (silver approximates)
	    } else if (station.getReusableBagDispenser() instanceof ReusableBagDispenserSilver || station.getReusableBagDispenser() instanceof ReusableBagDispenserGold) {
	        try {
	        	//checks if quantity in dispenser is enough to complete order
	            if (station.getReusableBagDispenser().getQuantityRemaining() >= bagsToBuy) {
	            	for (int i = 0; i < bagsToBuy; i++) {
	            		station.getReusableBagDispenser().dispense();
		                AddItemBarcode.AddItemFromBarcode(session, bagsBarcode); 
		            }
	            	// JOptionPane.showMessageDialog(null, bagsToBuy + " bags have been added to your order!");
	            } else  {
	            	// JOptionPane.showMessageDialog(null, "We're sorry, there may not be enough bags left in the dispenser");
	            	throw new RuntimeException("Dispenser low or empty");
	            }
	        } catch (NumberFormatException e) {
	            // JOptionPane.showMessageDialog(null, "Error. Something went wrong.");
	        } finally {
	        	station.getHandheldScanner().enable();
	    	    station.getMainScanner().enable();
	    	    session.enable();
	        }
	    } else {
	    	// JOptionPane.showMessageDialog(null, "Error. Could not find bag dispenser");
	    }
	}
	
	public void setBagsToBuy(int numBags) {
		bagsToBuy = numBags;
	}
	public int getBagsToBuy() {
		return bagsToBuy;

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
