package nl.tudelft.b_b_w.model.block;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nl.tudelft.b_b_w.model.EncodingUnavailableException;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.HashUnavailableException;
import nl.tudelft.b_b_w.model.User;

/**
 * Block represents a block of the blockchain
 */
public abstract class Block {
    /**
     * All data is stored here
     */
    private BlockData blockData;

    /**
     * Hash of this block
     */
    private String hash;

    /** Set the blockdata for this block */
    public Block(BlockData blockData) throws HashException {
        this.blockData = blockData;
        this.hash = calculateHash();
    }

    /**
     * Boolean indicating if this block is revoked.
     * @return if this block is a revoke block
     */
    public final boolean isRevoked() {
        return blockData.getBlockType() == BlockType.REVOKE_KEY;
    }

    /**
     * Boolean indicating if this block is a genesis block.
     * @return if this block is a genesis block
     */
    public final boolean isGenesis() {
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
     * @return owner of the block
     */
    public final User getOwner() {
        return blockData.getOwner();
    }

    /**
     * Default getter for own block hash
     * @return own hash
     */
    public final String getOwnHash() {
        return hash;
    }

    /**
     * Default getter for previous block hash of chain
     * @return previous hash of chain
     */
    public final String getPreviousHashChain() {
        return blockData.getPreviousHashChain();
    }

    /**
     * Default getter for previous block hash of chain
     * @return previous hash of chain
     */
    public final String getPreviousHashSender() {
        return blockData.getPreviousHashSender();
    }

    /**
     * Default getter for public key
     * @return public key of the block
     */
    public final String getPublicKey() {
        return blockData.getPublicKey();
    }

    /**
     * Default getter for sequence number
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
        if (this == o) {
            return true;
        }
        else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Block block = (Block) o;

        return getOwnHash().equals(block.getOwnHash());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return hash.hashCode();
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

    /**
     * Attributes are isRevoked, iban and public key
     * @param o given block
     * @return equals or not
     */
    public boolean verifyBlock(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Block block = (Block) o;

        if (isRevoked() != block.isRevoked()) {
            return false;
        }
        if (!getOwner().getIban().equals(block.getOwner().getIban())) {
            return false;
        }
        return getPublicKey().equals(block.getPublicKey());
    }

    /**
     * Calculate the SHA-256 hash of this block
     * @return the base-64 encoded hash as a string
     * @throws HashException when the crypto functions are not available
     */
    private String calculateHash() throws HashException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String text = getOwner().getName() + getPublicKey() + getPreviousHashChain() + getPreviousHashSender() + getOwner().getIban();
            md.update(text.getBytes("UTF-8"));
            byte[] digest = md.digest();
            String hash = String.format("%064x", new BigInteger(1, digest));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new HashUnavailableException();
        } catch (UnsupportedEncodingException e) {
            throw new EncodingUnavailableException();
        }
    }

}
