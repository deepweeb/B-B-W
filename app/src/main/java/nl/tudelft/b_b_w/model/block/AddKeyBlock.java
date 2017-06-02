package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.HashException;

/**
 * Block for adding a key
 */
public class AddKeyBlock extends KeyBlock {
    /** First used sequence number */
    private static final int FIRST_SEQUENCE_NUMBER = 1;

    /** Not available information */
    private final String NA = "N/A";

    /** {@inheritDoc} */
    public AddKeyBlock(BlockData blockData) throws HashException {
        super(blockData);

        // verify integration
        if (BuildConfig.DEBUG) {
            // verify integrity
            if (blockData.getBlockType() != BlockType.ADD_KEY
                    || blockData.getSequenceNumber() <= 1
                    || blockData.getPreviousHashChain().equals(NA))
            {
                throw new AssertionError("invalid revoke block");
            }
        }
    }
}
