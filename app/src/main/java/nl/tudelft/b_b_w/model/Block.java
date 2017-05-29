package nl.tudelft.b_b_w.model;

/**
 * Block represents
 */

public class Block {
    /** Reasonable value to multiply with to combine hash codes */
    private static final int HASH_MULTIPLIER = 31;

    //properties of a block
    private String owner;
    private int sequenceNumber;
    private String ownHash;
    private String previousHashChain;
    private String previousHashSender;
    private String publicKey;
    private String iban;
    private boolean isRevoked;
    private int trustValue;

    /**
     * Constructor for a block
     * @param _owner              owner of a block
     * @param _ownHash            our own hash
     * @param _previousHashChain  the hash value of the block before in the chain
     * @param _previousHashSender the hash value of the block before of the sender
     * @param _publicKey          public key of the sender
     * @param _iban               IBAN number of a contact
     * @param _trustvalue         the trust value of the contact
     * @param _isRevoked          boolean to check whether a block is revoked or not
     */
    public Block(String _owner, int _sequenceNumber, String _ownHash, String _previousHashChain, String _previousHashSender, String _publicKey, String _iban, int _trustvalue, boolean _isRevoked) {
        this.owner = _owner;
        this.sequenceNumber = _sequenceNumber;
        this.ownHash = _ownHash;
        this.previousHashChain = _previousHashChain;
        this.previousHashSender = _previousHashSender;
        this.publicKey = _publicKey;
        this.iban   = _iban;
        this.trustValue = _trustvalue;
        this.isRevoked = _isRevoked;
    }

    /**
     * Default getter for owner
     *
     * @return owner of the block
     */
    public final String getOwner() {
        return owner;
    }

    /**
     * Default getter for own block hash
     *
     * @return own hash
     */
    public final String getOwnHash() {
        return ownHash;
    }

    /**
     * Default getter for previous block hash of chain
     *
     * @return previous hash of chain
     */
    public final String getPreviousHashChain() {
        return previousHashChain;
    }

    /**
     * Default getter for previous block hash of chain
     *
     * @return previous hash of chain
     */
    public final String getPreviousHashSender() {
        return previousHashSender;
    }

    /**
     * Default getter for public key
     *
     * @return public key of the block
     */
    public final String getPublicKey() {
        return publicKey;
    }

    /**
     * Default getter for sequence number
     *
     * @return the sequence number of the block
     */
    public final int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Default getter for sequence number
     *
     * @return the sequence number of the block
     */
    public final String getIban() {
        return iban;
    }


    /**
     * Default getter for checking whether a block is revoked
     *
     * @return true or false
     */
    public final boolean isRevoked() {
        return isRevoked;
    }

    /**
     * Default getter for trustValue
     * @return the trust value of the block
     */
    public final int getTrustValue() { return trustValue;}

    /**
     * Default setter for trustValue
     * @param setValue trust value to set
     */
    public final void setTrustValue(int setValue) {this.trustValue = setValue;}

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (sequenceNumber != block.sequenceNumber) return false;
        if (isRevoked != block.isRevoked) return false;
        if (!owner.equals(block.owner)) return false;
        if (!ownHash.equals(block.ownHash)) return false;
        if (!previousHashChain.equals(block.previousHashChain)) return false;
        if (!previousHashSender.equals(block.previousHashSender)) return false;
        if (!iban.equals(block.iban)) return false;
        if (trustValue != (block.trustValue)) return false;
        return publicKey.equals(block.publicKey);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        int result = owner.hashCode();
        result = HASH_MULTIPLIER * result + sequenceNumber;
        result = HASH_MULTIPLIER * result + ownHash.hashCode();
        result = HASH_MULTIPLIER * result + previousHashChain.hashCode();
        result = HASH_MULTIPLIER * result + previousHashSender.hashCode();
        result = HASH_MULTIPLIER * result + publicKey.hashCode();
        result = HASH_MULTIPLIER * result + iban.hashCode();
        result = HASH_MULTIPLIER * result + trustValue;
        result = HASH_MULTIPLIER * result + (isRevoked ? 1 : 0);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "Block{" +
                "owner='" + owner + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", ownHash='" + ownHash + '\'' +
                ", previousHashChain='" + previousHashChain + '\'' +
                ", previousHashSender='" + previousHashSender + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", iban='" + iban + '\'' +
                ", trustValue='" + trustValue + '\'' +
                ", isRevoked=" + isRevoked +
                '}';
    }
}
