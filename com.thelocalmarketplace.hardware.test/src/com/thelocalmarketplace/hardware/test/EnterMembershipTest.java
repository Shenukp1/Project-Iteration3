package com.thelocalmarketplace.hardware.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import control.SelfCheckoutLogic;
import payment.CardController;
import powerutility.PowerGrid;
import testingUtilities.CardPayment;
import testingUtilities.Products;

public class EnterMembershipTest implements CardPayment{
	private SelfCheckoutStationGold gold;
	private SelfCheckoutStationBronze bronze;
	private SelfCheckoutStationSilver silver;
	private SelfCheckoutLogic logicBronze;
	private SelfCheckoutLogic logicSilver;
	private SelfCheckoutLogic logicGold;
	Products products = new Products();
	Calendar calendar = Calendar.getInstance();
	CardController tempCardClass;
	CardIssuer temp;
	
	@Before
	public void setUp() throws Exception {
		bronze.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationBronze bronzeStation = new SelfCheckoutStationBronze();
		bronzeStation.plugIn(PowerGrid.instance());
		bronzeStation.turnOn();
		
		silver.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationSilver silverStation = new SelfCheckoutStationSilver();
		silverStation.plugIn(PowerGrid.instance());
		silverStation.turnOn();
		
		gold.resetConfigurationToDefaults();
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		SelfCheckoutStationGold goldStation = new SelfCheckoutStationGold();
		goldStation.plugIn(PowerGrid.instance());
		goldStation.turnOn();
		
		logicBronze = SelfCheckoutLogic.installOn(bronzeStation);
		logicSilver = SelfCheckoutLogic.installOn(silverStation);
		logicGold = SelfCheckoutLogic.installOn(goldStation);

	}



	@Test
	public void testSwipeMembershipCard() throws IOException {
		Card memberCard = new Card("membership", "00000", "John", null, null, false, false);
		logicGold.station.getCardReader().enable();
		logicGold.station.getCardReader().swipe(memberCard);
		//logicSilver.station.cardReader.enable();
		//logicSilver.station.cardReader.swipe(memberCard);
	}
	
	@Test
	public void testSwipeInformationCorrect() throws IOException {
		Card memberCard = new Card("membership", "00000", "John", null, null, false, false);
		logicGold.station.getCardReader().enable();
		logicGold.station.getCardReader().swipe(memberCard);
		assertEquals(logicGold.session.membership, memberCard.number );
	}
	
	@Test
	public void testScanMembershipCard() throws IOException {
		logicGold.session.enable();
		logicGold.station.getHandheldScanner().scan(products.membershipCard);
	}
	
	@Test
	public void testScanInformationCorrect() throws IOException {
		logicGold.session.enable();
		logicGold.station.getHandheldScanner().scan(products.membershipCard);
		assertEquals(logicGold.session.membership, products.beanBarcode.toString() );
	}

}
