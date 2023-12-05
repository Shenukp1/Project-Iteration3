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
import java.math.BigInteger;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Mass.MassDifference;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

import control.SelfCheckoutLogic;


public class AddOwnBags {
	private boolean bagsAdded = false;
	private Mass cartMassNoBags;
	private SelfCheckoutLogic logic;
	private BigDecimal bagWeightLimit;

	
	public AddOwnBags(SelfCheckoutLogic logic) {
		this.logic = logic;
		this.bagWeightLimit = new BigDecimal(100); // 100 grams
	}
	
	// running addOwnBags method simulates the user indicating they wish to use their own bags
	// addOwnBags will likely be implemented into a UI in later iteration as a button.
	public void addOwnBags(AbstractSelfCheckoutStation station) throws OverloadedDevice {
		if (logic.session == null || !logic.session.isStarted()) {					// Check if a session is in progress
			System.err.println("Please start a session first");
			return;
		}
		if (logic.session.isDisabled()) {
			System.err.println("Please wait for the system to be enabled");			// Make sure customer interaction is enabled
			
		}
		
		// Disable session while they add bags.
		cartMassNoBags = new Mass(logic.session.getCartWeight());
		logic.session.disable();
		
        System.out.println("Please place your bags in the bagging area.");
        waitForBags();
        AbstractElectronicScale temp = (AbstractElectronicScale) station.getBaggingArea();
        //scale should be polymorphic and accept any version of ElectronicScale. 
		//i.e AbstractElectronicScale scale = new ElectronicScaleBronze(); etc.
        Mass scaleMassWithBags = temp.getCurrentMassOnTheScale();
     
        if (scaleMassWithBags.compareTo(cartMassNoBags) <= 0) {
        	System.err.println("No bags were detected or item removed.");
        }
        //updates the session Cart with the weight of the current order + weight of bags
        if (scaleMassWithBags.compareTo(cartMassNoBags) == 1) {     	
        	
        	//checks weight of bags against an arbitrary weight limit for bags
        	//note: no use case defines what the weight limit for bags should be
        	BigDecimal bagsWeight = scaleMassWithBags.inGrams().subtract(cartMassNoBags.inGrams());
        	if (bagsWeight.compareTo(bagWeightLimit) < 1) {
        		logic.session.setBagWeight(bagsWeight.doubleValue());
            	System.out.println("Please continue with your order.");
        	} else {
        		System.err.println("Bags too heavy, notifying attendant for assistance.");
        		// LATER ITERATION
        		// Attendant approval required; instantly approved for now
        		logic.session.setBagWeight(bagsWeight.doubleValue());
        		System.out.println("Please continue with your order.");	
        	}
        	
        }
        logic.session.enable();
	}
	// Method to wait for bags to be added
    private void waitForBags() throws OverloadedDevice {
        while (!bagsAdded) {
            try {
            	// For now, since there is no UI, this immediately simulates bags being added. In future iterations, this method
                // will wait for user to indicate they have added bags via UI before continuing.
                this.bagsAdded = true;
                Thread.sleep(1000); // Sleep for 1 second
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
		
}

