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
