package nl.tudelft.bbw.blockchain;


import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.math.BigInteger;
import java.security.MessageDigest;

import nl.tudelft.bbw.model.TrustValues;

/**
 * This class represents a block object.
 */
public class Block {

    /**
     * Properties of a Block object
     */
    private User owner;
    private User contact;
    private BlockData data;
    private Hash ownHash;

    /**
     * Constructor for the BlockData class
     *
     * @param owner   given the User object of the owner of the chain this block belongs to
     * @param contact given the User object of the contact which the block concerns
     * @param data    the data of the block such as hash, trust, etc.
     */
    public Block(User owner, User contact, BlockData data) {
        this.owner = owner;
        this.contact = contact;
        this.data = data;
        this.ownHash = calculateHash();
    }

    /**
     * Constructor for the Genesis block
     *
     * @param blockOwner given the User object of the one you want to make an genesis block of.
     */
    public Block(User blockOwner) {
        this.owner = blockOwner;
        this.contact = blockOwner;
        this.data = new BlockData(BlockType.GENESIS, 1, Hash.NOT_AVAILABLE, Hash.NOT_AVAILABLE, TrustValues.INITIALIZED.getValue());
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
        return data;
    }


    /**
     * This method returns the BlockType of the block object.
     *
     * @return the BlockType object of the Block.
     */
    public BlockType getBlockType() {
        return data.getBlockType();
    }

    /**
     * Boolean indicating if this block is revoked.
     *
     * @return if this block is a revoke block
     */
    public final boolean isRevoked() {
        return data.getBlockType() == BlockType.REVOKE_KEY;
    }


    /**
     * This method returns the sequence number of the block
     *
     * @return the sequence number of the block
     */
    public final int getSequenceNumber() {
        return data.getSequenceNumber();
    }

    /**
     * This method returns the hash of the previous block of the chain
     *
     * @return previous hash of chain
     */
    public final Hash getPreviousHashChain() {
        return data.getPreviousHashChain();
    }

    /**
     * This method returns the hash of the block of the contact/  block sender.
     *
     * @return hash of sender block
     */
    public final Hash getPreviousHashSender() {
        return data.getPreviousHashSender();
    }

    /**
     * This method returns the trust value of the block
     *
     * @return int trust value of block.
     */
    public final double getTrustValue() {
        return data.getTrustValue();
    }

    /**
     * This method set the trust value of the block
     *
     * @param trustValue of the block
     */
    public final void setTrustValue(double trustValue) {
        data.setTrustValue(trustValue);
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


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Block#" + getSequenceNumber() + "{owner=" + owner.getName() + ",contact="
                + contact.getName() + "}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Block))
        {
            return false;
        }

        Block block = (Block) o;

        if (owner != null ? !owner.equals(block.owner) : block.owner != null) {
            return false;
        }
        if (contact != null ? !contact.equals(block.contact) : block.contact != null) {
            return false;
        }
        if (data != null ? !data.equals(block.data) : block.data != null) {
            return false;
        }
        return ownHash != null ? ownHash.equals(block.ownHash) : block.ownHash == null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (ownHash != null ? ownHash.hashCode() : 0);
        return result;
    }
}
