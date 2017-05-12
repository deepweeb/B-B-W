package nl.tudelft.b_b_w.Models;

/**
 * Block represents
 *
 */

public class Block {

    //TODO own hash of block attribute


    //properties of a block
    private String owner, previous_hash, public_key;
    private boolean isRevoked;
    private int sequence_number;

    /**
     * Constructor to initialize a block
     * @param _owner owner of a block
     * @param _sequence_number sequence number of the block
     * @param _previous_hash the hash value of the block before in the chain
     * @param _public_key public key of the owner of the block
     * @param _isRevoked boolean to check whether a block is revoked or not
     */
    public Block(String _owner, int _sequence_number, String _previous_hash, String _public_key, boolean _isRevoked) {
        this.owner = _owner;
        this.previous_hash = _previous_hash;
        this.public_key = _public_key;
        this.isRevoked = _isRevoked;
        this.sequence_number = _sequence_number;
    }

    /**
     * Default getter for owner
     * @return owner of the block
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Default getter for previous block hash
     * @return previous hash
     */
    public String getPrevious_hash() {
        return previous_hash;
    }

    /**
     * Default getter for public key
     * @return public key of the block
     */
    public String getPublic_key() {
        return public_key;
    }

    /**
     * Default getter for checking whether a block is revoked
     * @return true or false
     */
    public boolean isRevoked() {
        return isRevoked;
    }

    /**
     * Default getter for sequence number
     * @return the sequence number of the block
     */
    public int getSequence_number() {
        return sequence_number;
    }

    /**
     * Equals methode to check whether two blocks match
     * @param o The block you want to match with the current block
     * @return true if blocks match, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (sequence_number != block.sequence_number) return false;
        if (!owner.equals(block.owner)) return false;
        if (!previous_hash.equals(block.previous_hash)) return false;
        return public_key.equals(block.public_key);

    }
}
