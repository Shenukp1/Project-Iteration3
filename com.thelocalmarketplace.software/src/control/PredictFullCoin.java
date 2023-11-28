package control;

import com.tdc.coin.CoinStorageUnit;

/**
 * This class checks if the coin tray is almost full
 */
public class PredictFullCoin extends AbstractPredictIssue {

	/**
	 * Declaring variables
	 */
	private CoinStorageUnit coinStorage;
	private String issue;
	private boolean issueExists = false;

	public PredictFullCoin(String stationNumber, boolean sessionStarted, CoinStorageUnit coinStorage) {

		/**
		 * This sends the parameters to the super class
		 */
		super(stationNumber, sessionStarted);

		/**
		 * This initializes the variables
		 */
		this.coinStorage = coinStorage;

		/**
		 * This if statement is needed to ensure that the software is only checking /
		 * predicting this issue while a session is not happening
		 */
		if (sessionStarted == false) {
			predictFullCoin();
			/**
			 * This sets the text to the proper prediction of error
			 */
			super.predictIssueLabel.setText(this.issue);
		}
	}

	/**
	 * this method will set the appropriate string for the right assumption of error
	 */
	private void predictFullCoin() {
		if (coinStorage.getCoinCount() >= coinStorage.getCapacity() - 5) {
			issue = "Coin Tray is almost full";
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
	public boolean coinAlmostFull() {
		return issueExists;
	}
}
