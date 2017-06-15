package nl.tudelft.b_b_w.controller;

import android.content.Context;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.database.Database;
import nl.tudelft.b_b_w.database.read.DatabaseEmptyQuery;
import nl.tudelft.b_b_w.model.HashException;

/**
 * Created by Ashay on 08/06/2017.
 */

public class BlockVerificationController {

    private Context context;
    private Database database;

    public BlockVerificationController(Context context) {
        this.context = context;
        this.database = new Database(context);
    }

    public final boolean isDatabaseEmpty() {
        DatabaseEmptyQuery query = new DatabaseEmptyQuery();
        database.read(query);
        return query.isDatabaseEmpty();    }

    public boolean verifyTrustworthiness(Block block) throws HashException {
        BlockController blockController = new BlockController(block.getBlockOwner(), context);
        return blockController.blockExists(block) && block.verifyBlock(block);
    }
}
