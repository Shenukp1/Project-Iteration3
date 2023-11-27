package control;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

public abstract class AbstractPredictIssue {
	private JFrame predictIssueFrame;
	private JPanel predictIssuePanel;
	public JLabel predictIssueLabel;
	private JButton predictIssueConfirmed;

	public AbstractPredictIssue(String stationNumber, boolean sessionStarted) {

		predictIssueFrame = new JFrame();
		predictIssuePanel = new JPanel();
		predictIssueLabel = new JLabel("Issue Was Detected On Self Checkout Station #" + stationNumber);
		predictIssuePanel.setLayout(new GridLayout(40, 40));

		
		predictIssuePanel.add(predictIssueConfirmed);

	}
}
