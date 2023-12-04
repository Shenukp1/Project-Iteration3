package attendant;

//GENERAL IDEA FOR MAINTAIN IMPLEMENTATION
//	1.1 A system for detecting low or empty levels of materials(Ink,paper,coin,banknotes)
//	1.2 open hardware and close hardware
//	1.3 A procedure for adding the required materials without causing (spillage,jams, or damage)
//	1.4 The system detects the changes after  
//	1.5 GUI



import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.jjjwelectronics.printer.ReceiptPrinterBronze;
import com.jjjwelectronics.printer.ReceiptPrinterGold;
import com.jjjwelectronics.printer.ReceiptPrinterListener;
import com.jjjwelectronics.printer.ReceiptPrinterSilver;
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

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

public class Maintain implements ReceiptPrinterListener,BanknoteStorageUnitObserver,CoinStorageUnitObserver, IDeviceListener{
	
	
	
	
	private IReceiptPrinter printer;
	
	private ReceiptPrinterBronze receiptPrinterBronze;
	private ReceiptPrinterSilver receiptPrinterSilver;
	private ReceiptPrinterGold receiptPrinterGold;
	
	
	
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
	
	int coinLevel;

	private int PrinterInkAddCountGold;//Keeps track of the ink in printer

	private int maxInk;//Has the max Ink allowed in printer
	private int maxPaper;
	
	


	private boolean isMaintenance;// field to allow maintenance to happen. no maintenance happens at the start,thus, false

	private AbstractSelfCheckoutStation station;
	
	
	

	/**
	 * Allows the station to have maintenance 
	 * @param station that is able to have maintenance 
	 */
	public Maintain(AbstractSelfCheckoutStation station)  {
		
		this.station = station;
		
		this.isMaintenance = false;
		
		printer = this.station.getPrinter();
		printer.register(this);
		
		banknoteStorage = station.getBanknoteStorage();
		banknoteStorage.attach(this);
		
		coinStorage = station.getCoinStorage();
		coinStorage.attach(this);
		
		receiptPrinterGold = new ReceiptPrinterGold();
		receiptPrinterGold.register(this);
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		receiptPrinterGold.plugIn(PowerGrid.instance());
		receiptPrinterGold.turnOn();
		
		
	}
	
	
	
	/*
	 * Start maintenance and disables the station
	 */
	public void maintenanceStart() {
		disabledM();
	}
	
	/*
	 * Ends maintenance and enables the station
	 */
	public void maintenanceFinished() {
		enabledM();
	}
	
	public void detectChange() {
		
	}
	

	
	
	/**
	 * If maintenance is happening. we can add ink. If not, then we cannot. 
	 * station must be disabled 
	 * @param quantity - the quantity of ink that is being added
	 * @throws OverloadedDevice - when to much ink is added
	 */
	public void maintainAddInk(int quantity) throws OverloadedDevice, InvalidArgumentSimulationException {
		if (isMaintenance == true) {
			
			printer.addInk(quantity); // AbstractReceiptPrinter. Announces "inkAdded" event. Requires power.
			//PROBLEM: gold,silver,bronze CheckoutStations all use the BronzeReceiptPrinter
			//SOLUTION(possibly): to keep track of bronzePrinter ink count we will have an instance of the goldPrinter
			//	Why: because in the documentation, gold and bronze are the same. thus, we can use the gold to get Ink values to check and possibly other things
			//Honeslty this might not be useful for this usecase other than to check
			receiptPrinterGold.addInk(quantity);
			
		} 
			
	}
	
	/*
	 * Prepare station before customer is able to use it
	 * Its the intial setup. thus, the system has not been booted up yet
	 * thus, printing can be done
	 * ADD: wrap it with a if statement that has a count. this should only be able to be done 
	 */
	public void setInitial(int ink, int paper) throws OverloadedDevice,InvalidArgumentSimulationException {
		printer.addInk(ink);
		receiptPrinterGold.addInk(ink);
		printer.addPaper(paper);
		receiptPrinterGold.addPaper(paper);
	}
	
	/**
	 * allows to print if maintenance is not happening 
	 * @param c - 1 char that is printed on to the paper using 1 ink
	 */
	
