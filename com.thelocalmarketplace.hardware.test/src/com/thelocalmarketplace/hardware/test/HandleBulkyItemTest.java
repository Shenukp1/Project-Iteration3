package com.thelocalmarketplace.hardware.test;

import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;

import static org.junit.Assert.*;

import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.ElectronicScaleBronze;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import control.SelfCheckoutLogic;
import control.SessionController;
import item.AddOwnBags;
import item.HandleBulkyItem;
import powerutility.PowerGrid;

import testingUtilities.Products;

public class HandleBulkyItemTest {
	
	SelfCheckoutStationBronze bronze;
	SelfCheckoutStationSilver silver;
	SelfCheckoutStationGold gold;
	/*
	 * each station needs its own logic instance for setup.
	 */
	SelfCheckoutLogic logicBronze;
	SelfCheckoutLogic logicSilver;
	SelfCheckoutLogic logicGold;
	
	public BarcodedItem bag;
	public Item bags;
	public Mass mass;
	public Barcode barcode;
	public Products products;
	public Product product;
	
	
	private SelfCheckoutLogic logic;
	private AddOwnBags addOwnBags;
  private AbstractSelfCheckoutStation abstractSelfCheckoutStation;
  private ElectronicScaleBronze scale;
  private SessionController session;
  private BarcodedProduct barcodedProduct;
  private static double d1 = 72.0;
  private static long l1=0;
  
  
  
 
	@Before
	public void setup() {
		
		/* First, create an instance of the SelfCheckoutStation (Bronze, Silver, Gold).
		 */
		//bronze
		bronze.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationBronze bronze = new SelfCheckoutStationBronze();
		bronze.plugIn(PowerGrid.instance());
		bronze.turnOn();
		
		//silver
		silver.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationSilver silver = new SelfCheckoutStationSilver();
		silver.plugIn(PowerGrid.instance());
		silver.turnOn();
		
		//gold
		gold.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationGold gold = new SelfCheckoutStationGold();
		gold.plugIn(PowerGrid.instance());
		gold.turnOn();
		
		/*
		 * Now that we have a station, we need to link the software to it.
		 */
		
		logicBronze = SelfCheckoutLogic.installOn(bronze);
		logicSilver = SelfCheckoutLogic.installOn(silver);
		logicGold = SelfCheckoutLogic.installOn(gold);
		
		
		
		
		
		products = new Products();
		mass = new Mass(10);
		barcode = new Barcode(new Numeral[] { Numeral.one });
		barcodedProduct = new BarcodedProduct(barcode, "The first test product", 1, d1);
		bag = new BarcodedItem(barcode,mass);
		bags = bag;
		session = new SessionController(logicBronze);
	
		//product = new Product(l1,true);
	
		
		
		scale = new ElectronicScaleBronze();
		
		logicBronze.station.getBaggingArea().enable();
		logicBronze.station.getBaggingArea().turnOn();
		logicBronze.station.getBaggingArea().addAnItem(products.bag);
		
	  }
    //Simulates valid session for coverage
	@Test
	public void validItem() {
		
		session.start();
		session.Cart.add(product);
        HandleBulkyItem.doNotBagItem(session,barcodedProduct);
		
	}
	
	//Simulates empty cart during session, expects System.err 
	@Test 
	public void noProductInCart() {
		session.start();
        HandleBulkyItem.doNotBagItem(session,barcodedProduct);
	}
	
	//Simulates product that was requested to not be bagged having not been scanned yet,
	//expects System.err
	@Test
	public void requestedItemMissing() {
		session.start();
		session.Cart.add(barcodedProduct);
        HandleBulkyItem.doNotBagItem(session,barcodedProduct);
	}
	
	
	
}
