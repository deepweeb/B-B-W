package nl.tudelft.b_b_w.model.block;

import static android.R.attr.type;

/**
 * BlockFactory class
 * Outputs a block
 */
public class BlockFactory {
    public static final Block getBlock(BlockData data) {
        switch (data.getBlockType()) {
            case GENESIS:
                return new GenesisBlock(data);
            case ADD_KEY:
                return new AddKeyBlock(data);
            case REVOKE_KEY:
            default:
                throw new IllegalArgumentException("Invalid type of block: " + type);
        }
    }

    @Deprecated
    public static final Block getBlock(String type, String newOwner, int sequenceIndex, String hash,
                                       String previousHashChain, String previousHashSender,
                                       String publicKey, String iban, int trustValue) {
        BlockData data = new BlockData();
        data.setOwner(newOwner);
        data.setHash(hash);
        data.setPreviousHashChain(previousHashChain);
        data.setPreviousHashSender(previousHashSender);
        data.setPublicKey(publicKey);
        data.setIban(iban);
        data.setTrustValue(trustValue);

        // determine block type
        if (sequenceIndex == 1)
            data.setBlockType(BlockType.GENESIS);
        else if (type.equals("REVOKE"))
            data.setBlockType(BlockType.REVOKE_KEY);
        else if (type.equals("ADD"))
            data.setBlockType(BlockType.ADD_KEY);
        else
            throw new IllegalArgumentException("Unknown Block Type");

        return getBlock(data);
    }
}

