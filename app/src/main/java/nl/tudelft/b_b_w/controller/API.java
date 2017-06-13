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
    private BlockVerificationController blockVerificationController;


    public API(User owner, Context context) throws HashException {
        this.blockController = new BlockController(owner, context);
        this.blockVerificationController = new BlockVerificationController(context);
        blockController.createGenesis(owner);
    }

    public void addContactToChain(User contact) throws HashException {
        blockController.addBlockToChain(contact);
    }

    public void revokeContactFromChain(User contact) throws HashException {
        blockController.revokeBlockFromChain(contact);
    }

    public void addContactToDatabase(User owner, User contact) throws HashException {
        blockController.createKeyBlock(owner, contact);
    }

    public void addRevokeContactToDatabase(User owner, User contact) throws HashException {
        blockController.createRevokeBlock(owner, contact);
    }

    public List<Block> getBlocks(User owner) throws HashException {
        return blockController.getBlocks(owner.getName());
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
