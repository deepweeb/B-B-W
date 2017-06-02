package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.HashException;

/**
 * Block for revoking a key
 */
public class RevokeKeyBlock extends KeyBlock {
    /**
     * First used sequence number
     */
    private final int firstSequenceNumber = 1;

    /**
     * Not available information
     */
    private final String notAvailable = "N/A";

    /**
     * Create new revoke block
     *
     * @param blockData {@inheritDoc}
     * @throws HashException {@inheritDoc}
     */
    public RevokeKeyBlock(BlockData blockData) throws HashException {
        super(blockData);

        // verify integration
        if (BuildConfig.DEBUG) {
            // verify integrity
            if (blockData.getBlockType() != BlockType.REVOKE_KEY
                    || blockData.getSequenceNumber() <= firstSequenceNumber
                    || blockData.getPreviousHashChain().equals(notAvailable)) {
                throw new AssertionError("invalid revoke block");
            }
        }
    }
}

