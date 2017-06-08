package nl.tudelft.b_b_w.Objects;

import nl.tudelft.b_b_w.model.User;
import nl.tudelft.b_b_w.model.block.BlockType;

/**
 * The BlockData class lets you gather data and finally construct a block
 */
public class BlockData extends nl.tudelft.b_b_w.model.block.BlockData {
    /**
     * The block type of the block
     */
    private BlockType blockType;

    /**
     * The sequence number of the block
     */
    private int sequenceNumber;

    /**
     * THe owner name of this block
     */
    private String ownerName;

    /**
     * The hash of this block
     */
    private String hash;

    /**
     * The hash of the previous block in the chain
     */
    private String previousHashChain;

    /**
     * The hash of the previous block of the sender of the block
     */
    private String previousHashSender;

    /**
     * The public key included in this block
     */
    private String publicKey;

    /**
     * The IBAN of the owner of the block
     */
    private String iban;

    /**
     * The trust value of this block
     */
    private int trustValue;

    /**
     * Get the block type
     *
     * @return the block type
     */
    public BlockType getBlockType() {
        return blockType;
    }

    /**
     * Set the block type
     *
     * @param blockType the block type
     */
    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    /**
     * Get the sequence number
     *
     * @return the sequence number
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Set the sequence number
     *
     * @param sequenceNumber the sequence number
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Get the owner
     *
     * @return the owner
     */
    public User getOwner() {
        return new User(ownerName, iban);
    }

    /**
     * Set the owner
     *
     * @param owner the owner
     */
    public void setOwner(User owner) {
        this.ownerName = owner.getName();
    }

    /**
     * Set the IBAN
     *
     * @param contact the contact
     */
    public void setIban(User contact) {
        this.iban = contact.getIban();
    }

    /**
     * Get the previous hash in the chain of this block
     *
     * @return
     */
    public String getPreviousHashChain() {
        return previousHashChain;
    }

    /**
     * Set the previous hash in the chain of this block
     *
     * @param previousHashChain the previous hash in the chain of this block
     */
    public void setPreviousHashChain(String previousHashChain) {
        this.previousHashChain = previousHashChain;
    }

    /**
     * Get the previous hash of the sender of this block
     *
     * @return the previous hash of the sender of this block
     */
    public String getPreviousHashSender() {
        return previousHashSender;
    }

    /**
     * Set the previous hash of the sender of this block
     *
     * @param previousHashSender the previous hash of the sender of this block
     */
    public void setPreviousHashSender(String previousHashSender) {
        this.previousHashSender = previousHashSender;
    }

    /**
     * Get the public key stored in this block
     *
     * @return the public key stored in this block
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Set the public key stored in this block
     *
     * @param publicKey the public key stored in this block
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Get the trust value of this block
     *
     * @return the trust value of this block
     */
    public int getTrustValue() {
        return trustValue;
    }

    /**
     * Set the trust value of this block
     *
     * @param trustValue the trust value of this block
     */
    public void setTrustValue(int trustValue) {
        this.trustValue = trustValue;
    }
}
