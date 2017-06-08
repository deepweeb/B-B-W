package nl.tudelft.b_b_w.Objects;


/**
 * This class represents a block object.
 */
public class Block {

    private User contact;
    private String blockOwner;
    private BlockData blockData;

    /**
     * Hash of this block
     */
    private String hash;

    /**
     * This method constructs an block object.
     *
     * @param contact    a contact.
     * @param blockOwner the owner of the chain.
     * @param blockData  some block data.
     */
    public Block(User contact, String blockOwner, BlockData blockData) {
        this.contact = contact;
        this.blockOwner = blockOwner;
        this.blockData = blockData;
    }

    /**
     * This method returns the contact of the block object.
     *
     * @return the contact object.
     */
    public User getContact() {
        return contact;
    }


    /**
     * This method returns the chainOwner of the block object.
     *
     * @return chain owner.
     */
    public String getBlockOwner() {
        return blockOwner;
    }

    /**
     * Boolean indicating if this block is revoked.
     *
     * @return if this block is a revoke block
     */
    public final boolean isRevoked() {
        return blockData.getBlockType().equals(BlockType.REVOKE_KEY);
    }

    /**
     * This method returns the data of the block object.
     *
     * @return the block data.
     */
    public BlockData getBlockData() {
        return blockData;
    }


    /**
     * Default getter for previous block hash of chain
     *
     * @return previous hash of chain
     */
    public final String getPreviousHashChain() {
        return blockData.getPreviousHashChain();
    }

    /**
     * Default getter for previous block hash of chain
     *
     * @return previous hash of chain
     */
    public final String getPreviousHashSender() {
        return blockData.getPreviousHashSender();
    }

    /**
     * Default getter for public key
     *
     * @return public key of the block
     */
    public final Public_Key getPublicKey() {
        return blockData.getPublicKey();
    }

    /**
     * Default getter for sequence number
     *
     * @return the sequence number of the block
     */
    public final int getSequenceNumber() {
        return blockData.getSequenceNumber();
    }

    /**
     * Default getter for own block hash
     *
     * @return own hash
     */
    public final String getOwnHash() {
        return hash;
    }

    /**
     * This method verifies a block
     *
     * @param object a block object.
     * @return true in case it is equal, otherwise false.
     */
    public boolean verifyBlock(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Block block = (Block) object;

        if (isRevoked() != block.isRevoked()) {
            return false;
        }

        if (!getContact().getIban().equals(block.getContact().getIban())) {
            return false;
        }
        return getPublicKey().equals(block.getPublicKey());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "Block{"
                + "owner='" + blockData.getOwner() + '\''
                + ", sequenceNumber=" + blockData.getSequenceNumber()
                + ", ownHash='" + hash + '\''
                + ", previousHashChain='" + blockData.getPreviousHashChain() + '\''
                + ", previousHashSender='" + blockData.getPreviousHashSender() + '\''
                + ", publicKey='" + blockData.getPublicKey() + '\''
                + ", iban='" + blockData.getOwner().getIban() + '\''
                + ", trustValue='" + blockData.getTrustValue() + '\''
                + ", type='" + blockData.getBlockType() + '\''
                + '}';
    }


}
