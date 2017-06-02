package nl.tudelft.b_b_w.model;

/**
 * Exception for when hashes do not match
 */
public class HashMismatchException extends HashException {
    private String expected;
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
        return "Hash mismatch! Expected: "  + expected + ", calculated: " + calculated;
    }
}
