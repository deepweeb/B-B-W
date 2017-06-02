package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.HashException;

/**
 * Block for adding a key
 */
public class AddKeyBlock extends KeyBlock {
    /**
     * First used sequence number
     */
    private final int firstSequenceNumber = 1;

    /**
     * {@inheritDoc}
     */
    public AddKeyBlock(BlockData blockData) throws HashException {
        super(blockData);

        // verify integration and integrity
        if (BuildConfig.DEBUG && blockData.getBlockType() != BlockType.ADD_KEY
                || blockData.getSequenceNumber() <= firstSequenceNumber) {
            throw new AssertionError("Invalid add block " + this);
        }
    }
}
