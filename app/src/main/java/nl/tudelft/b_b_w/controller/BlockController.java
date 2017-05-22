package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;
import nl.tudelft.b_b_w.model.DatabaseHandler;
import nl.tudelft.b_b_w.model.User;

/**
 * Performs the actions of the blockchain
 * Created by jasper on 11/05/2017.
 */

public class BlockController {

    private Context context;
    private User user;
    private DatabaseHandler databaseHandler;

    /**
     * contructor to initialize all the involved entities
     *
     * @param _context the instance
     */
    public BlockController(Context _context) {
        this.context = _context;
        this.user = new User();
        this.databaseHandler = new DatabaseHandler(this.context);
    }

    /**
     * adding a block to the blockchain
     *
     * @param block Block you want to add
     * @return returns the block you added
     */
    public List<Block> addBlock(Block block) {
        // Check if the block already exists
        String owner = block.getOwner();
        Block latest = databaseHandler.getLatestBlock(owner);

        if (latest == null) {
            databaseHandler.addBlock(block);
        } else if (latest.isRevoked()) {
            throw new RuntimeException("Error - Block is already revoked");
        } else {
            if (block.isRevoked()) databaseHandler.addBlock(block);
            else throw new RuntimeException("Error - Block already exists");
        }

        return getBlocks(owner);
    }

    /**
     * Get all blocks that are not revoked
     *
     * @return List of all the blocks
     */
    public List<Block> getBlocks(String owner) {
        List<Block> blocks = databaseHandler.getAllBlocks(owner);
        List<Block> res = new ArrayList<>();
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
     * Revoke a block from the blockchain by adding the same
     * block but setting revoked on true
     *
     * @param block The block you want to revoke
     * @return the revoked block
     */
    public List<Block> revokeBlock(Block block) {
        String owner = block.getOwner();
        Block newBlock = BlockFactory.getBlock("REVOKE", block.getOwner(), block.getSequenceNumber(),
                block.getOwnHash(), block.getPreviousHashChain(), block.getPreviousHashSender(),
                block.getPublicKey(), block.getIban());
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

}
