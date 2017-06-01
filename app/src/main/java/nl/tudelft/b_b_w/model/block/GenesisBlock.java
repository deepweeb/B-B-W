package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.HashException;

/**
 * The first block of a chain.
 */
public class GenesisBlock extends Block {
    /**
     * When creating a genesis block in debug mode its values are checked
     * @param blockData {@inheritDoc}
     * @throws HashException {@inheritDoc}
     */
    public GenesisBlock(BlockData blockData) throws HashException {
        super(blockData);

        // verify integration
        if (BuildConfig.DEBUG) {
            // verify integrity
            if (blockData.getBlockType() != BlockType.ADD_KEY
                    || blockData.getSequenceNumber() != 1
                    || !blockData.getPreviousHashChain().equals("N/A")
                    || !blockData.getPreviousHashSender().equals("N/A")
                    || !blockData.getIban().equals("N/A"))
                throw new AssertionError("invalid genesis block " + this.toString());
        }
    }

    /**
     * The genesis block is defined as not revoked
     * @return false
     */
    @Override
    public boolean isRevoked() {
        return false;
    }
}
