package nl.tudelft.b_b_w.model;

/**
 * Enum class for all available trustvalues
 */
public enum TrustValues {
    /**
     * Predefined trust values
     */
    REVOKED(0), VERIFIED(50), SUCCESFUL_TRANSACTION(10), FAILED_TRANSACTION(-10);

    /**
     * value of the predefined trust values
     */
    private int value;

    /**
     * Constructor to initialize trustvalue
     * @param _value given trust value
     */
    TrustValues(int _value) {
        this.value = _value;
    }

    /**
     * Default getter method for trust value
     * @return trust value
     */
    public int getValue() {
        return this.value;
    }
}
