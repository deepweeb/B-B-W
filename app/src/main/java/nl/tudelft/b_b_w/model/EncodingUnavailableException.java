package nl.tudelft.b_b_w.model;

/**
 * Thrown when the hash method (currently SHA-256) is not available.
 */
public class EncodingUnavailableException extends HashException {
    public String toString() {
        return "UTF-8 not available on this device";
    }
}