	public void print(char c) {
		if(isMaintenance == false) {
			try {
				printer.print(c);
				receiptPrinterGold.print(c);
			} catch (EmptyDevice e) {
				e.printStackTrace();
			} catch (OverloadedDevice e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	//According to the documentation. Gold and bronze keeps track of the same amount of Ink Printed
	public int getInkAdded() {
		PrinterInkAddCountGold = receiptPrinterGold.inkRemaining();
		return PrinterInkAddCountGold;
	}
	
	//Gets the max ink value allowed to be added
	public int getMaxInkValue() {
		System.out.println("YO"+receiptPrinterGold.MAXIMUM_INK);
		maxInk = receiptPrinterGold.MAXIMUM_INK;
		return maxInk;
	}
	
	public int getMaxPaperValue() {
		System.out.println("YO"+receiptPrinterGold.MAXIMUM_PAPER);
		maxInk = receiptPrinterGold.MAXIMUM_PAPER;
		return maxPaper;
	}
	
	
	
	
	
	// Attendant adds paper 
	public void maintainAddPaper(int quantity) throws OverloadedDevice {
		if (isMaintenance == true) {
			printer.addPaper(quantity); // AbstractReceiptPrinter. Announces "paperAdded" event. Requires power.
			receiptPrinterGold.addPaper(quantity);
		}
		
		
	}
	
	// Attendant adds bank notes
	public void maintainAddBanknotes(Banknote... banknotes) throws SimulationException, CashOverloadException {
		if (isMaintenance == true) {
			banknoteStorage.load(banknotes);// BanknoteStorageUnit. Announces "banknotesLoaded" event. Disabling has no effect. Requires power.

		}
		

	}
	
	// Attendant adds coins
	public void maintainAddCoins(Coin... coins) throws SimulationException, CashOverloadException {
		if (isMaintenance == true) {
			coinStorage.load(coins);// CoinStorageUnit. Announces "coinsLoaded" event. Disabling has no effect. Requires power.

				}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	//====STUFF THAT NEEDS TO BE CHANGED======
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		
		this.isMaintenance = false;
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		this.isMaintenance = true;		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	
//===============Detecting Changes==============
	
	
	
	
	public void enabledM() {
		this.isMaintenance = false;
	}

	
	public void disabledM() {
		this.isMaintenance = true;
	}
	/*
	 * check if maintenance is happening
	 */
	public boolean getMaintenance() {
		return this.isMaintenance;
	}

	
	


	//==================Paper=======================
	
	
	@Override
	/*
	 * Method Used to announce that the printer is out of paper
	 */
	public void thePrinterIsOutOfPaper() {
		outOfPaperMessage = true;
		printer.disable();
	}
	
	/*
	 * Gets the outOfPaperMessage value
	 */
	public boolean getOutOfPaperMessage() {
		return outOfPaperMessage;
		
	}
	
	@Override
	/*
	 * Method Used to announce that the printer is low on paper
	 * disbale the station if it does
	 */
	public void thePrinterHasLowPaper() {
		printer.disable();
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
		if (receiptPrinterGold.paperRemaining() >= getMaxPaperValue()*0.1 ) {
			lowPaperMessage = false;
			printer.enable();

		}
		paperAddedMessage = true;
		
	}

	/*
	 * Gets the paperAddedMessage Value
	 */
	public boolean getPaperAddedMessage() {
		return paperAddedMessage;
	}
	
	
	
	//==================Ink=======================

	
		

	@Override
    /*
     * Method Used to announce that the printer is out of ink
     * outOfInkMessage is originally false but when this method is called, the listener 
     * is notified, then the outOfInkMessage turns true
     */
    public void thePrinterIsOutOfInk() {
        outOfInkMessage = true;
        disabledM();
		printer.disable();

        
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
		printer.disable();

    }
    
    /*
     * Gets the lowInkMessage Value
     */
    public boolean getLowInkMessage() {
        return lowInkMessage;
    }

	
	@Override
	/*
	 * Method Used to announce that ink has been added to the printer
	 */
	public void inkHasBeenAddedToThePrinter() {
		if (receiptPrinterGold.inkRemaining() >= 104858) {
			lowInkMessage = false;
			printer.enable();

		}
		inkAddedMessage = true;
	}
	
	/*
	 * Gets the inkAddedMessage Value
	 */
	public boolean getInkAddedMessage() {
		return inkAddedMessage;
	}
	
	
	

	
	//==================Banknotes=======================


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
	
	
	
	
	
	//==================Coin=======================
	
	

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
	/*
	 * After coin added, check if coin level high or low. 
	 * If high, coinLevel = 1. If low, coinLevel = -1.
	 */
	public void coinAdded(CoinStorageUnit unit) {
		if (unit.getCoinCount() > unit.getCapacity()/2) {
			coinLevel = 1;
		} else {
			coinLevel = -1;
		}
	}
	
	/*
	 * Gets the coinLevel value. (if coin level is high after adding coin)
	 */
	public int getCoinLevel() {
		return coinLevel;
	
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



	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		
	}



	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		
	}



	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}
	
}
