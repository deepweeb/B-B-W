package nl.tudelft.b_b_w.controller;

import java.util.List;

import nl.tudelft.b_b_w.model.Block;

/**
 * Interface for BlockController class
 */
interface BlockControllerInterface {

    /**
     * Check if a block already exists in the database
     * @param owner owner of the block
     * @param key public key in the block
     * @param revoked whether the block is revoked
     * @return if the block already exists
     */
    boolean blockExists(String owner, String key, boolean revoked);

    /**
     * Adding a block to the blockchain
     *
     * @param block Block you want to add
     * @return returns the block you added
     */
    List<Block> addBlockToChain(Block block);

    /**
     * Add a block to the database with checking if the (owner,pubkey) pair
     * is already added to the database
     *
     * @param block Block you want to add
     */
    void addBlock(Block block);

    /**
     * Clears all blocks from the database
     */
    void clearAllBlocks();

    /**
     * Get all blocks that are not revoked in sorted order
     *
     * @return List of all the blocks
     */
    List<Block> getBlocks(String owner);

    /**
     * Function to backtrace the contact name given the hash that refer to their block
     * @param hash hash of the block which owner name we want to find from
     * @return name of owner
     */
    String getContactName(String hash);

    /**
     * Get the latest block of a specific owner
     *
     * @return a Block object, which is the newest block of the owner
     */
    Block getLatestBlock(String owner);

    /**
     * Get the latest sequence number of the chain of a specific owner
     *
     * @return an integer which is the latest sequence number of the chain
     */
    int getLatestSeqNumber(String owner);

    /**
     * Revoke a block from the blockchain by adding the same
     * block but setting revoked on true
     *
     * @param block The block you want to revoke
     * @return the revoked block
     */
    List<Block> revokeBlock(Block block);

    /**
     * Method for removing a certain block from a given list
     *
     * @param list  The list of all the blocks
     * @param block The revoke block
     * @return List without the revoked block
     */
    List<Block> removeBlock(List<Block> list, Block block);

    /**
     * verifyIBAN method
     * updates the trust value of the block to the set trust value for a verified IBAN
     * @param block given block to update
     * @return block that is updated
     */
    Block verifyIBAN(Block block);

    /**
     * successfulTransaction method
     * updates the trust value of the block to the set trust value for a succesful transaction
     * @param block given block to update
     * @return block that is updated
     */
    Block successfulTransaction(Block block);

    /**
     * failedTransaction method
     * updates the trust value of the block to the set trust value for a failed transaction
     * @param block given block to update
     * @return block that is updated
     */
    Block failedTransaction(Block block);

    /**
     * revokedTrustValue method
     * updates the trust value of the block to the set trust value for a revoked block
     * @param block given block to update
     * @return block that is updated
     */
    Block revokedTrustValue(Block block);

    /**
     * Check if the database is empty.
     * @return if the database is empty
     */
    boolean isDatabaseEmpty();
}