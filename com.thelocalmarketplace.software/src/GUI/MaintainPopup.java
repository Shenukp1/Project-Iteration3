package GUI;

import javax.swing.JOptionPane;
import attendant.Maintain;

/**
 * @author kian / givenn19
 * This is a pop-up indicating to the attendant station that ink/paper/bank-notes/cash needs to be
 * loaded into the customer station.
 */
public class MaintainPopup {
    
    // frame should say "maintenance required: refill [i]" i=ink,paper,banknotes,coins.

    public static void showMaintenancePopup(Maintain maintenanceRequired) {
    	String maintainMessage = maintenanceRequired.getMessage(); // TODO update from Maintain class
    	
        String message = "Maintenance required at customer station." + ".\n"
                + maintainMessage;

        // Show a pop-up dialog with an OK button
        JOptionPane.showMessageDialog(null, message, "Maintenance Alert", JOptionPane.INFORMATION_MESSAGE);
    }
}


