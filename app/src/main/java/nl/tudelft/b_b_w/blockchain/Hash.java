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
     * This function checks whether two User objects are equal to each other.
     * @param o an object
     * @return true in case they are equals, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else {
            return this.hashString.equals(o.toString());
        }
    }
}
