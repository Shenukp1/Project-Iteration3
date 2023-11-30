package control;

import java.util.ArrayList;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

import gui.AttendantGUIMockup;

/**
 * This class predicts any issue that might occur within the self checkout
 * station
 * 
 * Issues that this controller will predict: 1. Low Ink 2. Low Paper 3. Low
 * Coins 4. Low Banknotes 5. Full Coins 6. Full Banknotes
 */
public class PredictIssueController {

	/**
	 * This declares all the variables and initializes only most of them.
	 */
	private AbstractSelfCheckoutStation scs;
	private ArrayList<String> listOfIssues = new ArrayList<>();
	private AttendantGUIMockup attendantGUI = new AttendantGUIMockup();
	private boolean lowInkIssueExists = false;
	private boolean lowPaperIssueExists = false;;
	private boolean lowCoinIssueExists = false;
	private boolean lowBanknoteIssueExists = false;
	private boolean fullCoinIssueExists = false;;
	private boolean fullBanknoteIssueExists = false;

	public PredictIssueController(SessionController sessionController, AbstractSelfCheckoutStation scs) {

		/**
		 * This initializes the self checkout station
		 */
		this.scs = scs;

		/**
		 * This if statement is important to ensure that the software is only checking
		 * for issues if the session is currently not occurring
		 */
		if (sessionController.isStarted() == false) {
			predictLowInk();
			predictLowPaper();
			predictLowCoin();
			predictLowBanknote();
			predictFullCoin();
			predictFullBanknote();

			/**
			 * This updates the Attendant Station's GUI for new issues within the self checkout station
			 */
			for (int i = 0; i <= listOfIssues.size(); i++) {
				attendantGUI.update(listOfIssues.get(i));
			}
		}
	}

	/**
	 * This method predicts low ink within the station's printer
	 */
	public void predictLowInk() {
		if (scs.getPrinter().inkRemaining() <= 20) {
			listOfIssues.add("Printer is almost out of ink");
			lowInkIssueExists = true;
		}
	}

	/**
	 * This method predicts low paper within the station's printer
	 */
	public void predictLowPaper() {
		if (scs.getPrinter().paperRemaining() >= 20) {
			listOfIssues.add("Printer is almost out of printer");
			lowPaperIssueExists = true;
		}
	}

	/**
	 * This method predicts low coin within the station's coin dispenser
	 */
	public void predictLowCoin() {
		if (scs.getCoinDispensers().size() <= 5) {
			listOfIssues.add("Coin Dispenser is almost empty");
			lowCoinIssueExists = false;
		}
	}

	/**
	 * This method predicts low banknote within the station's banknote dispenser
	 */
	public void predictLowBanknote() {
		if (scs.getBanknoteDispensers().size() <= 5) {
			listOfIssues.add("Banknote Dispenser is almost empty");
			lowBanknoteIssueExists = true;
		}
	}

	/**
	 * This method predicts full coin within the station's coin storage unit
	 */
	public void predictFullCoin() {
		if (scs.getCoinStorage().getCoinCount() >= scs.getCoinStorage().getCapacity() - 5) {
			listOfIssues.add("Coin Storage Unit is almost full");
			fullCoinIssueExists = true;
		}
	}

	/**
	 * This method predicts full banknote within the station's banknote storage unit
	 */
	public void predictFullBanknote() {
		if (scs.getBanknoteStorage().hasSpace() == true
				&& scs.getBanknoteStorage().getBanknoteCount() >= scs.getBanknoteStorage().getCapacity() - 5) {
			listOfIssues.add("Banknote Storage Unit is almost full");
			fullBanknoteIssueExists = true;
		}
	}

	/**
	 * This method returns whether the ink is almost empty or not
	 * @return the boolean lowInkIssueExists
	 */
	public boolean inkAlmostEmpty() {
		return lowInkIssueExists;
	}

	/**
	 * This method returns whether the paper is almost empty or not
	 * @return the boolean lowPaperIssueExists
	 */
	public boolean paperAlmostEmpty() {
		return lowPaperIssueExists;
	}

	/**
	 * This method returns whether the coin is almost empty or not
	 * @return the boolean lowCoinIssueExists
	 */
	public boolean coinAlmostEmpty() {
		return lowCoinIssueExists;
	}

	/**
	 * This method returns whether the banknote is almost empty or not
	 * @return the boolean lowBanknoteIssueExists
	 */
	public boolean banknoteAlmostEmpty() {
		return lowBanknoteIssueExists;
	}

	/**
	 * This method returns whether the coin is almost full or not
	 * @return the boolean fullCoinIssueExists
	 */
	public boolean coinAlmostFull() {
		return fullCoinIssueExists;
	}

	/**
	 * This method returns whether the banknote is almost full or not
	 * @return fullBanknoteIssueExists
	 */
	public boolean banknoteAlmostFull() {
		return fullBanknoteIssueExists;
	}
	
	public int numberOfIssues() {
		return listOfIssues.size();
		
	}

}
