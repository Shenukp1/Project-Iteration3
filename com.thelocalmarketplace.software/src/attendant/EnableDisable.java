package attendant;

public class EnableDisable {

    private StationStatus status;
    private boolean isSessionActive;
    private String authorizedPassword; // Password for authorization

    enum StationStatus {
        ENABLED,
        DISABLED,
        OUT_OF_ORDER // When maintenance is required
    }
    // Constructor
    public EnableDisable(String authorizedPassword) {
        this.status = StationStatus.ENABLED; // Default status
        this.isSessionActive = false;
        this.authorizedPassword = authorizedPassword; // Set the authorized password
    }

    /**
     * Enable a station with password authentication.
     * @param password The password for authorization.
     * @return boolean True if operation was successful, false otherwise.
     */
    public boolean enableStation(String password) {
        if (isAuthorized(password) && status == StationStatus.DISABLED) {
            status = StationStatus.ENABLED;
            // Logic to be implemented
            return true;
        }
        return false;
    }

    /**
     * Disable a station with password authentication.
     * @param password The password for authorization.
     * @return boolean True if operation was successful, false otherwise.
     */
    public boolean disableStation(String password) {
        if (isAuthorized(password) && !isSessionActive) {
            status = StationStatus.DISABLED;
            // Logic to be implemented
            return true;
        }
        return false;
    }

    /**
     * Check if the provided password is authorized.
     * @param password The password to be verified.
     * @return boolean True if authorized, false otherwise.
     */
    private boolean isAuthorized(String password) {
        return password != null && password.equals(authorizedPassword);
    }

}
