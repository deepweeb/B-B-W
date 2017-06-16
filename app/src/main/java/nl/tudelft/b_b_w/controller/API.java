package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.List;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.User;

/**
 * Performs the actions on the blockchain
 */
public final class API {

    private static BlockController blockController;
    private static BlockVerificationController blockVerificationController;


    public API(User owner, Context context) throws Exception {
        blockController = new BlockController(owner, context);
        blockVerificationController = new BlockVerificationController(context);
        blockController.createGenesis(owner);
    }

    public static void addContactToChain(User contact) throws Exception {
        blockController.addBlockToChain(contact);
    }

    public static void revokeContactFromChain(User contact)
            throws Exception {
        blockController.revokeBlockFromChain(contact);
    }

    public static void addContactToDatabase(User owner, User contact)
            throws Exception {
        blockController.createKeyBlock(owner, contact);
    }

    public static void addRevokeContactToDatabase(User owner, User contact)
            throws Exception {
        blockController.createRevokeBlock(owner, contact);
    }

    public static List<Block> getBlocks(User owner) throws Exception {
        return blockController.getBlocks(owner);
    }

    public static boolean isDatabaseEmpty() {
        return blockVerificationController.isDatabaseEmpty();
    }

    public static void successfulTransaction(Block block) {
        Block updatedBlock = TrustController.successfulTransaction(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    public static void failedTransaction(Block block) {
        Block updatedBlock = TrustController.failedTransaction(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    public static void verifyIBAN(Block block) {
        Block updatedBlock = TrustController.verifiedIBAN(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    public static void revokedBlock(Block block) {
        Block updatedBlock = TrustController.revokeBlock(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }
}
