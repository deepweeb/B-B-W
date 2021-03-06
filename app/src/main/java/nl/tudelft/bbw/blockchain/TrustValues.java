package nl.tudelft.bbw.blockchain;

/**
 * Enum class for all available trustvalues
 */
public enum TrustValues {
    /**
     * Predefined trust values
     */
    INITIALIZED(10), REVOKED(0);

    /**
     * value of the predefined trust values
     */
    private double value;

    /**
     * Constructor to initialize trustvalue
     *
     * @param _value given trust value
     */
    TrustValues(double _value) {
        this.value = _value;
    }

    /**
     * Default getter method for trust value
     *
     * @return trust value
     */
    public double getValue() {
        return this.value;
    }
}
