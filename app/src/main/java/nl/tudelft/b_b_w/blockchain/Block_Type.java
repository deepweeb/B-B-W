package nl.tudelft.b_b_w.blockchain;


/**
 * This is a enumeration for the type of block.
 */
public enum Block_Type {

    /**
     * Genesis block type
     */
    GENESIS,

    /**
     * Add key block type
     */
    ADD_KEY,

    /**
     * Revoke key block type
     */
    REVOKE_KEY,
}
