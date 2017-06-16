package nl.tudelft.bbw.model.block;

import nl.tudelft.bbw.model.HashException;
import nl.tudelft.bbw.model.User;

/**
 * BlockFactory class
 * Outputs a block
 */
public final class BlockFactory {
    /**
     * Sequence number of first block
     */
    private static final int FIRST_SEQUENCE_NUMBER = 1;

    /**
     * Private constructor
     * Ensures that the class can not be instantiated
     */
    private BlockFactory() {
    }

    /**
     * Create a new block given the block data.
     *
     * @param data the data on which to base this block
     * @return the freshly created block
     */
    public static final Block createBlock(BlockData data) throws HashException {
        switch (data.getBlockType()) {
            case GENESIS:
                return new GenesisBlock(data);
            case ADD_KEY:
                return new AddKeyBlock(data);
            case REVOKE_KEY:
                return new RevokeKeyBlock(data);
            default:
                throw new IllegalArgumentException("unknown block type " + data.getBlockType());
        }
    }

    /**
     * Old block creation method with way too many parameters
     */
    @Deprecated
    public static final Block getBlock(String type, String newOwner, int sequenceIndex, String hash,
                                       String previousHashChain, String previousHashSender,
                                       String publicKey, String iban, double trustValue) throws
            HashException {
        // fill in values
        BlockData data = new BlockData();
        data.setOwner(new User(newOwner, iban));
        data.setIban(new User(newOwner, iban));
        data.setPreviousHashChain(previousHashChain);
        data.setPreviousHashSender(previousHashSender);
        data.setPublicKey(publicKey);
        data.setTrustValue(trustValue);
        data.setSequenceNumber(sequenceIndex);

        // determine block type
        if (sequenceIndex == FIRST_SEQUENCE_NUMBER) {
            data.setBlockType(BlockType.GENESIS);
        } else if (type.equals("REVOKE")) {
            data.setBlockType(BlockType.REVOKE_KEY);
        } else if (type.equals("BLOCK")) {
            data.setBlockType(BlockType.ADD_KEY);
        } else {
            throw new IllegalArgumentException("Unknown Block Type");
        }

        return createBlock(data);
    }
}
