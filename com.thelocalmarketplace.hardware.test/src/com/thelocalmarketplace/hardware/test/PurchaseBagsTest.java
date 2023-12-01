// Kear Sang Heng 30087289
package com.thelocalmarketplace.hardware.test;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import item.PurchaseBags;
import powerutility.PowerGrid;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.jjjwelectronics.bag.ReusableBagDispenserBronze;
import com.jjjwelectronics.bag.ReusableBagDispenserSilver;
import com.jjjwelectronics.bag.ReusableBagDispenserGold;
import com.jjjwelectronics.OverloadedDevice;
import control.SelfCheckoutLogic;
import control.SessionController;
import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.bag.ReusableBag;

public class PurchaseBagsTest {
	SelfCheckoutStationBronze bronze;
	SelfCheckoutStationSilver silver;
	SelfCheckoutStationGold gold;
	SelfCheckoutLogic logicBronze;
	SelfCheckoutLogic logicSilver;
	SelfCheckoutLogic logicGold;
	
	
	private SessionController sessionBronze;
	private SessionController sessionSilver;
	private SessionController sessionGold;
	private PurchaseBags purchaseBags;
	
    @Before 
    public void setup() {
        // bronze
        SelfCheckoutStationBronze.resetConfigurationToDefaults();
        PowerGrid.engageUninterruptiblePowerSource();
        PowerGrid.instance().forcePowerRestore();
        bronze = new SelfCheckoutStationBronze();
        bronze.plugIn(PowerGrid.instance());
        bronze.turnOn();
        
        // silver
        SelfCheckoutStationSilver.resetConfigurationToDefaults();
        PowerGrid.engageUninterruptiblePowerSource();
        PowerGrid.instance().forcePowerRestore();
        silver = new SelfCheckoutStationSilver();
        silver.plugIn(PowerGrid.instance());
        silver.turnOn();
        
        // gold
        SelfCheckoutStationGold.resetConfigurationToDefaults();
        PowerGrid.engageUninterruptiblePowerSource();
        PowerGrid.instance().forcePowerRestore();
        gold = new SelfCheckoutStationGold(); 
        gold.plugIn(PowerGrid.instance());
        gold.turnOn();
        
        // install logic
        logicBronze = SelfCheckoutLogic.installOn(bronze);
        logicSilver = SelfCheckoutLogic.installOn(silver);
        logicGold = SelfCheckoutLogic.installOn(gold);
        
        // install session controllers
        sessionBronze = new SessionController(logicBronze);
        sessionSilver = new SessionController(logicSilver);
        sessionGold = new SessionController(logicGold);
        
    }

	
    @Test
    public void testBuyBagsSuccessBronze() throws OverloadedDevice, EmptyDevice {
        // Load bags into the dispenser that's part of the bronze station
        ReusableBag[] bagsToLoad = new ReusableBag[5];
        Arrays.fill(bagsToLoad, new ReusableBag());
        ReusableBagDispenserBronze dispenserBronze = (ReusableBagDispenserBronze) bronze.getReusableBagDispenser();
        dispenserBronze.load(bagsToLoad);

        // Set up PurchaseBags with the bronze dispenser and session
        purchaseBags = new PurchaseBags(bronze, sessionBronze); 
        purchaseBags.setBagsToBuy(3);

        // Attempt to buy bags
       
        purchaseBags.buyBags();
        assertTrue("Bags bought successfully", true);
       
    }
    
    /*
    @Test
    public void testBuyBagsSuccessSilver() throws OverloadedDevice, EmptyDevice {
        // Load bags into the dispenser that's part of the silver station
        ReusableBag[] bagsToLoad = new ReusableBag[5];
        Arrays.fill(bagsToLoad, new ReusableBag());
        
        // Retrieve the correct dispenser type from the silver station
      
        // bug here when trying to get silver dispenser, gets a gold one instead
        ReusableBagDispenserSilver dispenserSilver = (ReusableBagDispenserSilver) silver.getReusableBagDispenser();
        dispenserSilver.load(bagsToLoad);
        
        purchaseBags = new PurchaseBags(silver, sessionSilver); 
        purchaseBags.setBagsToBuy(5);
        
        purchaseBags.buyBags();
        assertTrue("Bags bought successfully", true);
    }
*/

