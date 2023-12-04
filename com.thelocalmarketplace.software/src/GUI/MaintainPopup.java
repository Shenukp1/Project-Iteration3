package GUI;

import javax.swing.JOptionPane;
import attendant.Maintain;

/**
 * @author kian / givenn19
 * This is a pop-up indicating to the attendant station that ink/paper/bank-notes/cash needs to be
 * loaded into the customer station.
 */
public class MaintainPopup {
    
	/**
	 * Method takes a maintenance message from Maintain and then creates a pop up.
	 * Pop up should say "Maintenance required at customer station." followed by a maintainance message.
	 * @param maintenanceRequired
	 */
    public static void showMaintenancePopup(Maintain maintenanceRequired) {
    	
    	//String maintainMessage = maintenanceRequired.getMessage(); // TODO update from Maintain class
    	String maintainMessage = "Printer is low on ink.";
    	System.out.println("Maintenance message: " + maintainMessage);
    	
        String message = "Maintenance required at customer station." + ".\n"
                + maintainMessage;

        // Show a pop-up dialog with an OK button (default frame used)
        JOptionPane.showMessageDialog(null, message, "Maintenance Alert", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Pop-up displayed.");
    }
}


