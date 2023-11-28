package control;

import com.tdc.banknote.AbstractBanknoteDispenser;

/**
 * This class predicts if the banknote dispenser is low in storage
 */
public class PredictLowBanknote extends AbstractPredictIssue {

	/**
	 * Declaring variables
	 */
	private AbstractBanknoteDispenser banknoteDispenser;
	private String issue;
	private boolean issueExists;

	public PredictLowBanknote(String stationNumber, boolean sessionStarted,
			AbstractBanknoteDispenser banknoteDispenser) {
		
		/**
		 * Setting up the parameters for the super class
		 */
		super(stationNumber, sessionStarted);

		/**
		 * Initializing variables
		 */
		this.banknoteDispenser = banknoteDispenser;

	}

	/**
	 * This is the logic for predicting when banknotes might be low within the
	 * banknote dispenser. 5 is just a number that indicates if there are 5 or less 
	 * banknotes inside, send a signal.
	 */
	private void predictLowBanknote() {
		if (banknoteDispenser.size() <= 5) {
			issue = "Banknote Dispenser is almost empty";
			issueExists = true;
		} else {
			issueExists = false;
		}
	}
	
	/**
	 * This public method returns a boolean of whether the coin storage is full or
	 * not
	 * 
	 * @return issueExists
	 */
	public boolean banknoteAlmostEmpty() {
		return issueExists;
	}
}
