package nl.tudelft.b_b_w.blockchaincomponents;

/**
 * Class for creating a user
 */

public class Hash {

    /**
     * Properties of a hash
     */
    private String hash;

    public Hash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return this.hash;
    }

    /**
     * This function checks whether two User objects are equal to each other.
     *
     * @param o an object
     * @return true in case they are equals, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        return this.hash.equals(o.toString());
    }
}
