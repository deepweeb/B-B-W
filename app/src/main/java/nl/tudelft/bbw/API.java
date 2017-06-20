package nl.tudelft.bbw;

import android.content.Context;

import java.util.List;

import nl.tudelft.bbw.blockchain.Acquaintance;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.BlockController;
import nl.tudelft.bbw.controller.BlockVerificationController;
import nl.tudelft.bbw.controller.TrustController;
import nl.tudelft.bbw.database.read.DatabaseToMultichainQuery;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

/**
 * Performs the actions on the blockchain
 */
public final class API {

    private static BlockController blockController;
    private static BlockVerificationController blockVerificationController;

    /**
     * Private constructor to make sure that the class cannot be initialized
     */
    private API() {

    }

    /**
     * Intitialising the API, the genesis block is here created since this
     * can only be done once per user
     * TODO: Remove this function
     *
     * @param owner   the User
     * @param context The state of the program
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void initializeAPI(User owner, Context context) throws HashException, BlockAlreadyExistsException {
        blockController = new BlockController(owner, context);
        blockVerificationController = new BlockVerificationController(context);
        blockController.createGenesis(owner);
    }

    /**
     * Method to add a contact to your chain
     *
     * @param signature byte array containing the signature
     * @param message   byte array containing the message
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void addAcquaintanceToChain(Acquaintance acquaintance, byte[] signature, byte[] message)
            throws HashException, BlockAlreadyExistsException {

        //Adding his database into your database (so you can look up his contacts)
        blockController.addMultichain(acquaintance.getMultichain());


        //Adding the user into your own chain
        blockController.addBlockToChain(acquaintance, signature, message);
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
        //Revoke a block in own chain (adding a revoke block to the chain)
        Block revokedBlock = blockController.revokeBlockFromChain(contact);

        //update the trustValue of a block after revoking
        Block updatedBlock = TrustController.revokeBlockTrust(revokedBlock);
        blockController.updateTrustOfBlock(updatedBlock);
    }


    /**
     * Method to return the chain of a specific user
     *
     * @return the chain of the user
     */
    public static List<Block> getContactsOf(User owner) {
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
        Block updatedBlock = TrustController.successfulTransaction(block);
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
     * Create an acquintance object that you can send over the network
     *
     * @return a new acquintance object
     */
    public static Acquaintance makeAcquintanceObject() {
        DatabaseToMultichainQuery query = new DatabaseToMultichainQuery(
                blockController.getDatabase());
        blockController.getDatabase().read(query);
        User owner = blockController.getOwnUser();
        return new Acquaintance(owner.getName(), owner.getIban(), owner.getPublicKey(),
                query.getMultichain());
    }


}
