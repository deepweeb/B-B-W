package nl.tudelft.bbw.model.block;

import nl.tudelft.bbw.BuildConfig;
import nl.tudelft.bbw.model.HashException;

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

        // verify integration and integrity
        if (BuildConfig.DEBUG && blockData.getBlockType() != BlockType.REVOKE_KEY
                || blockData.getSequenceNumber() <= firstSequenceNumber
                || blockData.getPreviousHashChain().equals(notAvailable)) {
            throw new AssertionError("Invalid revoke block");
        }
    }
}

