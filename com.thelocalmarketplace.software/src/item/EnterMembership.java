package item;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import control.SessionController;

import com.jjjwelectronics.card.CardReaderListener;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

/*
 * This class is designed to provide the membership number to the system, for future customer loyalty program use.
 * For membership input, this class offer 3 ways:
 * 	1) Enter membership number through virtual numeric keyboard on the touch screen from self-checkout system
 * 	2) Swipe the membership Card through card reader
 * 	3) Scan the barcode on membership Card through hand held scanner
 * 
 */
public class EnterMembership implements CardReaderListener, BarcodeScannerListener{
	private SessionController session;
	private AbstractSelfCheckoutStation station;
	private String membershipNumber;
	public EnterMembership(SessionController c_session, AbstractSelfCheckoutStation sco) {
		station = sco;
		session = c_session;
		station.getHandheldScanner().register(this);
		sco.getCardReader().register(this);
	}
	
	/*
	 * Getter of the membership number
	 */
	public String getMembershipNumber() {
		return membershipNumber;
	}
	
	/*
	 * Enter membership number by touch screen
	 * Supposed that GUI should only show the Numeric keyboard to customer on touch screen, so number-only check will be skipped here for now.
	 */
//	public void EnterByTouchScreen() {
//		membershipNumber = JOptionPane.showInputDialog("Please Enter your Membership Number if you have one");
//	}
//	
	/*
	 * Enter membership number through card reader
	 */
	@Override
	public void theDataFromACardHasBeenRead(CardData data) throws NullPointerSimulationException{
		String cardType = data.getType(); 						// Get card type
		String cardNumber = data.getNumber(); 					// Get card number
		
		if(cardType == null) {
			new NullPointerSimulationException("cardType");		// Check if the card Type in null
		}
		else if(cardType != "membership") {						// Check if the card Type is membership Card
			System.err.println("membership card expected, type used is: " + cardType);
		}
		this.membershipNumber = cardNumber;
		session.membership = cardNumber;
			//System.err.println("membership card read successfully");
			
	}

		
	/*
	 * Enter membership number through hand held scanner
	 */
	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
		if (session == null || !session.isStarted()) {								// Check if a session is in progress
			System.err.println("Please start a session first");
			return;
		}
		this.membershipNumber = barcode.toString();										// Set the barcode as membershipNumber
		session.membership = membershipNumber;
			//System.err.println("membership card read successfully");
	}
	
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aCardHasBeenInserted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theCardHasBeenRemoved() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aCardHasBeenTapped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aCardHasBeenSwiped() {
		// TODO Auto-generated method stub
		
	}
	
	
}