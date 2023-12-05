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
package control;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Mass.MassDifference;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

public class WeightController implements ElectronicScaleListener{
	
	//Mass is measured in micrograms by default, per Mass.java.
	private Mass expectedWeight;
	private Mass sensitivity;
	private double cartWeight, bagWeight, bulkyWeight;
	private SessionController session;
	private AbstractSelfCheckoutStation station;
	
	public WeightController(SessionController c_session, AbstractSelfCheckoutStation sco) {
		session = c_session;
		station = sco;
		sco.getBaggingArea().register(this);
	}
	
	/**
	 * Method to deal with the event when the mass on the scale changes.
	 * If mass within sensitivity limits, unblocks the session if it was blocked.
	 * Blocks the session otherwise.
	 */
	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		// The weight software is expecting
		cartWeight = session.getCartWeight();
		bagWeight = session.getBagWeight();
		bulkyWeight = session.getBulkyWeight();
		expectedWeight = new Mass(cartWeight + bagWeight - bulkyWeight);
		
		// Sensitivity of the electronic scale
		sensitivity = scale.getSensitivityLimit();
		MassDifference massDifference = mass.difference(expectedWeight);
		
		// If the difference between Expected Weight and Weight on Scale is within the
		// sensitivity limit of the scale, then unblock the session
		if (massDifference.abs().compareTo(sensitivity) < 1){
			if (session.isDisabled()) {
				session.enable();
				System.err.println("Weight discrepancy has been corrected");
			}
		// If difference is outside sensitivity limits, then block the session
		} else {
			session.disable();
			System.err.println("Weight discrepancy has been detected");
		}
	}
	

	/**
	 * Blocks session if mass on the electronic scale has exceeded limit.
	 */
	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		session.disable();
		System.err.println("The mass on the scale has exceeded its limit. Please remove the item.");	
	}

	/**
	 * Unblocks session if mass on the electronic scale has changed within limit.
	 */
	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		session.enable();
		System.err.println("Mass on scale within limits now. System has been enabled.");	
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}

	

}
