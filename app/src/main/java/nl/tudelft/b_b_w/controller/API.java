package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.List;

import nl.tudelft.b_b_w.model.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.blockchain.Block;

/**
 * Performs the actions on the blockchain
 */
public class API {

    private BlockController blockController;
    private BlockVerificationController blockVerificationController;


    public API(User owner, Context context) throws HashException, BlockAlreadyExistsException {
        this.blockController = new BlockController(owner, context);
        this.blockVerificationController = new BlockVerificationController(context);
        blockController.createGenesis(owner);
    }

    public void addContactToChain(User contact) throws HashException, BlockAlreadyExistsException {
        blockController.addBlockToChain(contact);
    }

    public void revokeContactFromChain(User contact)
            throws HashException, BlockAlreadyExistsException {
        blockController.revokeBlockFromChain(contact);
    }

    public void addContactToDatabase(User owner, User contact)
            throws HashException, BlockAlreadyExistsException {
        blockController.createKeyBlock(owner, contact);
    }

    public void addRevokeContactToDatabase(User owner, User contact)
            throws HashException, BlockAlreadyExistsException {
        blockController.createRevokeBlock(owner, contact);
    }

    public List<Block> getBlocks(User owner) throws HashException, BlockAlreadyExistsException {
        return blockController.getBlocks(owner);
    }

    public boolean isDatabaseEmpty() {
        return blockVerificationController.isDatabaseEmpty();
    }

    public void successfulTransaction(Block block) {
        Block updatedBlock = TrustController.succesfulTransaction(block);
        blockController.updateBlock(updatedBlock);
    }

    public void failedTransaction(Block block) {
        Block updatedBlock = TrustController.failedTransaction(block);
        blockController.updateBlock(updatedBlock);
    }

    public void verifyIBAN(Block block) {
        Block updatedBlock = TrustController.verifiedIBAN(block);
        blockController.updateBlock(updatedBlock);
    }

    public void revokedBlock(Block block) {
        Block updatedBlock = TrustController.revokeBlock(block);
        blockController.updateBlock(updatedBlock);
    }
}
