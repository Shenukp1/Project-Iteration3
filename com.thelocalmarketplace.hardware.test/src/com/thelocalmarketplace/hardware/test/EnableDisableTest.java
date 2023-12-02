package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import attendant.EnableDisable;
import control.SelfCheckoutLogic;
import powerutility.PowerGrid;

public class EnableDisableTest {

	SelfCheckoutStationBronze bronze;
	SelfCheckoutStationSilver silver;
	SelfCheckoutStationGold gold;

	SelfCheckoutLogic logicBronze;
	SelfCheckoutLogic logicSilver;
	SelfCheckoutLogic logicGold;

	private EnableDisable enableDisable;

	@Before
	public void setup() {
		bronze.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		bronze = new SelfCheckoutStationBronze();
		bronze.plugIn(PowerGrid.instance());
		bronze.turnOn();
		logicBronze = SelfCheckoutLogic.installOn(bronze);

		silver.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		silver = new SelfCheckoutStationSilver();
		silver.plugIn(PowerGrid.instance());
		silver.turnOn();
		logicSilver = SelfCheckoutLogic.installOn(silver);

		gold.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		gold = new SelfCheckoutStationGold();
		gold.plugIn(PowerGrid.instance());
		gold.turnOn();
		logicGold = SelfCheckoutLogic.installOn(gold);
	
		
		enableDisable = new EnableDisable("password123", bronze); 
		// default is disabled
		enableDisable.disableStation("password123");
	}// Assuming bronze station for testing


	 @Test
	    public void testEnableStationWithCorrectPassword() {
	        assertTrue(enableDisable.enableStation("password123"));
	        assertEquals(EnableDisable.StationStatus.ENABLED, enableDisable.getStatus());
	    }

	    @Test
	    public void testEnableStationWithIncorrectPassword() {
	        assertFalse(enableDisable.enableStation("wrongpassword"));
	        assertEquals(EnableDisable.StationStatus.DISABLED, enableDisable.getStatus());
	    }

	    @Test
	    public void testDisableStationWithCorrectPassword() {
	    	enableDisable.enableStation("password123");
	        assertTrue(enableDisable.disableStation("password123"));
	        assertEquals(EnableDisable.StationStatus.DISABLED, enableDisable.getStatus());
	    }

	    @Test
	    public void testDisableStationWithIncorrectPassword() {
	    	enableDisable.enableStation("password123");
	        assertFalse(enableDisable.disableStation("wrongpassword"));
	        assertEquals(EnableDisable.StationStatus.ENABLED, enableDisable.getStatus());
	    }
	    
	    @Test
	    public void testEnableWhenAlreadyEnabled() {
	    	enableDisable.enableStation("password123");
	    	assertFalse(enableDisable.enableStation("password123"));
		    assertEquals(EnableDisable.StationStatus.ENABLED, enableDisable.getStatus());
	    	
	    }
	    
	    @Test
	    public void testDisableWhenAlreadyDisabled() {
	    	assertFalse(enableDisable.disableStation("password123"));
		    assertEquals(EnableDisable.StationStatus.DISABLED, enableDisable.getStatus());
	    	
	    }

}