package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;
import nl.tudelft.b_b_w.model.DatabaseHandler;
import nl.tudelft.b_b_w.model.TrustValues;

/**

 * Performs the actions of the blockchain
 */

public class BlockController {

    private Context context;
    private DatabaseHandler databaseHandler;

    /**
     * contructor to initialize all the involved entities
     *
     * @param _context the instance
     */
    public BlockController(Context _context) {
        this.context = _context;
        this.databaseHandler = new DatabaseHandler(this.context);
    }

    public boolean blockExists(String owner, String key, boolean revoked) {
        List<Block> blocks = databaseHandler.getAllBlocks(owner);

        for (Block block : blocks) {
            if (Objects.equals(block.getOwner(), owner) && Objects.equals(block.getPublicKey(), key)
                && block.isRevoked() == revoked)
                return true;
        }

        return false;
    }


    /**
     * adding a block to the blockchain
     *
     * @param block Block you want to add
     * @return returns the block you added
     */
    public List<Block> addBlockToChain(Block block) {
        // Check if the block already exists
        String owner = block.getOwner();
        Block latest = databaseHandler.getLatestBlock(owner);

        if (latest == null) {
            databaseHandler.addBlock(block);
        } else if (latest.isRevoked()) {
            throw new RuntimeException("Error - Block is already revoked");
        } else {
            if (block.isRevoked()) {
                revokedTrustValue(latest);
                databaseHandler.updateBlock(latest);
                databaseHandler.addBlock(block);
            }
            else throw new RuntimeException("Error - Block already exists");
        }

        return getBlocks(owner);
    }


    /**
     * Add a block to the database with checking if the (owner,pubkey) pair
     * is already added to the database
     *
     * @param block Block you want to add
     */
    public void addBlock(Block block) {

        if (blockExists(block.getOwner(), block.getPublicKey(), block.isRevoked()))
            throw new RuntimeException("block already exists");

        databaseHandler.addBlock(block);
    }

    /**
     * Clears all blocks from the database
     */
    public void clearAllBlocks() {
        databaseHandler.clearAllBlocks();
    }

    /**
     * Get all blocks that are not revoked in sorted order
     *
     * @return List of all the blocks
     */
    public List<Block> getBlocks(String owner) {
        // retrieve all blocks in the database and then sort it in order of sequence number
        List<Block> blocks = databaseHandler.getAllBlocks(owner);
        Collections.sort(blocks, new Comparator<Block>() {
                    @Override
                    public int compare(Block o1, Block o2) {
                        return Integer.compare(o1.getSequenceNumber(), o2.getSequenceNumber());
                    }
                }
        );
        List < Block > res = new ArrayList<>();

        for (Block block : blocks) {
            if (block.isRevoked()) {
                res = removeBlock(res, block);
            } else {
                res.add(block);
            }
        }
        return res;
    }


    /**
     * Function to backtrace the contact name given the hash that refer to their block
     * @param hash hash of the block which owner name we want to find from
     * @return name of owner
     */
    public String getContactName(String hash) {
        return databaseHandler.getContactName(hash);
    }

    /**
     * Get the latest block of a specific owner
     *
     * @return a Block object, which is the newest block of the owner
     */
    public Block getLatestBlock(String owner) {
        return databaseHandler.getLatestBlock(owner);
    }

    /**
     * Get the latest sequence number of the chain of a specific owner
     *
     * @return an integer which is the latest sequence number of the chain
     */
    public int getLatestSeqNumber(String owner) {
        return databaseHandler.lastSeqNumberOfChain(owner);
    }

    /**
     *
     * Revoke a block from the blockchain by adding the same
     * block but setting revoked on true
     *
     * @param block The block you want to revoke
     * @return the revoked block
     */
    public List<Block> revokeBlock(Block block) {
        String owner = block.getOwner();
        Block newBlock = BlockFactory.getBlock("REVOKE", block.getOwner(),
                block.getOwnHash(), block.getPreviousHashChain(), block.getPreviousHashSender(),
                block.getPublicKey(), block.getIban(), block.getTrustValue());
        addBlock(newBlock);
        return getBlocks(owner);
    }

    /**
     * Method for removing a certain block from a given list
     *
     * @param list  The list of all the blocks
     * @param block The revoke block
     * @return List without the revoked block
     */
    public List<Block> removeBlock(List<Block> list, Block block) {
        List<Block> res = new ArrayList<>();
        for (Block blc : list) {
            if (!(blc.getOwner().equals(block.getOwner()) && blc.getPublicKey().equals(block.getPublicKey()))) {
                res.add(blc);
            }
        }
        return res;
    }

    /**
     * verifyIBAN method
     * updates the trust value of the block to the set trust value for a verified IBAN
     * @param block given block to update
     * @return block that is updated
     */
    public Block verifyIBAN(Block block) {
        block.setTrustValue(TrustValues.VERIFIED.getValue());
        return block;
    }

    /**
     * successfulTransaction method
     * updates the trust value of the block to the set trust value for a succesful transaction
     * @param block given block to update
     * @return block that is updated
     */
    public Block successfulTransaction(Block block) {
        block.setTrustValue(block.getTrustValue() + TrustValues.SUCCESFUL_TRANSACTION.getValue());
        return block;
    }

    /**
     * failedTransaction method
     * updates the trust value of the block to the set trust value for a failed transaction
     * @param block given block to update
     * @return block that is updated
     */
    public Block failedTransaction(Block block) {
        block.setTrustValue(block.getTrustValue() + TrustValues.FAILED_TRANSACTION.getValue());
        return block;
    }

    /**
     * revokedTrustValue method
     * updates the trust value of the block to the set trust value for a revoked block
     * @param block given block to update
     * @return block that is updated
     */
    public Block revokedTrustValue(Block block) {
        block.setTrustValue(TrustValues.REVOKED.getValue());
        return block;
    }

    /**
     * Check if the database is empty.
     * @return if the database is empty
     */
    public boolean isDatabaseEmpty() {
        return databaseHandler.isDatabaseEmpty();
    }

}
