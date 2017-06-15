package nl.tudelft.b_b_w.controller;

import android.content.Context;
import android.content.res.Resources;

import nl.tudelft.b_b_w.model.GetDatabaseHandler;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.block.Block;

/**
 * Created by Ashay on 08/06/2017.
 */

public class BlockVerificationController {

    private Context context;
    private GetDatabaseHandler getDatabaseHandler;
    private final String notAvailable = "N/A";


    public BlockVerificationController(Context context) {
        this.context = context;
        this.getDatabaseHandler = new GetDatabaseHandler(context);
    }

    public final boolean isDatabaseEmpty() {
        return getDatabaseHandler.isDatabaseEmpty();
    }

    public Block backtrack(Block block) throws HashException {
        String previousHashSender = block.getPreviousHashSender();
        Block loopBlock = block;

        while (!previousHashSender.equals(notAvailable)) {
            loopBlock = getDatabaseHandler.getByHash(previousHashSender);
            if (loopBlock == null) {
                throw new
                        Resources.NotFoundException("Error - Block cannot be backtracked: "
                        + block.toString());
            }
            previousHashSender = loopBlock.getPreviousHashSender();
        }

        return loopBlock;
    }

    public boolean verifyTrustworthiness(Block block) throws HashException {
        BlockController blockController = new BlockController(block.getOwner(), context);
        return blockController.blockExists(block.getOwner().getName(), block.getPublicKey(), block.isRevoked())
                && block.verifyBlock(backtrack(block));
    }
}
