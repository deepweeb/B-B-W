package nl.tudelft.b_b_w.controller;

import android.content.Context;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.database.Database;
import nl.tudelft.b_b_w.database.read.DatabaseEmptyQuery;


/**
 * Class which handles the verification part of the blockchain
 */
public class BlockVerificationController {

    /**
     * Attributes to keep the state of the program and database in memory
     */
    private Context context;
    private Database database;

    /**
     * Constructor for making this controller
     *
     * @param context The specific context which contains our database
     */
    public BlockVerificationController(Context context) {
        this.context = context;
        this.database = new Database(context);
    }

    /**
     * Method to check whether the database is empty or not
     *
     * @return True if it is indeed empty, otherwise false
     */
    public final boolean isDatabaseEmpty() {
        DatabaseEmptyQuery query = new DatabaseEmptyQuery();
        database.read(query);
        return query.isDatabaseEmpty();
    }

    /**
     * Method to check whether a given block is the same as the block in the database
     *
     * @param block The block which we want to verify
     * @return True if it is a legit block, otherwise false
     */
    public boolean verifyTrustworthiness(Block block) {
        BlockController blockController = new BlockController(block.getBlockOwner(), context);
        return blockController.blockExists(block) && block.verifyBlock(block);
    }
}
