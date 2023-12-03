package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;

import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.Banknote;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.test.AddItemTests.scaleListener;
import com.thelocalmarketplace.hardware.test.AddItemTests.scannerListener;

import powerutility.NoPowerException;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.DollarsAndCurrency;
import testingUtilities.LoadProductDatabases;
import testingUtilities.Products;
import testingUtilities.Wallet;
import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import control.SelfCheckoutLogic;
import item.AddItemBarcode;
import item.AddItemPLU;
import item.AddItemText;

@RunWith(Parameterized.class)
public class RemoveItemTest implements DollarsAndCurrency, CardPayment, LoadProductDatabases {
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
public RemoveItemTest(AbstractSelfCheckoutStation station) {
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
	/*	station.getMainScanner().register(scanWatch);
		station.getHandheldScanner().register(scanWatch);
		station.getScanningArea().register(scaleWatch);
		station.getBaggingArea().register(scaleWatch);
		*/
		//productsNew= new LoadProductDatabases();
	}
	
/*
 * remove an item that isn't there
 */

	@Test (expected = InvalidArgumentSimulationException.class)
	public void removeEmptyItem() throws DisabledException, CashOverloadException, EmptyDevice, OverloadedDevice {
	
	
		logic.station.getScanningArea().enable();
		
		
		logic.station.getScanningArea().removeAnItem(beans.barcodedItem);
	
	}

	@Test
	public void removeItemAfterAddingSaidItem() throws DisabledException, CashOverloadException, EmptyDevice, OverloadedDevice {
		logic.station.getMainScanner().enable();
		AddItemBarcode.AddItemFromBarcode(logic.session, beans.itemBarcode);
		logic.station.getMainScanner().scan(beans.barcodedItem);
	
		logic.station.getScanningArea().enable();
		logic.station.getScanningArea().addAnItem(beans.barcodedItem);
		
		logic.station.getScanningArea().removeAnItem(beans.barcodedItem);
		
	
	}
	
	
}
