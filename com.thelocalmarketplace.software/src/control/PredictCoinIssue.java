package control;

import com.thelocalmarketplace.hardware.CoinTray;

/**
 * This class checks if the coin tray is almost full or almost empty
 */
public class PredictCoinIssue extends AbstractPredictIssue {

	private int capacity;
	private CoinTray coinTray;

	private String issue;
	private boolean issueExists = false;

	public PredictCoinIssue(int capacity, String stationNumber, boolean sessionStarted, CoinTray coinTray) {
		
		/**
		 * This sends the parameters to the super class
		 */
		super(stationNumber, sessionStarted);
		
		/**
		 * This initializes the variables
		 */
		this.capacity = capacity;
		this.coinTray = coinTray;

		/**
		 * This if statement is needed to ensure that the software is only checking /
		 * predicting this issue while a session is not happening
		 */
		if (sessionStarted == false) {
			predictCoinIssue();
			/**
			 * This sets the text to the proper prediction of error
			 */
			super.predictIssueLabel.setText(this.issue);
		}
	}

	/**
	 * this method will set the appropriate string for the right assumption of error
	 */
	private void predictCoinIssue() {
		boolean flag = true;
		while (flag) {
			if (coinTray.collectCoins().size() == capacity - 5) {
				issue = "Coin Tray is almost full";
				issueExists = true;
			} else if (coinTray.collectCoins().size() == 5) {
				issue = "Coin Tray is almost empty";
				issueExists = true;
			} else {
				break;
			}
		}
	}

	public boolean coinIssueExists() {
		return issueExists;
	}
}
