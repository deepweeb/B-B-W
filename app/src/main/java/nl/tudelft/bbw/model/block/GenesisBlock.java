package nl.tudelft.bbw.model.block;

import nl.tudelft.bbw.BuildConfig;
import nl.tudelft.bbw.model.HashException;

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

        // verify integration and integrity
        if (BuildConfig.DEBUG && blockData.getBlockType() != BlockType.GENESIS
                || blockData.getSequenceNumber() != firstSequenceNumber
                || !blockData.getPreviousHashChain().equals(notAvailable)
                || !blockData.getPreviousHashSender().equals(notAvailable)) {
            throw new AssertionError("Invalid genesis block " + this.toString());
        }
    }
}
