package nl.tudelft.b_b_w.blockchain;

/**
 * Class for creating a Hash object
 */

public class Hash {

    /**
     * Properties of a hash
     */
    private String hashString;

    /**
     * Constructor for Hash class
     * @param hash given a hash String
     */
    public Hash(String hash) {
        this.hashString = hash;
    }

    /**
     * toString() getter implementation
     * @return String representation of the hash
     */
    @Override
    public String toString() {
        return this.hashString;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else {
            return this.hashString.equals(o.toString());
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
    return 0;
    }
}