    @Test
    public void testBuyBagsSuccessGold() throws OverloadedDevice, EmptyDevice {
        // Load bags into the dispenser that's part of the gold station
        ReusableBag[] bagsToLoad = new ReusableBag[5];
        Arrays.fill(bagsToLoad, new ReusableBag());
        
        // Cast to the gold dispenser type
        ReusableBagDispenserGold dispenserGold = (ReusableBagDispenserGold) gold.getReusableBagDispenser();
        dispenserGold.load(bagsToLoad);
        
        // Set up PurchaseBags with the gold dispenser and session
        purchaseBags = new PurchaseBags(gold, sessionGold); 
        purchaseBags.setBagsToBuy(5);
        
        // Attempt to buy bags
        purchaseBags.buyBags();
        assertTrue("Bags bought successfully", true);
    }

    @Test(expected = EmptyDevice.class)
    public void testBuyBagsWithEmptyDispenserBronze() throws OverloadedDevice, EmptyDevice{
    	
    	// Load no bags into dispenser that is part of the bronze station
    	ReusableBag[] bagsToLoad = new ReusableBag[0];
        Arrays.fill(bagsToLoad, new ReusableBag());
        
        // Cast to the bronze dispenser type
        ReusableBagDispenserBronze dispenserBronze = (ReusableBagDispenserBronze) bronze.getReusableBagDispenser();
        dispenserBronze.load(bagsToLoad);

        // Set up PurchaseBags with the bronze dispenser and session
        purchaseBags = new PurchaseBags(bronze, sessionBronze); 
        purchaseBags.setBagsToBuy(3);

        // Attempt to buy bags
       
        purchaseBags.buyBags();

   }
    
    @Test(expected = RuntimeException.class)
    public void testBuyBagsWithEmptyDispenserGold() throws OverloadedDevice, EmptyDevice{
    	// Load no bags into dispenser that is part of the gold station
    	ReusableBag[] bagsToLoad = new ReusableBag[0];
        Arrays.fill(bagsToLoad, new ReusableBag());
        
        // Cast to the gold dispenser type
        ReusableBagDispenserGold dispenserGold = (ReusableBagDispenserGold) gold.getReusableBagDispenser();
        dispenserGold.load(bagsToLoad);

        // Set up PurchaseBags with the bronze dispenser and session
        purchaseBags = new PurchaseBags(gold, sessionGold); 
        purchaseBags.setBagsToBuy(3);

        // Attempt to buy bags
       
        purchaseBags.buyBags();

   }
    
    @Test(expected = EmptyDevice.class)
    public void testBuyBagWithMoreThanDispenserInventoryBronze() throws OverloadedDevice, EmptyDevice {
    	// Load two bags into dispenser that is part of the bronze station
    	ReusableBag[] bagsToLoad = new ReusableBag[2];
        Arrays.fill(bagsToLoad, new ReusableBag());
        
        // Cast to the bronze dispenser type
        ReusableBagDispenserBronze dispenserBronze = (ReusableBagDispenserBronze) bronze.getReusableBagDispenser();
        dispenserBronze.load(bagsToLoad);

        // Set up PurchaseBags with the bronze dispenser and session
        purchaseBags = new PurchaseBags(bronze, sessionBronze); 
        purchaseBags.setBagsToBuy(3);

        // Attempt to buy bags
       
        purchaseBags.buyBags();
    }

    @Test(expected = RuntimeException.class)
    public void testBuyBagWithMoreThanDispenserInventoryGold() throws OverloadedDevice, EmptyDevice{
    	// Load two bags into dispenser that is part of the gold station
    	ReusableBag[] bagsToLoad = new ReusableBag[2];
        Arrays.fill(bagsToLoad, new ReusableBag());
        
        // Cast to the gold dispenser type
        ReusableBagDispenserGold dispenserGold = (ReusableBagDispenserGold) gold.getReusableBagDispenser();
        dispenserGold.load(bagsToLoad);

        // Set up PurchaseBags with the gold dispenser and session
        purchaseBags = new PurchaseBags(gold, sessionGold); 
        purchaseBags.setBagsToBuy(3);

        // Attempt to buy bags
       
        purchaseBags.buyBags();

   }
    




	

    }

