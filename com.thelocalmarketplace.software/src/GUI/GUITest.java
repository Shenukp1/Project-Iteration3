package GUI;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.Banknote;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.ProductDatabases;


import item.HandleBulkyItem;
import item.RemoveItemController;
import powerutility.NoPowerException;
import powerutility.PowerGrid;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import control.SelfCheckoutLogic;

public class GUITest  {
/*
 * make three types of station to test, we'll have to test all three types for each kind of test.
 * Maybe there's a fast way to plug in these objects than repeating a test method?
 */
SelfCheckoutStationBronze bronze;
SelfCheckoutStationSilver silver;
SelfCheckoutStationGold gold;
/*
 * each station needs its own logic instance for setup.
 */
SelfCheckoutLogic logicBronze;
SelfCheckoutLogic logicSilver;
SelfCheckoutLogic logicGold;
public Numeral[] beansNumeral = new Numeral [4];
public Barcode beanBarcode;
public BarcodedItem beanBarcodeItem;
public BarcodedItem bigItem; //to overload scale
public BarcodedProduct beanBarcodedProduct;
public BarcodedProduct bigProduct;
public Mass beansMass;
public Mass bigItemMass;
public Item Beans;
public BigInteger   bigIBeanMass = new BigInteger("500");
public BigInteger excessiveMass= new BigInteger("5000000000000");

/*wallet to add banknotes to a slot. may not be needed because of Dollars And Currency Utility
 *  
 */
/*
 * 
 */
/*
 * Products? where are usharabs products?
 */

@Rule
	public ExpectedException expectedMessage = ExpectedException.none();

	@Before
	public void testSetup () throws OverloadedDevice{
		
	
		bronze.resetConfigurationToDefaults();
		bronze.configureScaleSensitivity(0.0001);
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
		logicBronze.session.enable();
		logicBronze.station.getPrinter().addInk(300);
		logicBronze.station.getPrinter().addPaper(300);
		
		
		
		beansNumeral [0]=Numeral.one;
		beansNumeral [1]=Numeral.two;
		beansNumeral [2]=Numeral.three;
		beansNumeral [3]=Numeral.four;
		
		beansMass= new Mass(bigIBeanMass);
		beanBarcode=new Barcode (beansNumeral);
		beanBarcodeItem= new BarcodedItem (beanBarcode, beansMass);
		beanBarcodedProduct=new BarcodedProduct(beanBarcode, "beans", 5,bigIBeanMass.intValue());
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(beanBarcode, beanBarcodedProduct);
		
	}
	
/*
 * add item to scale
 */
@Test
	public void Starttest() throws InterruptedException {

		/*
		 * // Remove this commented section to reformat as a test
		 * 
		 * logicGold.station.getScreen().setVisible(true);
		 * start.getContentPane().removeAll(); start.setLayout(new BorderLayout());
		 * 
		 * System.out.print(logicGold); StartScreen checkoutPanel = new
		 * StartScreen(logicGold);
		 * System.out.println(logicGold.station.getScreen().getFrame());
		 * Thread.sleep(60000);
		 */
	
}
}



		