package nl.tudelft.bbw.model;

/**
 * Exception for when hashes do not match
 */
public class HashMismatchException extends HashException {
    /**
     * The expected hash value of a block
     */
    private String expected;

    /**
     * The calculated hash value of a block
     */
    private String calculated;

    /**
     * {@inheritDoc}
     */
    public HashMismatchException(String expected, String calculated) {
        this.expected = expected;
        this.calculated = calculated;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "Hash mismatch! Expected: " + expected + ", calculated: " + calculated;
    }
}
