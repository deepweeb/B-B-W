package nl.tudelft.bbw.model.block;

/**
 * Enum indicating which kinds of block type there are
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
