package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.HashException;

/**
 * The first block of a blockchain
 */
public class GenesisBlock extends Block {
    /**
     * First used sequence number
     */
    private final int firstSequenceNumber = 1;

    /**
     * Not available information
     */
    private final String notAvailable = "N/A";

    /**
     * When creating a genesis block in debug mode its values are checked
     *
     * @param blockData {@inheritDoc}
     * @throws HashException {@inheritDoc}
     */
    public GenesisBlock(BlockData blockData) throws HashException {
        super(blockData);

        // verify integration
        if (BuildConfig.DEBUG) {
            // verify integrity
            if (blockData.getBlockType() != BlockType.GENESIS
                    || blockData.getSequenceNumber() != firstSequenceNumber
                    || !blockData.getPreviousHashChain().equals(notAvailable)
                    || !blockData.getPreviousHashSender().equals(notAvailable)) {
                throw new AssertionError("invalid genesis block " + this.toString());
            }
        }
    }
}
