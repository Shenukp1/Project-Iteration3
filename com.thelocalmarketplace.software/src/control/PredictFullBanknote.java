package control;

import com.tdc.banknote.BanknoteStorageUnit;

/**
 * This class predicts if the bank note storage unit is full
 */
public class PredictFullBanknote extends AbstractPredictIssue {

	/**
	 * Declaring variables
	 */
	private BanknoteStorageUnit banknoteStorage;
	private String issue;
	private boolean issueExists;

	/**
	 * This is the constructor for this class
	 * 
	 * @param stationNumber  is the self checkout station's number
	 * @param sessionStarted is the boolean for whether the session started
	 * @param storageUnit    is the bank note storage unit of the self checkout
	 *                       station
	 */
	public PredictFullBanknote(String stationNumber, boolean sessionStarted, BanknoteStorageUnit storageUnit) {

		/**
		 * This sends the parameters to the super class
		 */
		super(stationNumber, sessionStarted);

		/**
		 * This initializes the variables
		 */
		this.banknoteStorage = storageUnit;

		/**
		 * This if statement is needed to ensure that the software is only checking /
		 * predicting this issue while a session is not happening
		 */
		if (sessionStarted == false) {
			predictFullBanknote();
			/**
			 * This sets the text to the proper prediction of error
			 */
			super.predictIssueLabel.setText(this.issue);
		}
	}

	/**
	 * This private method is the logic for predicting if the banknote storage unit
	 * is almost full
	 */
	private void predictFullBanknote() {
		if (banknoteStorage.hasSpace() == true
				&& banknoteStorage.getBanknoteCount() >= banknoteStorage.getCapacity() - 5) {
			issue = "Banknote Storage Unit is almost full";
			issueExists = true;
		} else {
			issueExists = false;
		}
	}

	/**
	 * This public method returns a boolean of whether the banknote storage is full or
	 * not
	 * @return issueExists
	 */
	public boolean banknoteAlmostFull() {
		return issueExists;
	}
}
