package nl.tudelft.b_b_w;

import android.content.Context;

import java.util.List;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.controller.BlockVerificationController;
import nl.tudelft.b_b_w.controller.TrustController;
import nl.tudelft.b_b_w.model.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.model.HashException;

/**
 * Performs the actions on the blockchain
 */
public final class API {

    /**
     * Handlers which are used
     */
    private static BlockController blockController;
    private static BlockVerificationController blockVerificationController;


    /**
     * Intitialising the API, the genesis block is here created since this
     * can only be done once per user
     *
     * @param owner   the User
     * @param context The state of the program
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public API(User owner, Context context) throws HashException, BlockAlreadyExistsException {
        blockController = new BlockController(owner, context);
        blockVerificationController = new BlockVerificationController(context);
        blockController.createGenesis(owner);
    }

    /**
     * Method to add a contact to your chain
     *
     * @param contact the contact
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void addContactToChain(User contact)
            throws HashException, BlockAlreadyExistsException {
        blockController.addBlockToChain(contact);
    }

    /**
     * Method to revoke a contact to your chain
     *
     * @param contact the contact
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void revokeContactFromChain(User contact)
            throws HashException, BlockAlreadyExistsException {
        blockController.revokeBlockFromChain(contact);
    }

    /**
     * Method to add a contact to your database
     *
     * @param contact the contact
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void addContactToDatabase(User owner, User contact)
            throws HashException, BlockAlreadyExistsException {
        blockController.createKeyBlock(owner, contact);
    }

    /**
     * Method to add a revoked contact to your database
     *
     * @param contact the contact
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void addRevokeContactToDatabase(User owner, User contact)
            throws HashException, BlockAlreadyExistsException {
        blockController.createRevokeBlock(owner, contact);
    }

    /**
     * Method to return the chain of a specific user
     *
     * @param owner The user
     * @return the chain of the user
     */
    public static List<Block> getBlocks(User owner) {
        return blockController.getBlocks(owner);
    }

    /**
     * Method to check whether the database is empty or not
     *
     * @return true if the database is empty, false otherwise
     */
    public static boolean isDatabaseEmpty() {
        return blockVerificationController.isDatabaseEmpty();
    }

    /**
     * Method to update the trustValue of a block after successful transaction
     *
     * @param block the specific block
     */
    public static void successfulTransaction(Block block) {
        Block updatedBlock = TrustController.succesfulTransaction(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to update the trustValue of a block after failed transaction
     *
     * @param block the specific block
     */
    public static void failedTransaction(Block block) {
        Block updatedBlock = TrustController.failedTransaction(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to update the trustValue of a block after successful verification
     *
     * @param block the specific block
     */
    public static void verifyIBAN(Block block) {
        Block updatedBlock = TrustController.verifiedIBAN(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to update the trustValue of a block after revoking
     *
     * @param block the specific block
     */
    public static void revokedBlock(Block block) {
        Block updatedBlock = TrustController.revokeBlock(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }
}
