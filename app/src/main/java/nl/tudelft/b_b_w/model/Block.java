package nl.tudelft.b_b_w.model;

/**
 * Block represents
 */

public class Block {

    //properties of a block
    private String owner;
    private int sequenceNumber;
    private String ownHash;
    private String previousHashChain;
    private String previousHashSender;
    private String publicKey;
    private String iban;
    private boolean isRevoked;

    /**
     * @param _owner              owner of a block
     * @param _sequenceNumber     sequence number of the block
     * @param _ownHash            our own hash
     * @param _previousHashChain  the hash value of the block before in the chain
     * @param _previousHashSender the hash value of the block before of the sender
     * @param _publicKey          public key of the sender
     * @param _iban               IBAN number of a contact
     * @param _isRevoked          boolean to check whether a block is revoked or not
     */
    public Block(String _owner, int _sequenceNumber, String _ownHash, String _previousHashChain, String _previousHashSender, String _publicKey, String _iban, boolean _isRevoked) {
        this.owner = _owner;
        this.sequenceNumber = _sequenceNumber;
        this.ownHash = _ownHash;
        this.previousHashChain = _previousHashChain;
        this.previousHashSender = _previousHashSender;
        this.publicKey = _publicKey;
        this.iban   = _iban;
        this.isRevoked = _isRevoked;
    }

    /**
     * Default getter for owner
     *
     * @return owner of the block
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Default getter for own block hash
     *
     * @return own hash
     */
    public String getOwnHash() {
        return ownHash;
    }

    /**
     * Default getter for previous block hash of chain
     *
     * @return previous hash of chain
     */
    public String getPreviousHashChain() {
        return previousHashChain;
    }

    /**
     * Default getter for previous block hash of chain
     *
     * @return previous hash of chain
     */
    public String getPreviousHashSender() {
        return previousHashSender;
    }

    /**
     * Default getter for public key
     *
     * @return public key of the block
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Default getter for sequence number
     *
     * @return the sequence number of the block
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Default getter for sequence number
     *
     * @return the sequence number of the block
     */
    public String getIban() {
        return iban;
    }


    /**
     * Default getter for checking whether a block is revoked
     *
     * @return true or false
     */
    public boolean isRevoked() {
        return isRevoked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
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
        return publicKey.equals(block.publicKey);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + sequenceNumber;
        result = 31 * result + ownHash.hashCode();
        result = 31 * result + previousHashChain.hashCode();
        result = 31 * result + previousHashSender.hashCode();
        result = 31 * result + publicKey.hashCode();
        result = 31 * result + iban.hashCode();
        result = 31 * result + (isRevoked ? 1 : 0);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Block{" +
                "owner='" + owner + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", ownHash='" + ownHash + '\'' +
                ", previousHashChain='" + previousHashChain + '\'' +
                ", previousHashSender='" + previousHashSender + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", iban='" + iban + '\'' +
                ", isRevoked=" + isRevoked +
                '}';
    }
}
