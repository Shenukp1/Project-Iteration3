/*Group P3-6***
Andy Tang 10139121
Ayman Inayatali Momin 30192494
Darpal Patel 30088795
Dylan Dizon 30173525
Ellen Bowie 30191922
Emil Huseynov 30171501
Ishita Udasi 30170034
Jason Very 30222040
Jesse Leinan 00335214
Joel Parker 30021079
Kear Sang Heng 30087289
Khadeeja Abbas 30180776
Kian Sieppert 30134666
Michelle Le 30145965
Raja Muhammed Omar 30159575
Sean Gilluley 30143052
Shenuk Perera 30086618
Simrat Virk 30000516
Sina Salahshour 30177165
Tristan Van Decker 30160634
Usharab Khan 30157960
YiPing Zhang 30127823*/
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
