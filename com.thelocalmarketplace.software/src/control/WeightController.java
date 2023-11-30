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
