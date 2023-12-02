package attendant;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.printer.ReceiptPrinterListener;
import com.tdc.CashOverloadException;
import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.banknote.BanknoteStorageUnitObserver;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.CoinStorageUnitObserver;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import ca.ucalgary.seng300.simulation.SimulationException;

public class Maintain implements ReceiptPrinterListener,BanknoteStorageUnitObserver,CoinStorageUnitObserver  {
	private IReceiptPrinter printer;
	private BanknoteStorageUnit banknoteStorage;
	private CoinStorageUnit coinStorage;
	
	//printer
	private Boolean lowInkMessage = false;
	private Boolean outOfInkMessage= false;
	private Boolean inkAddedMessage = false;
	private Boolean lowPaperMessage = false;
	private Boolean outOfPaperMessage = false;
	private Boolean paperAddedMessage = false;
	
	
	//banknotes
	private Boolean lowBanknotesMessage = false;
	private Boolean outOfBanknotesMessage = false;
	private Boolean banknotesAddedMessage = false;
	
	//coins
	private Boolean lowCoinsMessage = false;
	private Boolean outOfCoinsMessage = false;
	private Boolean coinsAddedMessage = false;
	
	public Maintain(AbstractSelfCheckoutStation station)  {
		//shenuk - changed these from .printer,.banknotestorage,.coinStorage to getter b/c of new hardware
		station.getPrinter().register(this);
		station.getBanknoteStorage().attach(this);
		station.getCoinStorage().attach(this);
		
	}
	
	// Attendant adds ink
	public void maintainInk(int quantity) throws OverloadedDevice {
		System.out.println("remaining ink: "+ printer.inkRemaining());
		printer.addInk(quantity); // AbstractReceiptPrinter. Announces "inkAdded" event. Requires power.
		System.out.println("remaining ink: "+ printer.inkRemaining());
	}
	
	
	// Attendant adds paper 
	public void maintainPaper(int quantity) throws OverloadedDevice {
		printer.addPaper(quantity); // AbstractReceiptPrinter. Announces "paperAdded" event. Requires power.
		
	}
	
	// Attendant adds bank notes
	public void maintainBanknotes(Banknote... banknotes) throws SimulationException, CashOverloadException {
		banknoteStorage.load(banknotes);// BanknoteStorageUnit. Announces "banknotesLoaded" event. Disabling has no effect. Requires power.

	}
	
	// Attendant adds coins
	public void maintainCoins(Coin... coins) throws SimulationException, CashOverloadException {
		coinStorage.load(coins);// CoinStorageUnit. Announces "coinsLoaded" event. Disabling has no effect. Requires power.
	}

	
	//====STUFF THAT NEEDS TO BE CHANGED======
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/*
	 * Method Used to announce that the printer is out of paper
	 */
	public void thePrinterIsOutOfPaper() {
		outOfPaperMessage = true;
	}
	
	/*
	 * Gets the outOfPaperMessage value
	 */
	public boolean getOutOfPaperMessage() {
		return outOfPaperMessage;
	}

	@Override
	/*
	 * Method Used to announce that the printer is out of ink
	 */
	public void thePrinterIsOutOfInk() {
		outOfInkMessage = true;
		
	}
	
	/*
	 * Gets the outOfInkMessage value
	 */
	public boolean getOutOfInkMessage() {
		return outOfInkMessage;
	}

	@Override
	/*
	 * Method Used to announce that the printer is low on ink
	 */
	public void thePrinterHasLowInk() {
		lowInkMessage = true;
	}
	
	/*
	 * Gets the lowInkMessage Value
	 */
	public boolean getLowInkMessage() {
		return lowInkMessage;
	}

	@Override
	/*
	 * Method Used to announce that the printer is low on paper
	 */
	public void thePrinterHasLowPaper() {
		lowPaperMessage = true;
	}
	
	/*
	 * Gets the lowPaperMessage Value
	 */
	public boolean getLowPaperMessage() {
		return lowPaperMessage;
	}

	@Override
	/*
	 * Method Used to announce that paper has been added to the printer
	 */
	public void paperHasBeenAddedToThePrinter() {
		paperAddedMessage = true;
		
	}

	/*
	 * Gets the paperAddedMessage Value
	 */
	public boolean getPaperAddedMessage() {
		return paperAddedMessage;
	}
	
	@Override
	/*
	 * Method Used to announce that ink has been added to the printer
	 */
	public void inkHasBeenAddedToThePrinter() {
		inkAddedMessage = true;
	}
	
	/*
	 * Gets the inkAddedMessage Value
	 */
	public boolean getInkAddedMessage() {
		return inkAddedMessage;
	}
	
	//=== paper ink ends here==

	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banknotesFull(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banknoteAdded(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banknotesLoaded(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void banknotesUnloaded(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}
	//===Banknotes stuff ends here===

	@Override
	public void coinsFull(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinAdded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsLoaded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsUnloaded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}
	
}
