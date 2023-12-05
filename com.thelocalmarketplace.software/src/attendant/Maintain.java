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
