package GUI;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Rule; 
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.Banknote;
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
	 
		logicBronze.station.printer.addInk(300);
		logicBronze.station.printer.addPaper(300);
		
		//I use custom bank notes from interface
		
		
	}
	
/*
 * add item to scale
 */
@Test
	public void Starttest() {
	
	JFrame start = logicGold.station.screen.getFrame();
	
	logicGold.station.screen.setVisible(true);
	start.getContentPane().removeAll();
	start.setLayout(new BorderLayout());
	
	StartScreen checkoutPanel = new StartScreen(logicGold);
	
	
}
}
		