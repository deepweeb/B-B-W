package nl.tudelft.bbw.exception;

/**
 * Thrown when the hash method (currently SHA-256) is not available.
 */
public class EncodingUnavailableException extends HashException {
    public String toString() {
        return "UTF-8 not available on this device";
    }
}
