package GUI;

import javax.swing.JOptionPane;
import attendant.Maintain;

/**
 * @author kian / givenn19
 * This is a pop-up indicating to the attendant station that ink/paper/bank-notes/cash has been
 * loaded into the customer station.
 */
public class MaintainPopup {
    
	/**
	 * Method takes a maintenance message from Maintain and then creates a pop up.
	 * Pop up should say "Maintenance required at customer station." followed by a maintenance message.
	 * @param maintenanceRequired
	 */
    public static void showMaintenancePopup(Maintain maintenanceRequired) {
    	
    	//String maintainMessage = maintenanceRequired.getMessage(); // TODO update from Maintain class
    	String maintainMessage = "Printer has been loaded with ink.";  // replacement/example message
    	System.out.println("Maintenance message: " + maintainMessage);
    	
        String message = "Maintenance completed at customer station." + ".\n"
                + maintainMessage;

        // Show a pop-up dialog with an OK button (default frame is used)
        JOptionPane.showMessageDialog(null, message, "Maintenance Alert", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Pop-up displayed.");
    }
    
    
}