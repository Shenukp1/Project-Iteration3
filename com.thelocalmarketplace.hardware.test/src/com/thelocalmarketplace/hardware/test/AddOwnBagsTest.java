package com.thelocalmarketplace.test;

import static org.junit.Assert.*;

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
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import control.SelfCheckoutLogic;
import control.SessionController;
import item.AddOwnBags;
import junit.framework.Assert;
import powerutility.PowerGrid;

import testingUtilities.Products;


public class AddOwnBagsTest {


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
	public ElectronicScaleObserver scaleWatcher = new ElectronicScaleObserver();

	public BarcodedItem bag;
	public Item bags;
	public Mass mass;
	public Barcode barcode;
	public Products products;


	private SelfCheckoutLogic logic;
	private AddOwnBags addOwnBags;
  private AbstractSelfCheckoutStation abstractSelfCheckoutStation;
  private ElectronicScaleBronze scale;


	@Before
	public void setup() {
		//SelfCheckoutStation = new SelfCheckoutStation();
		//logic = new SelfCheckoutLogic();
		//addOwnBags= new AddOwnBags();

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
		bag = new BarcodedItem(barcode,mass);
		bags = bag;

		scale = new ElectronicScaleBronze();

		logicBronze.station.baggingArea.enable();
		logicBronze.station.baggingArea.turnOn();
		logicBronze.station.baggingArea.addAnItem(products.bag);
		addOwnBags = new AddOwnBags(logicBronze);
		logicBronze.station.baggingArea.register(scaleWatcher);
		
	  }

	//tests when user adds their own bags correctly
	@Test
	public void testSuccessfullyAddOwnBags() throws OverloadedDevice {
		try {

	      	logicBronze.session.setCartWeight(0);
	      	addOwnBags.addOwnBags(logicBronze.station);


	      } catch (OverloadedDevice e) {

	      }


	}

	//user chose to add own bags, but no bags were actually added
	@Test
	public void testNoBagsAdded() {
		try {
			// Simulate the scenario where no bags are added


          ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
      	System.setOut(new PrintStream(outputStreamCaptor));

      	logicBronze.session.setCartWeight(0);

      	addOwnBags.addOwnBags(logicBronze.station);


			String output = outputStreamCaptor.toString().trim();

			//Test if expected message is displayed
			equals(output);
			//reset

      } catch (OverloadedDevice e) {

      }
	}

	//bags added are over expected weight
	@Test
	public void testBagsOverWeight() {
		try {
			logicBronze.session.setCartWeight(101);

	        // Simulate the scenario where added are overweight
	        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	      	System.setOut(new PrintStream(outputStreamCaptor));

	      	logicBronze.session.setCartWeight(101);
	      	addOwnBags.addOwnBags(logicBronze.station);


	        String output = outputStreamCaptor.toString().trim();

	        //Test if expected message is displayed

	        //Assert.assertEquals("Bags too heavy, notifying attendant for assistance.",output));

	        assertTrue(scaleWatcher.eOrd == "over weight");
	        //reset

	      } catch (OverloadedDevice e) {

	      }
  }

	//session not started
	@Test
	public void testSessionNotStarted() {

	}

	//system is disabled
	@Test
	public void testSystemDisabled() {
		logicBronze.station.baggingArea.disable();

		ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
      	System.setOut(new PrintStream(outputStreamCaptor));


        String output = outputStreamCaptor.toString().trim();

        //Test if expected message is displayed
        assertTrue(scaleWatcher.eOrd=="disabled");
        //reset
	}

	@Test
	public void testWeightDiscrepencyNotNoted() {

	}
}
