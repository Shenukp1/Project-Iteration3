package item;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.printer.ReceiptPrinterListener;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;

import control.SessionController;

public class PrintController implements ReceiptPrinterListener{
	private SessionController session;
	private AbstractSelfCheckoutStation station;
	private static final int CHARACTERS_PER_LINE = 60;
	public static int linesUsed = 0;
	
	public PrintController(SessionController session, AbstractSelfCheckoutStation station) {
		this.session = session;
		this.station = station;
		station.getPrinter().register(this);
	}
	
	public static void printReceipt(AbstractSelfCheckoutStation station, SessionController session) {
		IReceiptPrinter printer = station.getPrinter();					// The printer we need to print from
		StringBuilder receiptText = new StringBuilder();
		long p;
        Product item;
        String desc;
        															// Build our receipt text string
        for (int i = 0; i < session.Cart.size(); i++) {
        	item = session.Cart.get(i); 							// Get item at index i in cart
        	p = item.getPrice();
        	receiptText.append("Price: $" + p +", ");						// Print price without new line
        	if (item instanceof BarcodedProduct) {						
        		desc = ((BarcodedProduct) item).getDescription();	// Print description if its a barcoded product
        		receiptText.append("Description: " + desc);
        	}
        	receiptText.append("\n");								// Print a full stop with new line
        }
       
        String[] lines = receiptText.toString().split("\n");		// Split our receipt text into lines
        int character;
        															// Loop through the lines of our receipt text
        for (int line = 0; line < (lines.length-1); line++) {
            														// Loop through the characters in each line
        	character = 0;
        	while ((character < lines[line].length()) && (character < CHARACTERS_PER_LINE)) {				// Print maximum characters per line
        		try {
					printer.print(lines[line].charAt(character));
				} catch (EmptyDevice | OverloadedDevice e) {		// Catch exceptions thrown by printer
					e.printStackTrace();
				}
        		character++;
        	}
        	linesUsed++;
        }
	}
	
	public int getLinesUsed() {
		return linesUsed;
	}
	@Override
	public void thePrinterIsOutOfPaper() {
		System.err.println("Printer has run out of paper");
		station.getPrinter().disable();
	}

	@Override
	public void thePrinterIsOutOfInk() {
		System.err.println("Printer has run out of ink");
		station.getPrinter().disable();
	}

	@Override
	public void paperHasBeenAddedToThePrinter() {
		System.err.println("Paper added to printer");
		station.getPrinter().enable();
	}

	@Override
	public void inkHasBeenAddedToThePrinter() {
		System.err.println("Ink added to printer");
		station.getPrinter().enable();
	}
	
	@Override
	public void thePrinterHasLowInk() {
		System.err.println("Printer is running low on ink");
	}

	@Override
	public void thePrinterHasLowPaper() {
		System.err.println("Printer is running low on paper");
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