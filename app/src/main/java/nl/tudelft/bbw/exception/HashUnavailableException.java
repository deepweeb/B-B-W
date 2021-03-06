package nl.tudelft.bbw.exception;

/**
 * Thrown when the hash method (currently SHA-256) is not available.
 */
public class HashUnavailableException extends HashException {
    public String toString() {
        return "SHA-256 not available on this device";
    }
}
