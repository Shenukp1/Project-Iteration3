package control;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.printer.ReceiptPrinterListener;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

public class CheckForPrinterIssues implements ReceiptPrinterListener {

	AbstractSelfCheckoutStation scs;
	private boolean paperAdded = false;
	private boolean inkAdded = false;
	private int maxInk = 1 << 20;
	private int maxPaper = 1 << 10;
	private int inkRemaining = maxInk;
	private int paperRemaining = maxPaper;
	
	@Override
	public void paperHasBeenAddedToThePrinter() {
		paperAdded = true;
	}

	@Override
	public void inkHasBeenAddedToThePrinter() {
		inkAdded = true;
	}
	
	public CheckForPrinterIssues(AbstractSelfCheckoutStation scs) {
		scs.getPrinter().register(this);
		if (inkAdded == true) {
			inkRemaining = maxInk;
		}
		if (paperAdded == true) {
			paperRemaining = maxPaper;
		}
		checkForPaperAndInk();
	} 
	
	private void checkForPaperAndInk() {
		if (scs.getPrinter().removeReceipt().length() >= 500) {
			inkRemaining = maxInk - 100000;
			paperRemaining = maxPaper - 30;
		} else if (scs.getPrinter().removeReceipt().length() >= 200) {
			inkRemaining = maxInk - 50000;
			paperRemaining = maxPaper - 20;
		} else if (scs.getPrinter().removeReceipt().length() >= 100) {
			inkRemaining = maxInk - 10000;
			paperRemaining = maxPaper - 10;
		} else if (scs.getPrinter().removeReceipt().length() >= 50) {
			inkRemaining = maxInk - 5000;
			paperRemaining = maxPaper - 5;
		}
	}
	
	public int inkRemaining() {
		return inkRemaining;
	}
	
	public int paperRemaining() {
		return paperRemaining;
	}
	
	public boolean paperAdded() {
		return paperAdded;
	}
	
	public boolean inkAdded() {
		return inkAdded;
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
	public void thePrinterIsOutOfPaper() {
	}

	@Override
	public void thePrinterIsOutOfInk() {
	}

	@Override
	public void thePrinterHasLowInk() {
	}

	@Override
	public void thePrinterHasLowPaper() {
	}
	
}