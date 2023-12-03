package com.thelocalmarketplace.hardware.test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import control.PredictIssueController;
import control.SelfCheckoutLogic;
import control.SessionController;
import control.WeightController;
import item.BarcodeController;
import item.PrintController;
import payment.BanknoteController;
import payment.CardController;
import payment.CoinController;

public class SelfCheckoutLogicStub extends SelfCheckoutLogic {
	// Software simulation of SelfCheckoutStation hardware
	public AbstractSelfCheckoutStation station;

	// Controller to manage payments
	public CoinController coinController;

	// Controller to manage banknote payments
	public BanknoteController banknoteController;

	// Controller to manage credit card payments
	public CardController creditController;

	// Controller to manage handheld barcode scans
	public BarcodeController barcodeController;

	// Controller to manage session
	public SessionController session;

	// Controller to manage scale weight
	public WeightController weightController;

	// Controller to manage printing
	public PrintController printController;

	// Controller to manage predicting issues
	public PredictIssueController predictController;

	// Card Issuer
	public CardIssuer cardIssuer;
	
	public int linesUsed;

	/**
	 * This method links our software to our hardware (simulation) and initializes
	 * all the controllers that we need.
	 * 
	 * @param scs The AbstractSelfCheckoutStation hardware we need to link it to
	 *            (currently simulated when a customer presses "Start Session")
	 * @return the instance of the software logic
	 */

	public static SelfCheckoutLogicStub installOn(AbstractSelfCheckoutStation scs) {
		return new SelfCheckoutLogicStub(scs);
	}

	/**
	 * Every customer session will have one logic instance.
	 * 
	 * @param scs The SelfCheckoutStation hardware we need to link it to (currently
	 *            simulated when a customer presses "Start Session")
	 */
	public SelfCheckoutLogicStub(AbstractSelfCheckoutStation scs) {
		super(scs);
		station = scs;

		session = new SessionController(this);

		barcodeController = new BarcodeController(session, scs);
		weightController = new WeightController(session, scs);
		banknoteController = new BanknoteController(session, scs);
		coinController = new CoinController(session, scs);
		// creditController = new CardController(session, scs, cardIssuer);
		printController = new PrintController(session, scs);
		predictController = new PredictIssueController(session, scs, linesUsed);
		// Disable banknote insertion slot so the customer does not insert banknotes
		// before going to the payment page.
		scs.banknoteInput.disable();
		// Same for the coin insertion slot
		scs.getCoinSlot().disable();
		// Same for the debit/credit card reader
		scs.getCardReader().disable();
	}
}
