package nl.tudelft.b_b_w.Controllers;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tudelft.b_b_w.Models.Block;
import nl.tudelft.b_b_w.Models.DatabaseHandler;
import nl.tudelft.b_b_w.Models.User;

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
     * @param _context the instance
     */
    public BlockController(Context _context) {
        this.context = _context;
        this.user = User.getUser();
        this.databaseHandler = DatabaseHandler.getInstance(this.context);
    }

    /**
     * adding a block to the blockchain
     * @param block Block you want to add
     * @return returns the block you added
     */
    public Block addBlock(Block block) {
        // Check if the block already exists

        Block latest = databaseHandler.getLatestBlock(block.getOwner(), block.getPublic_key());

        if (latest == null) {
            databaseHandler.addBlock(block);
        } else if (latest.isRevoked()) {
            throw new RuntimeException("Error - Block is already revoked");
        } else {
            if (block.isRevoked()) databaseHandler.addBlock(block);
            else throw new RuntimeException("Error - Block already exists");
        }

        return block;
    }

    /**
     * Get all blocks that are not revoked
     * @return List of all the blocks
     */
    public List<Block> getBlocks() {
        List<Block> blocks = databaseHandler.getAllBlocks();
        Map<Integer,Block> res = new HashMap<>();
        for (Block block : blocks) {
            if (block.isRevoked())
                res.remove(block.getSequence_number());
            else
                res.put(block.getSequence_number(), block);
        }
        return new ArrayList<Block>(res.values());
    }

    /**
     * Revoke a block from the blockchain by adding the same
     * block but setting revoked on true
     * @param block The block you want to revoke
     * @return the revoked block
     */
    public Block revokeBlock(Block block) {
        return addBlock(new Block(block.getOwner(), block.getSequence_number(),
                block.getPrevious_hash(), block.getPublic_key(), true));
    }

}
