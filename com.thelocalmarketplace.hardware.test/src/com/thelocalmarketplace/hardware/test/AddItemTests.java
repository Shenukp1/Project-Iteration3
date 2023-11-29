package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.Banknote;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import item.HandleBulkyItem;
import item.RemoveItemController;
import powerutility.NoPowerException;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.Products;
import testingUtilities.Wallet;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import control.SelfCheckoutLogic;


@RunWith(Parameterized.class)
public class AddItemTests implements DollarsAndCurrency, CardPayment {
/*
 * make three types of station to test, we'll have to test all three types for each kind of test.
 * Maybe there's a fast way to plug in these objects than repeating a test method?
 */
public static SelfCheckoutStationBronze bronze;
public static SelfCheckoutStationSilver silver;
public static SelfCheckoutStationGold gold;

private AbstractSelfCheckoutStation station;
/*
 * Each station needs 1 logic
 */
SelfCheckoutLogic logic;

/*
 * listener objects
 */
public scannerListener scanWatch = new scannerListener();
public scaleListener scaleWatch = new scaleListener();
/*
 * Products? where are usharabs products?
 */
Products products = new Products();



@Rule
	public ExpectedException expectedMessage = ExpectedException.none();
@Parameterized.Parameters
public static Collection<AbstractSelfCheckoutStation[]> data() {
	//bronze
	bronze.resetConfigurationToDefaults();
	bronze.configureBanknoteDenominations(bankNoteDenominations);
	
	PowerGrid.engageUninterruptiblePowerSource();
	PowerGrid.instance().forcePowerRestore();
	SelfCheckoutStationBronze bronze = new SelfCheckoutStationBronze();
	bronze.plugIn(PowerGrid.instance());
	bronze.turnOn();
	
	
	
	//silver
	silver.resetConfigurationToDefaults();
	silver.configureBanknoteDenominations(bankNoteDenominations);
	
	
	PowerGrid.engageUninterruptiblePowerSource();
	PowerGrid.instance().forcePowerRestore();
	SelfCheckoutStationSilver silver = new SelfCheckoutStationSilver();
	silver.plugIn(PowerGrid.instance());
	silver.turnOn();
	
	//gold
	gold.resetConfigurationToDefaults();
	gold.configureBanknoteDenominations(bankNoteDenominations);
	
	PowerGrid.engageUninterruptiblePowerSource();
	PowerGrid.instance().forcePowerRestore();
	SelfCheckoutStationGold gold = new SelfCheckoutStationGold();
	gold.plugIn(PowerGrid.instance());
	gold.turnOn();
	
    return Arrays.asList(new AbstractSelfCheckoutStation[][] {
            { bronze },
            { silver  },
            { gold  },
            // Add more instances as needed
        });
}
public AddItemTests(AbstractSelfCheckoutStation station) {
    this.station = station;
}
	@Before
	public void testSetup () throws OverloadedDevice{
		

		/* First, create an instance of the SelfCheckoutStation (Bronze, Silver, Gold).
		 */
		//bronze
		station.resetConfigurationToDefaults();
		station.configureBanknoteDenominations(bankNoteDenominations);
		
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		
		/*
		 * Now that we have a station, we need to link the software to it. 
		 */
		
		logic = SelfCheckoutLogic.installOn(station);


		logic.session.enable();
	 
		logic.station.printer.addInk(300);
		logic.station.printer.addPaper(20);
		
		//I use custom bank notes from interface
		station.mainScanner.register(scanWatch);
		station.handheldScanner.register(scanWatch);
		station.scanningArea.register(scaleWatch);
		station.baggingArea.register(scaleWatch);
		
		
	}
	
/*
 * add item to scale
 */

