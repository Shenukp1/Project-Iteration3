package testingUtilities;

import java.util.Calendar;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.hardware.external.CardIssuer;

public interface CardPayment {
	CardIssuer creditcard= new CardIssuer("credit", 500);
	CardIssuer debitcard= new CardIssuer("debit", 500);

	Card otherCreditCard = new Card("credit", "555", "Donkey Kong", "312");
	Card otherDebitCard = new Card("debit", "555", "Donkey Kong", "312");
	
}
