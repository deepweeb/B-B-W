package nl.tudelft.bbw.controller;

import java.util.List;

import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.exception.HashException;

/**
 * Interface for BlockController class
 */
interface BlockControllerInterface {

    /**
     * Check if a block already exists in the database
     *
     * @param owner   owner of the block
     * @param key     public key in the block
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
    List<Block> addBlockToChain(Block block) throws HashException;

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
     * @param owner owner of which to get the blocks from
     * @return List of all the blocks
     */
    List<Block> getBlocks(String owner) throws HashException;

    /**
     * Function to backtrace the contact name given the hash that refer to their block
     *
     * @param hash hash of the block which owner name we want to find from
     * @return name of owner
     */
    String getContactName(String hash) throws HashException;

    /**
     * Get the latest block of a specific owner
     *
     * @param owner owner of which to get the block from
     * @return a Block object, which is the newest block of the owner
     */
    Block getLatestBlock(String owner) throws HashException;

    /**
     * Get the latest sequence number of the chain of a specific owner
     *
     * @param owner owner of which to get the block from
     * @return an integer which is the latest sequence number of the chain
     */
    int getLatestSeqNumber(String owner);

    /**
     * Revoke a block from the blockchain by adding the same
     * block but setting revoked on true
     *
     * @param block The block you want to revoke
     * @return the revoked block
     * @throws HashException when hashing has failed
     */
    List<Block> revokeBlock(Block block) throws HashException;

    /**
     * Method for removing a certain block from a given list
     *
     * @param list  The list of all the blocks
     * @param block The revoke block
     * @return List without the revoked block
     */
    List<Block> removeBlock(List<Block> list, Block block);

    /**
     * Check if the database is empty.
     *
     * @return if the database is empty
     */
    boolean isDatabaseEmpty();

    /**
     * backtrack method
     *
     * @param block given input block to backtrace the previous hash of the sender from
     * @return block which is the true ancestor
     */
    Block backtrack(Block block) throws HashException;

    /**
     * verifyTrustworthiness method
     *
     * @param block given input block to backtrace and verify trust value from
     * @return boolean, representing whether the blocks are equal
     */
    boolean verifyTrustworthiness(Block block) throws HashException;

    /**
     * Remove a chain with an owner from the database
     * @param owner the owner of the chain to remove
     */
    void removeChainFromDatabase(User owner);
}
