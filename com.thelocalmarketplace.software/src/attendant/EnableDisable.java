package attendant;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;

public class EnableDisable {

    private StationStatus status;
    private String authorizedPassword; // Password for authorization
    private AbstractSelfCheckoutStation station;

   public enum StationStatus {
        ENABLED,
        DISABLED,
        OUT_OF_ORDER // When maintenance is required
    }

    // Constructor
    public EnableDisable(String authorizedPassword, AbstractSelfCheckoutStation station) {
        this.status = StationStatus.ENABLED; // Default status
        this.authorizedPassword = authorizedPassword; // Set the authorized password
        this.station = station; // Initialize the station reference
    }

    /**
     * Enable a station with password authentication.
     * @param password The password for authorization.
     * @return boolean True if operation was successful, false otherwise.
     */
    public boolean enableStation(String password) {
        try {
            if (isAuthorized(password) && status == StationStatus.DISABLED) {
                enableHardwareComponents();
                status = StationStatus.ENABLED;
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to enable station: " + e.getMessage());
            return false;
        }
    }

    /**
     * Disable a station with password authentication.
     * @param password The password for authorization.
     * @return boolean True if operation was successful, false otherwise.
     */
    public boolean disableStation(String password) {
        try {
            if (isAuthorized(password) && status == StationStatus.ENABLED) {
                disableHardwareComponents();
                status = StationStatus.DISABLED;
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Failed to disable station: " + e.getMessage());
            return false;
        }
    }

    /**
     * Enable hardware components of the station.
     * @throws Exception If there is a failure in enabling hardware components.
     */
    private void enableHardwareComponents() throws Exception {
        station.getBanknoteInput().enable();
        station.getCoinSlot().enable();
        station.getCardReader().enable();
        station.getScreen().enable();
        station.getMainScanner().enable();
        station.getHandheldScanner().enable();
        station.getPrinter().enable();
        station.getScanningArea().enable();
        station.getBaggingArea().enable();
        station.getScreen().enable();
        station.getReusableBagDispenser().enable();
    }

    /**
     * Disable hardware components of the station.
     * @throws Exception If there is a failure in disabling hardware components.
     */
    private void disableHardwareComponents() throws Exception {
        station.getBanknoteInput().disable();
        station.getCoinSlot().disable();
        station.getCardReader().disable();
        station.getScreen().disable();
        station.getMainScanner().disable();
        station.getHandheldScanner().disable();
        station.getPrinter().disable();
        station.getScanningArea().disable();
        station.getBaggingArea().disable();
        station.getScreen().disable();
        station.getReusableBagDispenser().disable();
    }

    /**
     * Check if the provided password is authorized.
     * @param password The password to be verified.
     * @return boolean True if authorized, false otherwise.
     */
    private boolean isAuthorized(String password) {
        return password != null && password.equals(authorizedPassword);
    }

	public StationStatus getStatus() {
		return status;
	}

}