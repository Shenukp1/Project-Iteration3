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

	private Boolean lowInkMessage = false;
	private Boolean outOfInkMessage= false;
	private Boolean inkAddedMessage = false;
	private Boolean lowPaperMessage = false;
	private Boolean outOfPaperMessage = false;
	private Boolean paperAddedMessage = false;
	private Boolean banknotesFullMessage = false;
	//private Boolean banknoteAddedMessage = false;
	private Boolean banknotesLoadedMessage = false;
	private Boolean banknotesUnloadedMessage = false;
	private Boolean coinsFullMessage = false;
	//private Boolean coinAddedMessage = false;
	private Boolean coinsLoadedMessage = false;
	private Boolean coinsUnloadedMessage = false;

	
	
	public Maintain(AbstractSelfCheckoutStation station,BanknoteStorageUnit bns,CoinStorageUnit csu)  {
		//shenuk - changed these from .printer,.banknotestorage,.coinStorage to getter b/c of new hardware
		station.getPrinter().register(this);
		station.getBanknoteStorage().attach(this);
		station.getCoinStorage().attach(this);
		bns.attach(this);
		csu.attach(this);
		
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
	/*
	 * Method Used to announce that the banknote storage is full
	 */
	public void banknotesFull(BanknoteStorageUnit unit) {
		banknotesFullMessage = true;
		
	}
	
	/*
	 * Gets the banknotesFullMessage Value
	 */
	public boolean getBanknotesFullMessage() {
		return banknotesFullMessage;
	}

	@Override
	public void banknoteAdded(BanknoteStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/*
	 * Method Used to announce that banknotes have been loaded into storage
	 */
	public void banknotesLoaded(BanknoteStorageUnit unit) {
		banknotesLoadedMessage = true;
		
	}
	
	/*
	 * Gets the banknotesLoadedMessage Value
	 */
	public boolean getBanknotesLoadedMessage() {
		return banknotesLoadedMessage;
	}

	@Override
	/*
	 * Method Used to announce that banknotes have been unloaded from storage
	 */
	public void banknotesUnloaded(BanknoteStorageUnit unit) {
		banknotesUnloadedMessage = true;
		
	}
	
	/*
	 * Gets the banknotesUnloadedMessage Value
	 */
	public boolean getBanknotesUnloadedMessage() {
		return banknotesUnloadedMessage;
	}
	
	//===Banknotes stuff ends here===
	
	

	@Override
	/*
	 * Method Used to announce that coin storage is full
	 */
	public void coinsFull(CoinStorageUnit unit) {
		coinsFullMessage = true;
		
	}
	
	/*
	 * Gets the coinsFullMessage Value
	 */
	public boolean getCoinsFullMessage() {
		return coinsFullMessage;
	}

	@Override
	public void coinAdded(CoinStorageUnit unit) {
		if (unit.getCoinCount() > unit.getCapacity()-3) {
			boolean coinLevelHigh = true;
		} else if (unit.getCoinCount() > 2) {
			boolean coinLevelLow = true;
		}
		
	}

	@Override
	/*
	 * Method Used to announce that coins have been loaded into storage
	 */
	public void coinsLoaded(CoinStorageUnit unit) {
		coinsLoadedMessage = true;
		
	}
	
	/*
	 * Gets the coinsLoadedMessage Value
	 */
	public boolean getCoinsLoadedMessage() {
		return coinsLoadedMessage;
	}

	@Override
	/*
	 * Method Used to announce that coins have been unloaded from storage
	 */
	public void coinsUnloaded(CoinStorageUnit unit) {
		coinsUnloadedMessage = true;
		
	}
	
	/*
	 * Gets the coinsUnloadedMessage Value
	 */
	public boolean getCoinsUnloadedMessage() {
		return coinsUnloadedMessage;
	}
	
}
