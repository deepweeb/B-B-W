package nl.tudelft.b_b_w.blockchain;


import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * This class represents a block object.
 */
public class Block {

    /**
     * Properties of a Block object
     */
    private User owner;
    private User contact;
    private BlockData blockData;
    private Hash ownHash;

    /**
     * Constructor for the BlockData class
     *
     * @param blockOwner given the name of the owner of the chain this block belongs to
     * @param contact    given the User object of the contact which the block concerns
     * @param blockData  the data of the block such as hash, trust, etc.
     */
    public Block(User blockOwner, User contact, BlockData blockData) {
        this.owner = blockOwner;
        this.contact = contact;
        this.blockData = blockData;
        this.ownHash = calculateHash();
    }

    /***********************************************************************************
     * This part below contains methods to get the attributes of the owner.
     */
    /**
     * This method returns the chainOwner User of the block object.
     *
     * @return user object of the chain owner
     */
    public User getBlockOwner() {
        return owner;
    }

    /**
     * This method returns the name of the owner of the block object.
     *
     * @return the owner's name.
     */
    public String getOwnerName() {
        return owner.getName();
    }

    /**
     * This method returns the iban of the owner of the block object.
     *
     * @return the owner's iban String.
     */
    public String getOwnerIban() {
        return owner.getIban();
    }

    /**
     * This method returns the iban of the owner of the block object.
     *
     * @return the owner's Iban String.
     */
    public EdDSAPublicKey getOwnerPublicKey() {
        return owner.getPublicKey();
    }

    /**************************************END******************************************/


    /***********************************************************************************
     * This part below contains methods to get the attributes of the contact.
     */

    /**
     * This method returns the contact of the block object.
     *
     * @return the contact object.
     */
    public User getContact() {
        return contact;
    }

    /**
     * This method returns the name of the contact of the block object.
     *
     * @return the contact's name.
     */
    public String getContactName() {
        return contact.getName();
    }

    /**
     * This method returns the iban of the contact of the block object.
     *
     * @return the contact's iban String.
     */
    public String getContactIban() {
        return contact.getIban();
    }

    /**
     * This method returns the iban of the contact of the block object.
     *
     * @return the contact's iban String.
     */
    public EdDSAPublicKey getContactPublicKey() {
        return contact.getPublicKey();
    }

    /**************************************END******************************************/


    /***********************************************************************************
     * This part below contains methods to get the attributes of the contact.
     */

    /**
     * This method returns the data of the block object.
     *
     * @return the BlockData object of the Block.
     */
    public BlockData getBlockData() {
        return blockData;
    }


    /**
     * This method returns the BlockType of the block object.
     *
     * @return the BlockType object of the Block.
     */
    public BlockType getBlockType() {
        return blockData.getBlockType();
    }

    /**
     * Boolean indicating if this block is revoked.
     *
     * @return if this block is a revoke block
     */
    public final boolean isRevoked() {
        return blockData.getBlockType() == BlockType.REVOKE_KEY;
    }


    /**
     * This method returns the sequence number of the block
     *
     * @return the sequence number of the block
     */
    public final int getSequenceNumber() {
        return blockData.getSequenceNumber();
    }

    /**
     * This method returns the hash of the previous block of the chain
     *
     * @return previous hash of chain
     */
    public final Hash getPreviousHashChain() {
        return blockData.getPreviousHashChain();
    }

    /**
     * This method returns the hash of the block of the contact/  block sender.
     *
     * @return hash of sender block
     */
    public final Hash getPreviousHashSender() {
        return blockData.getPreviousHashSender();
    }

    /**
     * This method returns the trust value of the block
     *
     * @return int trust value of block.
     */
    public final double getTrustValue() {
        return blockData.getTrustValue();
    }

    /**
     * This method set the trust value of the block
     *
     * @param trustValue of the block
     */
    public final void setTrustValue(double trustValue) {
        blockData.setTrustValue(trustValue);
    }


    /**************************************END******************************************/

    /**
     * This method returns the hash of the block
     *
     * @return Hash object of the block's hash
     */
    public final Hash getOwnHash() {
        return ownHash;
    }

    /**
     * Calculate the SHA-256 hash of this block
     *
     * @return the base-64 encoded hash as a string
     */
    private Hash calculateHash() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String text = this.getOwnerName()
                    + this.getOwnerIban()
                    + this.getOwnerPublicKey().toString()
                    + this.getContactName()
                    + this.getContactIban()
                    + this.getContactPublicKey().toString()
                    + this.getBlockType().name()
                    + this.getSequenceNumber()
                    + this.getPreviousHashChain().toString()
                    + this.getPreviousHashSender().toString();
            md.update(text.getBytes("UTF-8"));
            byte[] digest = md.digest();
            Hash hash = new Hash(String.format("%064x", new BigInteger(1, digest)));
            return hash;
        } catch (Exception e) {
            return new Hash(e.getMessage());
        }
    }


    /**
     * Attributes are isRevoked, iban and public key
     *
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
        if (!getContactIban().equals(block.getContactIban())) {
            return false;
        }
        return getContactPublicKey().equals(block.getContactPublicKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Block)) {
            return false;
        }

        Block block = (Block) o;

        return ownHash.equals(block.ownHash);

    }

    @Override
    public int hashCode() {
        return ownHash.hashCode();
    }


}
