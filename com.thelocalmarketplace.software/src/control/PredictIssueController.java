package control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.ICoinDispenser;
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
			 * This updates the Attendant Station's GUI for new issues within the self
			 * checkout station
			 */
			String text = "";
			for (int i = 0; i < listOfIssues.size(); i++ ) {
				text += "<html>" + listOfIssues.get(i) + "<br/>";
			}
			attendantGUI.update(text);
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
		Iterator<Map.Entry<BigDecimal, ICoinDispenser>> itr = scs.getCoinDispensers().entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<BigDecimal, ICoinDispenser> banknote = itr.next();
			if (banknote.getValue().size() <= 5) {
				lowCoinIssueExists = true;
			}
		}
		if (lowCoinIssueExists == true) {
			listOfIssues.add("One or More of the Coin Dispensers is Almost Empty");

		}
	}

	/**
	 * This method predicts low banknote within the station's banknote dispenser
	 */
	public void predictLowBanknote() {
		Iterator<Map.Entry<BigDecimal, IBanknoteDispenser>> itr = scs.getBanknoteDispensers().entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<BigDecimal, IBanknoteDispenser> banknote = itr.next();
			if (banknote.getValue().size() <= 5) {
				lowBanknoteIssueExists = true;
			}
		}
		if (lowBanknoteIssueExists == true) {
			listOfIssues.add("One or More of the Banknote Dispensers is Almost Empty");
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
	 * 
	 * @return the boolean lowInkIssueExists
	 */
	public boolean inkAlmostEmpty() {
		return lowInkIssueExists;
	}

	/**
	 * This method returns whether the paper is almost empty or not
	 * 
	 * @return the boolean lowPaperIssueExists
	 */
	public boolean paperAlmostEmpty() {
		return lowPaperIssueExists;
	}

	/**
	 * This method returns whether the coin is almost empty or not
	 * 
	 * @return the boolean lowCoinIssueExists
	 */
	public boolean coinAlmostEmpty() {
		return lowCoinIssueExists;
	}

	/**
	 * This method returns whether the banknote is almost empty or not
	 * 
	 * @return the boolean lowBanknoteIssueExists
	 */
	public boolean banknoteAlmostEmpty() {
		return lowBanknoteIssueExists;
	}

	/**
	 * This method returns whether the coin is almost full or not
	 * 
	 * @return the boolean fullCoinIssueExists
	 */
	public boolean coinAlmostFull() {
		return fullCoinIssueExists;
	}

	/**
	 * This method returns whether the banknote is almost full or not
	 * 
	 * @return fullBanknoteIssueExists
	 */
	public boolean banknoteAlmostFull() {
		return fullBanknoteIssueExists;
	}

	public int numberOfIssues() {
		return listOfIssues.size();

	}

}
