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
