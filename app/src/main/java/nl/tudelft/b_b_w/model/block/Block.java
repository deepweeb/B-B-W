package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.User;

/**
 * Block represents
 */

public abstract class Block {
    /** All block data is stored in another class */
    private BlockData blockData;

    /** Hash value of the block */
    private String hash;

    /** Set the blockdata for this block */
    public Block(BlockData blockData) throws HashException {
        this.blockData = blockData;
        this.hash = blockData.calculateHash();
    }

    /** Does this block revoke a key? */
    public boolean isRevoked() {
        return blockData.getBlockType() == BlockType.REVOKE_KEY;
    }

    /** Is this block a genesis block? */
    public boolean isGenesis() {
        return blockData.getBlockType() == BlockType.GENESIS;
    }

    /**
     * Retrieve block data of this block
     * @return the block data
     */
    public final BlockData getBlockData() {
        return blockData;
    }

    /**
     * Default getter for owner
     *
     * @return owner of the block
     */
    public final User getOwner() {
        return blockData.getOwner();
    }

    /**
     * Default getter for own block hash
     *
     * TODO change to getHash()
     * @return own hash
     */
    public final String getOwnHash() {
        return hash;
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
    public final String getPublicKey() {
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
     * Default getter for trustValue
     * @return the trust value of the block
     */
    public final int getTrustValue() {
        return blockData.getTrustValue();
    }

    /**
     * Default setter for trustValue
     * @param trustValue trust value to set
     */
    public final void setTrustValue(int trustValue) {
        blockData.setTrustValue(trustValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        return this.blockData.equals(block.blockData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return blockData.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "Block{" +
                "owner='" + blockData.getOwner() + '\'' +
                ", sequenceNumber=" + blockData.getSequenceNumber() +
                ", ownHash='" + hash + '\'' +
                ", previousHashChain='" + blockData.getPreviousHashChain() + '\'' +
                ", previousHashSender='" + blockData.getPreviousHashSender() + '\'' +
                ", publicKey='" + blockData.getPublicKey() + '\'' +
                ", iban='" + blockData.getOwner().getIBAN() + '\'' +
                ", trustValue='" + blockData.getTrustValue() + '\'' +
                ", type='" + blockData.getBlockType() + '\'' +
                '}';
    }

    /**
     * Verifies whether the attributes of the given block are equal to this
     * Attributes are isRevoked, iban and public key
     * @param o given block
     * @return equals or not
     */
    public boolean verifyBlock(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (isRevoked() != block.isRevoked()) return false;
        if (!getOwner().getIBAN().equals(block.getOwner().getIBAN())) return false;
        return getPublicKey().equals(block.getPublicKey());
    }

}
