package payment;

import java.math.BigDecimal;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.NoCashAvailableException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

import control.SessionController;
import item.PrintController;

public class ChangeController {
    
	/**
     * Checks if all items have been paid for. Displays change due if payment is more than total.
     * Displays payment due if payment was less than total. Prints cart details if payment = total.
     * @param NewCartTotal The new total amount of the cart after a certain payment was made
     */
    public static void checkAllItemPaid(AbstractSelfCheckoutStation station, SessionController session, 
    										BigDecimal NewCartTotal, String changeType) {
    	
    	if (NewCartTotal.compareTo(BigDecimal.ZERO) < 0) { 						// Payment > CartTotal
            System.out.println("Change due: " + NewCartTotal.abs()); 			// Display the change 
            dispenseChange(station, NewCartTotal, changeType);					// Dispense the change
            PrintController.printReceipt(station, session);
        } 
    	else if (NewCartTotal.compareTo(BigDecimal.ZERO) == 0) { 				// Payment = cartTotal
            System.out.println("Printing receipt...");
            PrintController.printReceipt(station, session);
            // CLEAN UP CUSTOMER SESSION (LATER ITERATION)
        } else {
        	System.out.println("Amount due: " + NewCartTotal);					// Display amount remaining
        }
    }
    
	/**
	 * This function dispenses the coins/banknotes according to the change
	 * required to be given to the customer. Loops through the denominations
	 * available in the SelfCheckoutStation and checks how much of each
	 * denomination to dispense.
	 * @param sco
	 * 			the self checkout station to dispense the notes from
	 * @param changeAmount
	 * 			the amount of change that has to be given
	 * @param changeType
	 * 			the type of change to give i.e coins or banknotes
	 */
	public static void dispenseChange(AbstractSelfCheckoutStation sco, BigDecimal changeAmount, String changeType) {
		BigDecimal[] denominations;
		if (changeType == "banknote") {
			denominations = sco.banknoteDenominations;									// Get banknote denominations
		}
		else {	
			denominations = new BigDecimal[(sco.coinDenominations).size()];
        	(sco.coinDenominations).toArray(denominations);								// Get coin denominations
		}
		
        for (BigDecimal denomination : denominations) {									// For every denomination
            if (denomination.compareTo(changeAmount) >= 0) {							// If denomination >= changeAmount
                int count = changeAmount.divide(denomination).intValue();				// Get quotient
                while (count > 0) {														// Emit that many number of banknotes/coins
	                try {																// for that denomination
	                	if (changeType == "banknote") {
	                		sco.banknoteDispensers.get(denomination).emit();
	                	}
	                	else
	                		sco.coinDispensers.get(denomination).emit();
					} catch (NoCashAvailableException | DisabledException | CashOverloadException e) {
						e.printStackTrace();
					};
					count--;
                }
                changeAmount.subtract(denomination.multiply(new BigDecimal(count)));	// Reduce change due by the amount dispensed
            }
        }
        if (changeType == "banknote")													// Dispense accumulated banknotes in the end
        	sco.banknoteOutput.dispense();
    }
}

