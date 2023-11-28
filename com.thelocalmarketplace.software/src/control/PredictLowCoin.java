package control;

import com.tdc.coin.AbstractCoinDispenser;

/**
 * This class predicts if the coin dispenser is almost empty
 */
public class PredictLowCoin extends AbstractPredictIssue{

	private AbstractCoinDispenser coinDispenser;
	private String issue;
	private boolean issueExists = false;
	
	public PredictLowCoin(String stationNumber, boolean sessionStarted, AbstractCoinDispenser coinDispenser) {
		super(stationNumber, sessionStarted);
		
		this.coinDispenser = coinDispenser;
		
		if (sessionStarted == false) {
			predictLowCoin();
			super.predictIssueLabel.setText(this.issue);
		}
	}
	
	private void predictLowCoin() {
		if (coinDispenser.size() <= 5) {
			issue = "Coin Dispenser is almost empty";
			issueExists = false;
		}
	}
	
	public boolean coinAlmostEmpty() {
		return issueExists;
	}
}
