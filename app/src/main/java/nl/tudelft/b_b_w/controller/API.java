package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.List;

import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.TrustValues;
import nl.tudelft.b_b_w.model.User;
import nl.tudelft.b_b_w.model.block.Block;

/**
 * Performs the actions on the blockchain
 */
public class API {

    private BlockController blockController;
    private TrustValueController trustValueController;
    private BlockVerificationController blockVerificationController;


    public API(User owner, Context context) {
        this.blockController = new BlockController(owner, context);
        this.trustValueController = new TrustValueController(context);
        this.blockVerificationController = new BlockVerificationController(context);
    }

    public void addBlockToChain(Block block) throws HashException {
        blockController.addBlockToChain(block);
    }

    public void makeGenesis(User user) throws HashException {
        blockController.createGenesis(user);
    }

    public void revokeBlockFromChain(Block block) throws HashException {
        blockController.revokeBlock(block);
    }

    public List<Block> getBlocks(User owner) {
        try {
            return blockController.getBlocks(owner.getName());
        } catch (HashException e) {
            //do nothing
        }
        return null;
    }

    public boolean isDatabaseEmpty() {
        return blockVerificationController.isDatabaseEmpty();
    }

    public void transaction(Block block, TrustValues type) {
        switch (type) {
            case SUCCESFUL_TRANSACTION: trustValueController.successfulTransaction(block);
                break;
            case FAILED_TRANSACTION: trustValueController.failedTransaction(block);
                break;
            case VERIFIED: trustValueController.verifyIBAN(block);
                break;
            case REVOKED: trustValueController.revokedTrustValue(block);
                break;
            default: //do nothing
                break;
        }
    }
}
