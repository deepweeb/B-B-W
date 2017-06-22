package nl.tudelft.bbw;

import android.content.Context;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;

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
 * Performs the actions on the blockchain
 */
public final class BlockChainAPI {

    private static BlockController blockController;
    private static BlockVerificationController blockVerificationController;
    private static User owner;

    /**
     * Private constructor to make sure that the class cannot be initialized
     */
    private BlockChainAPI() {

    }

    /**
     * Intitialising the BlockChainAPI, the genesis block is here created since this
     * can only be done once per user
     * TODO: Remove this function
     *
     * @param Name    of the BlockChainAPI user
     * @param Iban    of the BlockChainAPI user
     * @param context The state of the program
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static void initializeAPI(String Name, String Iban, Context context) throws HashException, BlockAlreadyExistsException {
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        owner = new User(Name, Iban, ED25519.getPublicKey(privateKey));
        owner.setPrivateKey(privateKey);
        blockController = new BlockController(owner, context);
        blockVerificationController = new BlockVerificationController(context);
        blockController.createGenesis(owner);
    }

    /**
     * Getter method to get the BlockChainAPI user's contact list
     *
     * @return List of blocks forming this user contact list.
     */
    public static List<Block> getMyContacts() {
        return blockController.getBlocks(owner);
    }

    /**
     * Getter method to get the BlockChainAPI user's name
     *
     * @return name of the BlockChainAPI user
     */
    public static String getMyName() {
        return owner.getName();
    }

    /**
     * Getter method to get the BlockChainAPI user's Iban
     *
     * @return Iban number of the BlockChainAPI user
     */
    public static String getMyIban() {
        return owner.getIban();
    }

    /**
     * Getter method to get the BlockChainAPI user's Public Key
     *
     * @return Public Key  of the BlockChainAPI user
     */
    public static EdDSAPublicKey getMyPublicKey() {
        return owner.getPublicKey();
    }


    public static void addAcquaintanceMultichain(Acquaintance acquaintance) throws BlockAlreadyExistsException, HashException {
        //Adding his database into your database (so you can look up his contacts)
        blockController.addMultichain(acquaintance.getMultichain());
    }


    /**
     * Method to add a contact to your chain
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    public static Block addContact(User user)
            throws HashException, BlockAlreadyExistsException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        try {
            final byte[] message = user.getPublicKey().getEncoded();
            final byte[] signature = ED25519.generateSignature(message, owner.getPrivateKey());
            //Adding the user into your own chain
            return blockController.addBlockToChain(user, signature, message);
        } catch (DatabaseException e) {
            return null;
        }

    }

    /**
     * Method to revoke a contact to your chain
     *
     * @param contact the contact
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
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
    public static void successfulTransactionTrustUpdate(Block block) {
        Block updatedBlock = TrustController.successfulTransaction(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to update the trustValue of a block after failed transaction
     *
     * @param block the specific block
     */
    public static void failedTransactionTrustUpdate(Block block) {
        Block updatedBlock = TrustController.failedTransaction(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    /**
     * Method to update the trustValue of a block after successful verification
     *
     * @param block the specific block
     */
    public static void verifyIBANTrustUpdate(Block block) {
        Block updatedBlock = TrustController.verifiedIBAN(block);
        blockController.updateTrustOfBlock(updatedBlock);
    }

    /**
     * Create an acquaintance object that you can send over the network
     *
     * @return a new acquaintance object
     */
    public static Acquaintance makeAcquaintanceObject() {
        return blockController.makeAcquaintanceObject();
    }

}