package testingUtilities;

import java.util.Calendar;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.hardware.external.CardIssuer;

public interface CardPayment {

	boolean tapEnabled=true;
	boolean tapDisabled=false;
	
	boolean hasChip=true;
	boolean noChip=false;
	
	/*
	 * card issuer and card might be related, 
	 */
	CardIssuer creditcard= new CardIssuer("TD", 500);
	CardIssuer debitcard= new CardIssuer("CIBC", 500);
	
	
	/*
	 * copy the initializing parameters to add card data so a card can pay.
	 */
	
	Card otherCreditCard = new Card("credit", "555", "Donkey Kong", "312", "911", tapEnabled, hasChip);
	Card otherDebitCard = new Card("debit", "555", "Donkey Kong", "312", "911", tapEnabled, hasChip);
	
}
