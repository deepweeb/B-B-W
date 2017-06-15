package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.List;

import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.blockchain.Block;

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
        return blockController.getBlocks(owner);
    }

    public boolean isDatabaseEmpty() {
        return blockVerificationController.isDatabaseEmpty();
    }

    /**
     * This method adds an updaed block to the chain after a successful transaction.
     * @param block the block that succeeded the transaction.
     */
    public void successfulTransaction(Block block) {
        Block updatedBlock = TrustController.succesfulTransaction(block);
        blockController.addBlock(updatedBlock);
    }

    /**
     * This method adds an updated block to the chain after a failed transaction.
     * @param block the block that failed the transaction.
     */
    public void failedTransaction(Block block) {
        Block updatedBlock = TrustController.failedTransaction(block);
        blockController.addBlock(updatedBlock);
    }

    /**
     * This method in the API verifies the iban.
     * @param block the block that has to be verified.
     */
    public void verifyIBAN(Block block) {
        Block updatedBlock = TrustController.verifiedIBAN(block);
        blockController.addBlock(updatedBlock);
    }

    /**
     * This method in the API revokes a given block.
     * @param block the block that has to be revoked.
     */
    public void revokedBlock(Block block) {
        Block updatedBlock = TrustController.revokeBlock(block);
        blockController.addBlock(updatedBlock);
    }
}
