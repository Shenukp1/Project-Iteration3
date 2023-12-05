package attendant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.tdc.DisabledException;
import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.NoCashAvailableException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteDispenserObserver;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.banknote.BanknoteStorageUnitObserver;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinDispenserObserver;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.CoinStorageUnitObserver;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

public class Maintain implements ReceiptPrinterListener, 
	CoinDispenserObserver, BanknoteDispenserObserver, IDeviceListener{
	
	
	
	
	private IReceiptPrinter printer;
	
	private ReceiptPrinterBronze receiptPrinterBronze;
	private ReceiptPrinterSilver receiptPrinterSilver;
	private ReceiptPrinterGold receiptPrinterGold;
	
	
	
	private BanknoteStorageUnit banknoteStorage;
	private CoinStorageUnit coinStorage;
	private Map<BigDecimal, ICoinDispenser> cDispenser;
	public ICoinDispenser dollarDispenser;
	private Map<BigDecimal, IBanknoteDispenser> nDispenser;
	public IBanknoteDispenser noteDispenser;
	

	
	
	
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
	
	private boolean changesOccurred = false;
	public String maintenanceMessage;
	private NotifyAttendantScreen attendantGUI = new NotifyAttendantScreen();
	private ArrayList<String> list = new ArrayList<>(); // list of all maintenance done
	
	


	private boolean isMaintenance;// field to allow maintenance to happen. no maintenance happens at the start,thus, false

	private AbstractSelfCheckoutStation station;

	private int PrinterPaperAddedGold;

	private boolean coinAddedMessage;

	private boolean coinLow;

	private boolean coinUnloadedMessage;

	private boolean bankNotesLow;
	
	
	

	/**
	 * Allows the station to have maintenance 
	 * @param station that is able to have maintenance 
	 */
	public Maintain(AbstractSelfCheckoutStation station)  {
		
		this.station = station;
		
		this.isMaintenance = false;
		
		printer = this.station.getPrinter();
		printer.register(this);
		
		
		cDispenser = this.station.getCoinDispensers();
		
		//Might need to fix for all denoms
		dollarDispenser = cDispenser.get(new BigDecimal("1"));
		dollarDispenser.attach(this);
		
		
		//Might need to fix for all denoms
		BigDecimal denoms = this.station.getBanknoteDenominations()[0];
        noteDispenser = this.station.getBanknoteDispensers().get(denoms);
        noteDispenser.attach(this);
		
		
		receiptPrinterGold = new ReceiptPrinterGold();
		receiptPrinterGold.register(this);
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		receiptPrinterGold.plugIn(PowerGrid.instance());
		receiptPrinterGold.turnOn();
		
		// ----NEW STUFF -----
		
		
		/**
		 * This updates the Attendant Station's GUI for new issues within the self
		 * checkout station
		 */
		String text = "";
		for (int i = 0; i < list.size(); i++) {
			text += "<html>" + list.get(i) + "<br/>";
		}
		attendantGUI.update(text);
			
	
		
		
	}
	
	
	
	/*
	 * Start maintenance and disables the station
	 */
	public void maintenanceStart() {
		printer.disable();
		disabledM();
	}
	
	/*
	 * Ends maintenance and enables the station
	 */
	public void maintenanceFinished() {
		printer.enable();
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
		
		list.add("Ink has been added to the printer.");
			
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
	

	// setting nickle dispenser for coins for testing
	public void setCoins(Coin... coins) throws OverloadedDevice,SimulationException, CashOverloadException, DisabledException {
		dollarDispenser.load(coins);
		
	}
	
	/**
	 * If maintenance is happening. Attendant can add coin. If not, then we cannot. 
	 * station must be disabled.
	 * @param coin
	 * @throws DisabledException
	 * @throws CashOverloadException
	 */
	public void maintainAddCoin(Coin coin) throws DisabledException, CashOverloadException {
		if(isMaintenance == true) {
			dollarDispenser.load(coin);
			
		}
		list.add("A coin has been added to the customer station.");
	}
	
	public void maintainEmitCoin(Coin coins) throws DisabledException, CashOverloadException, NoCashAvailableException {
		if(isMaintenance == true) {
			System.out.println("dispenser size before unload: "+ dollarDispenser.size());
			dollarDispenser.unload();
			System.out.println("dispenser size after unload: "+ dollarDispenser.size());
			
		}
		list.add("A coin has been unloaded to the customer station.");
	}
	
	
	
	
	
	//According to the documentation. Gold and bronze keeps track of the same amount of Ink Printed
	public int getInkAdded() {
		PrinterInkAddCountGold = receiptPrinterGold.inkRemaining();
		return PrinterInkAddCountGold;
	}
	
	
	//Gets the max ink value allowed to be added
	public int getMaxInkValue() {
		maxInk = receiptPrinterGold.MAXIMUM_INK;
		return maxInk;
	}
	
	public int getPaperAdded() {
		PrinterPaperAddedGold = receiptPrinterGold.paperRemaining();
		return PrinterPaperAddedGold;
	}
	
	//Gets the Max amount of paper allowed in printer
	public int getMaxPaperValue() {
		maxPaper = receiptPrinterGold.MAXIMUM_PAPER;
		return maxPaper;
	}
	
	
	
	
	
	// Attendant adds paper 
	public void maintainAddPaper(int quantity) throws OverloadedDevice {
		if (isMaintenance == true) {
			
			printer.addPaper(quantity); // AbstractReceiptPrinter. Announces "paperAdded" event. Requires power.
			receiptPrinterGold.addPaper(quantity);
		}
		
		
	}
	
	
	

	/*
	 * Setting Initial value for station
	 */
	public void setBanknotes(Banknote... banknotes) throws CashOverloadException {
		Banknote[] banknote = new Banknote[1000]; // Array to hold 800 banknotes

	    // Fill the array with the 'five' banknote 800 times
	    for (int i = 0; i < 800; i++) {
	        banknote[i] = banknotes;
	    }
		noteDispenser.load(banknotes);
		
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
		
		changesOccurred = true;
		
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
		
		changesOccurred = true;
	}
	
	/*
	 * Gets the inkAddedMessage Value
	 */
	public boolean getInkAddedMessage() {
		return inkAddedMessage;
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



	@Override
	public void moneyFull(IBanknoteDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void banknotesEmpty(IBanknoteDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void banknoteAdded(IBanknoteDispenser dispenser, Banknote banknote) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void banknoteRemoved(IBanknoteDispenser dispenser, Banknote banknote) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void banknotesLoaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
		coinAddedMessage = true;
		System.out.println("BANK dispenser size: " + dispenser.size());
		System.out.println("BANK capacity: " + dispenser.getCapacity());
		int high = (dispenser.getCapacity()/2)+300;
		int low = (dispenser.getCapacity()/2)-300;
		System.out.println("high number: " + high);
		System.out.println("low number: " + low);
		
		if(dispenser.size() >= high) {
			bankNotesLow = false;
			dollarDispenser.disable();
			disabledM();
			
		} else if(dispenser.size() <= low){
			bankNotesLow = true;
			dollarDispenser.disable();
			disabledM();
		} else {
			enabledM();
		}		
	}



	@Override
	public void banknotesUnloaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void coinsFull(ICoinDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void coinsEmpty(ICoinDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void coinAdded(ICoinDispenser dispenser, Coin coin) {
		
		
		
	}

	
	public boolean getCoinAddedMessage() {
		return coinAddedMessage;
	}
	
	public boolean getCoinLow() {
		return coinLow;
	}



	@Override
	public void coinRemoved(ICoinDispenser dispenser, Coin coin) {
		// TODO Auto-generated method stub
		
	}



	@Override
	
	public void coinsLoaded(ICoinDispenser dispenser, Coin... coins) {
		
		coinAddedMessage = true;
		
		System.out.println("dispenser size: " + dispenser.size());
		System.out.println("capacity: " + dispenser.getCapacity());
		
		int high = (dispenser.getCapacity()/2)+3;
		int low = (dispenser.getCapacity()/2)-3;
		
		System.out.println("high number: " + high);
		System.out.println("low number: " + low);
		
		if(dispenser.size() >= high) {
			coinLow = false;
			dollarDispenser.disable();
			disabledM();
			
		} else if(dispenser.size() <= low){
			coinLow = true;
			dollarDispenser.disable();
			disabledM();
		} else {
			enabledM();
		}
		
	}



	@Override
	public void coinsUnloaded(ICoinDispenser dispenser, Coin... coins) {
		coinUnloadedMessage = true;
		
		System.out.println("dispenser size: " + dispenser.size());
		System.out.println("capacity: " + dispenser.getCapacity());
		
		int high = (dispenser.getCapacity()/2)+3;
		int low = (dispenser.getCapacity()/2)-3;
		
		System.out.println("high number: " + high);
		System.out.println("low number: " + low);
		
		if(dispenser.size() >= high) {
			coinLow = false;
			dollarDispenser.disable();
			disabledM();
			
		} else if(dispenser.size() <= low){
			coinLow = true;
			dollarDispenser.disable();
			disabledM();
		} else {
			enabledM();
		}
		
	}
	
}
