package control;

import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;

/**
 * This is just a temporary class I (usharab) am using to demonstrate how
 * software and hardware (simulation) will interact.
 */

public class TempClass {

	public void main() {
		/* First, create an instance of the SelfCheckoutStation (Bronze, Silver, Gold).
		 * Technically, this will be done in our tests since we never need to 
		 * instantiate the SelfCheckoutStations. This is because we're only creating 
		 * the software the stations will be interacting with.
		 */

		SelfCheckoutStationBronze station = new SelfCheckoutStationBronze();

		/*
		 * Now that we have a station, we need to link the software to it. To do this,
		 * we call a static method in our 'SelfCheckoutLogic' class called 'installOn',
		 * that will return an instance of the SelfCheckoutLogic class that is linked
		 * to our station hardware. This will also only be needed in the tests.
		 * 
		 * installOn takes one parameter that is of type AbstractSelfCheckoutStation
		 * so we can cater Bronze, Silver, and Gold together.
		 */

		SelfCheckoutLogic theLogic = SelfCheckoutLogic.installOn(station);

		/*
		 * The above step started a session on the object 'station'. All the controllers
		 * (payment, weight, barcode) are instantiated in the logic and registered as
		 * listeners to components of 'station'. This will allow them to respond to events
		 * occurring in 'station' and update session details (total, weight, items) accordingly.
		 * 
		 * Example:
		 * 		In test project, we test call 'scan' method on the 'handheldScanner' of
		 * 		'station'. This will trigger an event 'aBarcodeHasBeenScanned' for all
		 * 		listeners of 'handheldScanner', including instance of 'BarcodeController'
		 * 		in our 'theLogic' object (which is an instance of SelfCheckoutLogic).
		 * 		
		 * 		In the 'BarcodeController', we get reference to an object 'BarcodedProduct'
		 * 		that we can get details of. We add the price and weight of the product
		 * 		to the total price and total weight of the customer's cart.
		 */
	}
}
