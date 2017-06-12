package nl.tudelft.b_b_w.blockchain;


/**
 * Class for creating a BlockData object
 */
public class BlockData {

    /**
     * Properties of a BlockData object
     */
    private Block_Type blockType;
    private int sequenceNumber;
    private Hash previousHashChain;
    private Hash previousHashSender;
    private int trustValue;

    /**
     * Constructor for the BlockData class
     * @param blockType given the type of the block: revoke, add or genesis
     * @param sequenceNumber given the sequence number of the block in a chain of the block's owner
     * @param previousHashChain given the ownHash of the block with this sequenceNumber - 1;
     * @param previousHashSender given the ownHash of the block where the contact of the current
     *                          iban number, public key, etc are.
     * @param trustValue given trust value of the contact
     */
    public BlockData(Block_Type blockType, int sequenceNumber, Hash previousHashChain,
                     Hash previousHashSender, int trustValue) {
        this.blockType = blockType;
        this.sequenceNumber = sequenceNumber;
        this.previousHashChain = previousHashChain;
        this.previousHashSender = previousHashSender;
        this.trustValue = trustValue;
    }

    /**
     * Get the block type of the BlockData
     * @return the block type
     */
    public Block_Type getBlockType() {
        return blockType;
    }

    /**
     * Get the sequence number
     * @return the sequence number
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Get the previous ownHash in the chain of this block
     * @return the hash of the previous block of the current chain
     */
    public Hash getPreviousHashChain() {
        return previousHashChain;
    }


    /**
     * Get the previous ownHash of the sender of this block
     * @return the previous ownHash of the sender of this block
     */
    public Hash getPreviousHashSender() {
        return previousHashSender;
    }

    /**
     * Get the trust value of this block
     * @return the trust value of this block
     */
    public int getTrustValue() {
        return trustValue;
    }


}
