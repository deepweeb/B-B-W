package nl.tudelft.b_b_w;

import android.content.Context;

import java.util.List;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.controller.BlockVerificationController;
import nl.tudelft.b_b_w.controller.TrustController;
import nl.tudelft.b_b_w.exception.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.exception.HashException;

/**
 * Performs the actions on the blockchain
 */
public final class API {

    /**
     * Private constructor to make sure that the class cannot be initialized
     */
    private API() {

    }

    /**
     * Method to add a contact to your chain
     *
     * @param owner   the User
     * @param context The state of the program
     * @param contact the contact
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void addContactToChain(User owner, Context context, User contact)
            throws HashException, BlockAlreadyExistsException {
        new BlockController(owner, context).addBlockToChain(contact);
    }

    /**
     * Method to revoke a contact to your chain
     *
     * @param owner   the User
     * @param context The state of the program
     * @param contact the contact
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void revokeContactFromChain(User owner, Context context, User contact)
            throws HashException, BlockAlreadyExistsException {
        new BlockController(owner, context).revokeBlockFromChain(contact);
    }

    /**
     * Method to add a contact to your database
     *
     * @param owner   the User
     * @param context The state of the program
     * @param contact the contact
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void addContactToDatabase(User owner, Context context, User contact)
            throws HashException, BlockAlreadyExistsException {
        new BlockController(owner, context).createKeyBlock(owner, contact);
    }

    /**
     * Method to add a revoked contact to your database
     *
     * @param owner   the User
     * @param context The state of the program
     * @param contact the contact
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void addRevokeContactToDatabase(User owner, Context context, User contact)
            throws HashException, BlockAlreadyExistsException {
        new BlockController(owner, context).createRevokeBlock(owner, contact);
    }

    /**
     * Method to return the chain of a specific user
     *
     * @param owner   the User
     * @param context The state of the program
     * @return the chain of the user
     * @throws HashException when the hashing algorithm is unavailable
     */
    public static List<Block> getBlocks(User owner, Context context) throws HashException {
        return new BlockController(owner, context).getBlocks(owner);
    }

    /**
     * Method to check whether the database is empty or not
     *
     * @param context The state of the program
     * @return true if the database is empty, false otherwise
     */
    public static boolean isDatabaseEmpty(Context context) {
        return new BlockVerificationController(context).isDatabaseEmpty();
    }

    /**
     * Method to update the trustValue of a block after successful transaction
     *
     * @param owner   the User
     * @param context The state of the program
     * @param block   the specific block
     * @throws HashException when the hashing algorithm is unavailable
     */
    public static void successfulTransaction(User owner, Context context, Block block) throws HashException {
        Block updatedBlock = TrustController.successfulTransaction(block);
        new BlockController(owner, context).updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to update the trustValue of a block after failed transaction
     *
     * @param owner   the User
     * @param context The state of the program
     * @param block   the specific block
     * @throws HashException when the hashing algorithm is unavailable
     */
    public static void failedTransaction(User owner, Context context, Block block) throws HashException {
        Block updatedBlock = TrustController.failedTransaction(block);
        new BlockController(owner, context).updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to update the trustValue of a block after successful verification
     *
     * @param owner   the User
     * @param context The state of the program
     * @param block   the specific block
     * @throws HashException when the hashing algorithm is unavailable
     */
    public static void verifyIBAN(User owner, Context context, Block block) throws HashException {
        Block updatedBlock = TrustController.verifiedIBAN(block);
        new BlockController(owner, context).updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to update the trustValue of a block after revoking
     *
     * @param owner   the User
     * @param context The state of the program
     * @param block   the specific block
     * @throws HashException when the hashing algorithm is unavailable
     */
    public static void revokedBlock(User owner, Context context, Block block) throws HashException {
        Block updatedBlock = TrustController.revokeBlock(block);
        new BlockController(owner, context).updateTrustOfBlock(updatedBlock);
    }
}
