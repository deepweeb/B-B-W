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
     *
     * @param hash given a hash String
     */
    public Hash(String hash) {
        this.hashString = hash;
    }

    /**
     * toString() getter implementation
     *
     * @return String representation of the hash
     */
    @Override
    public String toString() {
        return this.hashString;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Hash hash = (Hash) o;

        return toString().equals(hash.toString());
    }

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        return hashString.hashCode();
    }
}