	@Test
	public void scanItem() {
		for (int i =0;scanWatch.notify!="barcode has been scanned"; i++) {
		logic.station.handheldScanner.scan(products.beanBarcodeItem);}
		Assert.assertTrue (scanWatch.notify=="barcode has been scanned");
		scanWatch.notify="";
		for (int i =0;scanWatch.notify!="barcode has been scanned"; i++) {
		logic.station.mainScanner.scan(products.beanBarcodeItem);}
		Assert.assertTrue (scanWatch.notify=="barcode has been scanned");
	}
	
	@Test
	public void addItemToScaleTest() {
	
		
		logic.station.scanningArea.addAnItem(products.beanBarcodeItem);
		Assert.assertTrue (scaleWatch.notify=="theMassOnTheScaleHasChanged");
		logic.station.scanningArea.removeAnItem(products.beanBarcodeItem);
		Assert.assertTrue (scaleWatch.notify=="theMassOnTheScaleHasChanged");
		logic.station.baggingArea.addAnItem(products.beanBarcodeItem);
		Assert.assertTrue (scaleWatch.notify=="theMassOnTheScaleHasChanged");
		//logic.weightController.
	}
/*
 * overload scale then remove overloaded item
 */
@Test
public void addHugeItemToScaleAndRemoveTest() {
	scaleWatch.notify = "";
	logic.station.scanningArea.addAnItem(products.bigItem);
	Assert.assertTrue (scaleWatch.notify=="theMassOnTheScaleHasChanged");
	scaleWatch.notify = "";
	logic.station.scanningArea.removeAnItem(products.bigItem);
	Assert.assertTrue (scaleWatch.notify=="theMassOnTheScaleNoLongerExceedsItsLimit");
	scaleWatch.notify = "";
	logic.station.baggingArea.addAnItem(products.bigItem);
	Assert.assertTrue (scaleWatch.notify=="theMassOnTheScaleHasChanged");
	scaleWatch.notify = "";
	logic.station.baggingArea.removeAnItem(products.bigItem);
	Assert.assertTrue (scaleWatch.notify=="theMassOnTheScaleNoLongerExceedsItsLimit");
	//logic.weightController.
}
/*
 * add bulky item to cart?
 */
@Test
public void addBulkyItemToCart() {
	
	logic.session.BulkyItems = new ArrayList<Product>();
	logic.session.BulkyItems.add(products.beanBarcodedProduct);
	logic.session.Cart.add(products.beanBarcodedProduct);
	RemoveItemController remover= new RemoveItemController(logic.session, bronze);
	remover.removeItem(products.beanBarcodedProduct);
	//logic.session.(,products.beanBarcodedProduct);
	//logic.weightController.
}
/*
 * handle a bulky item
 */
@Test
public void handleBulkyItem() {
	
	HandleBulkyItem handler = new HandleBulkyItem();
	logic.session.BulkyItems = new ArrayList<Product>();
	logic.session.BulkyItems.add(products.bigProduct);
	logic.session.Cart.add(products.bigProduct);
	
	handler.doNotBagItem(logic.session, products.bigProduct);
	//logic.session.(,products.beanBarcodedProduct);
	//logic.weightController.
}

public class scannerListener implements BarcodeScannerListener{
	 public String notify ="";
		@Override
		public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
			this.notify= "enabled";
			
		}

		@Override
		public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
			this.notify= "disabled";
			
		}

		@Override
		public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
			this.notify= "turned On";
			
		}

		@Override
		public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
			this.notify= "turned off";
			
		}

		@Override
		public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
			this.notify= "barcode has been scanned";
			
		}
	}

public class scaleListener implements ElectronicScaleListener{
	String notify ="";
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		this.notify= "enabled";
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		this.notify= "disabled";
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		this.notify= "turnedOn";
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		this.notify= "turnedOff";
		
	}

	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		this.notify= "theMassOnTheScaleHasChanged";
		
	}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		this.notify= "theMassOnTheScaleHasExceededItsLimit";
		
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		this.notify= "theMassOnTheScaleNoLongerExceedsItsLimit";
		
	}}

}