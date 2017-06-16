package nl.tudelft.bbw.blockchain;


/**
 * This is a enumeration for the type of block.
 */
public enum BlockType {

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
