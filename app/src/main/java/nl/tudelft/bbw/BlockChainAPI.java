package nl.tudelft.bbw;

import android.content.Context;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

import nl.tudelft.bbw.blockchain.Acquaintance;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.BlockController;
import nl.tudelft.bbw.controller.BlockVerificationController;
import nl.tudelft.bbw.controller.ED25519;
import nl.tudelft.bbw.controller.TrustController;
import nl.tudelft.bbw.database.DatabaseException;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

/**
 * This class represents the API of the application
 * Contains various methods which let the user access all functionality of the application
 */
public final class BlockChainAPI {

    /**
     * All the global variables of the BlockChainAPI
     */
    private static BlockController blockController;
    private static BlockVerificationController blockVerificationController;
    private static User owner;


    /**
     * Private constructor to make sure that the class cannot be initialized
     */
    private BlockChainAPI() {}
    
    /**
     * Initializing the BlockChainAPI, the genesis block is here created since this
     * can only be done once per user
     *
     * @param Name    of the BlockChainAPI user
     * @param Iban    of the BlockChainAPI user
     * @param context The state of the program
     * @return User object of the BlockChainAPI user
     * @throws HashException               when the hash methods are not available
     * @throws BlockAlreadyExistsException when a block already exists
     */
    public static User initializeAPI(String Name, String Iban, Context context) throws
            HashException, BlockAlreadyExistsException {
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        owner = new User(Name, Iban, ED25519.getPublicKey(privateKey));
        owner.setPrivateKey(privateKey);
        blockController = new BlockController(owner, context);
        blockVerificationController = new BlockVerificationController(context);
        blockController.createGenesis(owner);
        return owner;
    }

    /**
     * Method to add the multichain (database) of the pairing partner right after the pairing process
     * so you can look up his/her contact & find his/her genesis block
     *
     * @param acquaintance object of your pairing partner
     * @throws BlockAlreadyExistsException when a block already exists
     * @throws HashException               when the hash methods are not available
     */
    public static void addAcquaintanceMultichain(Acquaintance acquaintance) throws
            BlockAlreadyExistsException, HashException {
        blockController.addMultichain(acquaintance.getMultichain());
    }

    /**
     * @param user which you want to add to your contact list
     * @return The block which represent the contact in your chain
     * @throws HashException               when the hash methods are not available
     * @throws BlockAlreadyExistsException when a block already exists
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static Block addContact(User user)
            throws HashException, BlockAlreadyExistsException, NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        try {
            final byte[] message = user.getPublicKey().getEncoded();
            final byte[] signature = ED25519.generateSignature(message, owner.getPrivateKey());
            return blockController.addBlockToChain(user, signature, message);
        } catch (DatabaseException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Method to revoke a contact from you contact list (your chain)
     *
     * @param contact the User object you want to revoke from your chain
     * @throws HashException               when the hash methods are not available
     * @throws BlockAlreadyExistsException when a block already exists
     */
    public static void revokeContact(User contact)
            throws HashException, BlockAlreadyExistsException {
        //Revoke a block in own chain (adding a revoke block to the chain)
        Block revokedBlock = blockController.revokeBlockFromChain(contact);
        //update the trustValue of a block after revoking
        Block updatedBlock = TrustController.revokeBlock(revokedBlock);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to return the chain of a specific user
     * This method can also be used on yourself
     *
     * @return the chain of the user, including that user's genesis block
     */
    public static List<Block> getContactsOf(User owner) {
        return blockController.getBlocks(owner);
    }

    /**
     * Method to check whether the database, where the blocks are stored, is empty or not
     *
     * @return true if the database is empty, false otherwise
     */
    public static boolean isDatabaseEmpty() {
        return blockVerificationController.isDatabaseEmpty();
    }

    /**
     * Method to update the Trust Value of a block after successful transaction
     *
     * @param block with the desired, updated value
     */
    public static void successfulTransactionTrustUpdate(Block block) {
        Block updatedBlock = TrustController.successfulTransaction(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to update the Trust Value of a block after failed transaction
     *
     * @param block with the desired, updated value
     */
    public static void failedTransactionTrustUpdate(Block block) {
        Block updatedBlock = TrustController.failedTransaction(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to update the Trust Value of a block after successful Iban number verification
     *
     * @param block with the desired, updated value
     */
    public static void verifyIBANTrustUpdate(Block block) {
        Block updatedBlock = TrustController.verifiedIBAN(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    /**
     * Create an acquaintance object so that you can send to the person you are pairing with
     * so he/she can add you.
     *
     * @return Acquaintance object which contains your information and your database
     */
    public static Acquaintance makeAcquaintanceObject() {
        return blockController.makeAcquaintanceObject();
    }
}
