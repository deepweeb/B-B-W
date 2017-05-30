package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.HashException;

/**
 * Block for revoking a key
 */
public class RevokeKeyBlock extends KeyBlock {
    /**
     * Create new revoke block
     * @param blockData {@inheritDoc}
     * @throws HashException {@inheritDoc}
     */
    public RevokeKeyBlock(BlockData blockData) throws HashException {
        super(blockData);

        // verify integration
        if (BuildConfig.DEBUG) {
            // verify integrity
            if (blockData.getBlockType() != BlockType.REVOKE_KEY
                    || blockData.getSequenceNumber() <= 1
                    || !blockData.getPreviousHashChain().equals("N/A")
                    || !blockData.getPreviousHashSender().equals("N/A"))
                throw new AssertionError("invalid revoke block");
        }
    }

    /**
     * As a revoke block, we always revoke
     * @return true
     */
    @Override
    public boolean isRevoked() {
        return true;
    }
}
