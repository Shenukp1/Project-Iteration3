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

import com.thelocalmarketplace.hardware.external.CardIssuer;

import control.SelfCheckoutLogic;
import payment.PaymentCardController;
/*
 * this class is a stub class for a card
 */
public class CardConstructor implements CardPayment{

	/*
	 * we need a calendar to use cardIssuer
	 */
		Calendar calendar = Calendar.getInstance();
		
		PaymentCardController tempCardClass;
		CardIssuer temp; 
		  
		  CardConstructor(SelfCheckoutLogic logic){
			 //a calendar to show expiry date of card
			  calendar.add(Calendar.DAY_OF_MONTH, 7);
			  calendar.add(Calendar.YEAR, 2024);
			  calendar.add(Calendar.MONTH, 02);
			  //seems like this needs some static field to work in tests. strange
				 temp = new CardIssuer("TD trust", 12321);
			  //adds a card issuer to use a card from. 
		  logic.cardIssuer = new CardIssuer("TD trust", 12321);
		  		//adds card data to database for this card
			logic.cardIssuer.addCardData(otherCreditCard.number, otherCreditCard.cardholder,
					calendar,otherCreditCard.cvv , 1000);
				//registers listener on logic sco system
			logic.creditController =new PaymentCardController(logic.session, logic.station, temp );
	
			  
		  }
		  
}
