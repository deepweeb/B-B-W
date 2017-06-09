package nl.tudelft.b_b_w.blockchaincomponents;


import nl.tudelft.b_b_w.model.block.BlockType;

/**
 * The BlockData class lets you gather data and finally construct a block
 */
public class BlockData {
    /**
     * The block type of the block
     */
    private BlockType blockType;

    /**
     * The sequence number of the block
     */
    private int sequenceNumber;

    /**
     * The hash of this block
     */
    private Hash hash;

    /**
     * The hash of the previous block in the chain
     */
    private Hash previousHashChain;

    /**
     * The hash of the previous block of the sender of the block
     */
    private Hash previousHashSender;


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
     * Get the sequence number
     *
     * @return the sequence number
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }


    /**
     * Get the previous hash in the chain of this block
     *
     * @return
     */
    public Hash getOwnHash() {
        return hash;
    }

    /**
     * Get the previous hash in the chain of this block
     *
     * @return
     */
    public Hash getPreviousHashChain() {
        return previousHashChain;
    }



    /**
     * Get the previous hash of the sender of this block
     *
     * @return the previous hash of the sender of this block
     */
    public Hash getPreviousHashSender() {
        return previousHashSender;
    }



    /**
     * Get the trust value of this block
     *
     * @return the trust value of this block
     */
    public int getTrustValue() {
        return trustValue;
    }


}
