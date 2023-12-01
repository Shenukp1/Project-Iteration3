package attendant;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.tdc.CashOverloadException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteStorageUnit;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinStorageUnit;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import ca.ucalgary.seng300.simulation.SimulationException;

public class Maintain {
	private IReceiptPrinter printer;
	private BanknoteStorageUnit banknoteStorage;
	private CoinStorageUnit coinStorage;
	
	
	public Maintain(AbstractSelfCheckoutStation station) {
		this.printer = station.getPrinter();
		this.banknoteStorage = station.getBanknoteStorage();
		this.coinStorage = station.getCoinStorage();
	}
	
	// Attendant adds ink
	public void maintainInk(int quantity) throws OverloadedDevice {
		printer.addInk(quantity); // AbstractReceiptPrinter. Announces "inkAdded" event. Requires power.
		
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
	
	
}
