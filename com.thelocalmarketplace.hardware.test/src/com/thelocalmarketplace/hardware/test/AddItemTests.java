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

import item.AddItemBarcode;
import item.AddItemCatalogue;
import item.AddItemPLU;
import item.AddItemText;
import item.HandleBulkyItem;
import item.RemoveItemController;
import powerutility.NoPowerException;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.InventoryProductGenerator;
import testingUtilities.LoadProductDatabases;
import testingUtilities.Products;
import testingUtilities.Wallet;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import control.SelfCheckoutLogic;


@RunWith(Parameterized.class)
public class AddItemTests implements DollarsAndCurrency, CardPayment, LoadProductDatabases {
/*
 * make three types of station to test, we'll have to test all three types for each kind of test.
 * Maybe there's a fast way to plug in these objects than repeating a test method?
 */
public static SelfCheckoutStationBronze bronze;
public static SelfCheckoutStationSilver silver;
public static SelfCheckoutStationGold gold;

public AddItemPLU addPluItem;
public AddItemText addTextItem;
public AddItemText addCatalogueItem;

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
//Products products = new Products();
//LoadProductDatabases productsNew=new LoadProductDatabases();


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
	 
		logic.station.getPrinter().addInk(300);
		logic.station.getPrinter().addPaper(20);
		
		//I use custom bank notes from interface
		station.getMainScanner().register(scanWatch);
		station.getHandheldScanner().register(scanWatch);
		station.getScanningArea().register(scaleWatch);
		station.getBaggingArea().register(scaleWatch);
		
		
		//productsNew= new LoadProductDatabases();
	}
	
/*
 * add item to scale
 */

	@Test
	public void scanItem() {
		for (int i =0;scanWatch.notify!="barcode has been scanned"; i++) {
		logic.station.getHandheldScanner().scan(beans.barcodedItem);}
		Assert.assertTrue (scanWatch.notify=="barcode has been scanned");
		scanWatch.notify="";
		for (int i =0;scanWatch.notify!="barcode has been scanned"; i++) {
		logic.station.getMainScanner().scan(beans.barcodedItem);}
		Assert.assertTrue (scanWatch.notify=="barcode has been scanned");
	}

	@Test
	public void addItemToScaleTest() {
	
		
		logic.station.getScanningArea().addAnItem(bacon.barcodedItem);
		Assert.assertTrue (scaleWatch.notify=="theMassOnTheScaleHasChanged");
		//logic.weightController.
	}
/*
 * overload scale then remove overloaded item
 */
@Test
public void addHugeItemToScaleAndRemoveTest() {
	scaleWatch.notify = "";
	logic.station.getScanningArea().addAnItem(bigProduct.barcodedItem);
	Assert.assertTrue (scaleWatch.notify=="theMassOnTheScaleHasChanged"&&
			scaleWatch.exceedsLimit=="theMassOnTheScaleHasExceededItsLimit");
	//scaleWatch.notify = "";
	logic.station.getScanningArea().removeAnItem(bigProduct.barcodedItem);
	Assert.assertTrue (scaleWatch.exceedsLimit=="theMassOnTheScaleNoLongerExceedsItsLimit");
	scaleWatch.notify = "";
}
@Test
public void addItembarcodeScanNew() {
		//AddItemBarcode aib;
	
		//logic.station.getScanningArea().addAnItem(beer.barcodedItem);
		Assert.assertEquals("Success: Product added to cart",
				AddItemBarcode.AddItemFromBarcode(logic.session, bacon.itemBarcode));
}
@Test
public void addItemfromCatalogue() {
		
	
		//logic.station.getScanningArea().addAnItem(beer.barcodedItem);
		Assert.assertEquals("Success: Product added to cart",
				AddItemCatalogue.AddItemFromCatalogue(logic.session,
				beer.barcodedProduct, beer.bigDecimalMass));
}
@Test
public void addTextItem() {
		
	
		//logic.station.getScanningArea().addAnItem(beer.barcodedItem);
		Assert.assertEquals("Success: Product added to cart", 
				AddItemText.AddItemFromText(logic.session, "beer", 
				beer.bigDecimalMass));
}
//shows there's milk in inventory
@Test
public void testInventoryItem() {
	Assert.assertTrue(100== ProductDatabases.INVENTORY.get(milk.barcodedProduct));}
@Test
public void addPluItem() {
	logic.station.getScanningArea().addAnItem(milk.barcodedItem);
		Assert.assertEquals("Success: Product added to cart", AddItemPLU.AddItemFromPLU(logic.session, 
				milk.pluCode,milk.bigDecimalMass));
}
/*
 * add bulky item to cart?
 */
@Test
public void addBulkyItemToCart() {
	
	logic.session.BulkyItems = new ArrayList<Product>();
	logic.session.BulkyItems.add(beans.barcodedProduct);
	logic.session.Cart.add(beans.barcodedProduct);
	RemoveItemController remover= new RemoveItemController(logic.session, bronze);
	remover.removeItem(beans.barcodedProduct);
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
	logic.session.BulkyItems.add(bigProduct.barcodedProduct);
	logic.session.Cart.add(bigProduct.barcodedProduct);
	
	handler.doNotBagItem(logic.session, bigProduct.barcodedProduct);
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
	String exceedsLimit="";
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
		this.exceedsLimit= "theMassOnTheScaleHasExceededItsLimit";
		
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		this.exceedsLimit= "theMassOnTheScaleNoLongerExceedsItsLimit";
		
	}}

}
