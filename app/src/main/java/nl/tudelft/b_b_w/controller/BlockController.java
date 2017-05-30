package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;
import nl.tudelft.b_b_w.model.DatabaseHandler;
import nl.tudelft.b_b_w.model.TrustValues;

/**
 * Performs the actions on the blockchain
 */

public class BlockController {
    /** Context of the block database */
    private Context context;

    /** Databasehandler to use */
    private DatabaseHandler databaseHandler;

    /**
     * contructor to initialize all the involved entities
     *
     * @param _context the instance
     */
    public BlockController(Context _context) {
        this.context = _context;
        this.databaseHandler = new DatabaseHandler(this.context);
    }

    /**
     * Check if a block already exists in the database
     * @param owner owner of the block
     * @param key public key in the block
     * @param revoked whether the block is revoked
     * @return if the block already exists
     */
    public final boolean blockExists(String owner, String key, boolean revoked) {
        return databaseHandler.blockExists(owner, key, revoked);
    }


    /**
     * adding a block to the blockchain
     *
     * @param block Block you want to add
     * @return returns the block you added
     */
    public final List<Block> addBlockToChain(Block block) {
        // Check if the block already exists
        String owner = block.getOwner();
        Block latest = databaseHandler.getLatestBlock(owner);

        if (latest == null) {
            databaseHandler.addBlock(block);
        } else if (latest.isRevoked()) {
            throw new RuntimeException("Error - Block is already revoked");
        } else {
            if (block.isRevoked()) {
                revokedTrustValue(latest);
                databaseHandler.updateBlock(latest);
                databaseHandler.addBlock(block);
            }
            else {
                throw new RuntimeException("Error - Block already exists");
            }
        }

        return getBlocks(owner);
    }


    /**
     * Add a block to the database with checking if the (owner,pubkey) pair
     * is already added to the database
     *
     * @param block Block you want to add
     */
    public final void addBlock(Block block) {

        if (blockExists(block.getOwner(), block.getPublicKey(), block.isRevoked()))
            throw new RuntimeException("block already exists");

        databaseHandler.addBlock(block);
    }

    /**
     * Clears all blocks from the database
     */
    public final void clearAllBlocks() {
        databaseHandler.clearAllBlocks();
    }

    /**
     * Get all blocks that are not revoked in sorted order
     *
     * @return List of all the blocks
     */
    public final List<Block> getBlocks(String owner) {
        // retrieve all blocks in the database and then sort it in order of sequence number
        List<Block> blocks = databaseHandler.getAllBlocks(owner);
        List < Block > res = new ArrayList<>();

        for (Block block : blocks) {
            if (block.isRevoked()) {
                res = removeBlock(res, block);
            } else {
                res.add(block);
            }
        }
        return res;
    }


    /**
     * Function to backtrace the contact name given the hash that refer to their block
     * @param hash hash of the block which owner name we want to find from
     * @return name of owner
     */
    public final String getContactName(String hash) {
        return databaseHandler.getContactName(hash);
    }

    /**
     * Get the latest block of a specific owner
     *
     * @return a Block object, which is the newest block of the owner
     */
    public final Block getLatestBlock(String owner) {
        return databaseHandler.getLatestBlock(owner);
    }

    /**
     * Get the latest sequence number of the chain of a specific owner
     *
     * @return an integer which is the latest sequence number of the chain
     */
    public final int getLatestSeqNumber(String owner) {
        return databaseHandler.lastSeqNumberOfChain(owner);
    }

    /**
     *
     * Revoke a block from the blockchain by adding the same
     * block but setting revoked on true
     *
     * @param block The block you want to revoke
     * @return the revoked block
     */
    public final List<Block> revokeBlock(Block block) {
        String owner = block.getOwner();
        Block newBlock = BlockFactory.getBlock("REVOKE", block.getOwner(),
                block.getOwnHash(), block.getPreviousHashChain(), block.getPreviousHashSender(),
                block.getPublicKey(), block.getIban(), block.getTrustValue());
        addBlock(newBlock);
        return getBlocks(owner);
    }

    /**
     * Method for removing a certain block from a given list
     *
     * @param list  The list of all the blocks
     * @param block The revoke block
     * @return List without the revoked block
     */
    public final List<Block> removeBlock(List<Block> list, Block block) {
        List<Block> res = new ArrayList<>();
        for (Block blc : list) {
            if (!(blc.getOwner().equals(block.getOwner()) && blc.getPublicKey().equals(block.getPublicKey()))) {
                res.add(blc);
            }
        }
        return res;
    }

    /**
     * verifyIBAN method
     * updates the trust value of the block to the set trust value for a verified IBAN
     * @param block given block to update
     * @return block that is updated
     */
    public final Block verifyIBAN(Block block) {
        block.setTrustValue(TrustValues.VERIFIED.getValue());
        return block;
    }

    /**
     * successfulTransaction method
     * updates the trust value of the block to the set trust value for a succesful transaction
     * @param block given block to update
     * @return block that is updated
     */
    public final Block successfulTransaction(Block block) {
        block.setTrustValue(block.getTrustValue() + TrustValues.SUCCESFUL_TRANSACTION.getValue());
        return block;
    }

    /**
     * failedTransaction method
     * updates the trust value of the block to the set trust value for a failed transaction
     * @param block given block to update
     * @return block that is updated
     */
    public final Block failedTransaction(Block block) {
        block.setTrustValue(block.getTrustValue() + TrustValues.FAILED_TRANSACTION.getValue());
        return block;
    }

    /**
     * revokedTrustValue method
     * updates the trust value of the block to the set trust value for a revoked block
     * @param block given block to update
     * @return block that is updated
     */
    public final Block revokedTrustValue(Block block) {
        block.setTrustValue(TrustValues.REVOKED.getValue());
        return block;
    }

    /**
     * Check if the database is empty.
     * @return if the database is empty
     */
    public final boolean isDatabaseEmpty() {
        return databaseHandler.isDatabaseEmpty();
    }

    /**
     * Create genesis block for an owner
     * @param owner the new owner of the block
     * @return the freshly created block
     * @throws Exception when the key hashing method does not work
     */
    public Block createGenesis(String owner) throws Exception {
        String chainHash = "N/A";
        String senderHash = "N/A";
        String publicKey = "N/A";
        String iban = "N/A";
        ConversionController conversionController = new ConversionController(owner, publicKey, chainHash, senderHash, iban);
        String hash = conversionController.hashKey();
        Block block = new Block(1, owner, hash, chainHash, senderHash, publicKey, iban, 0, false);
        addBlock(block);
        return block;
    }

    /**
     * Create a block which adds a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     * @param owner owner of the block
     * @param contact of whom is the information
     * @param publicKey public key you want to store
     * @param iban IBAN number to store in this block
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    public Block createKeyBlock(String owner, String contact, String publicKey, String iban) throws Exception {
        return createBlock(owner, contact, publicKey, iban, false);
    }

    /**
     * Create a block which revokes a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     * @param owner owner of the block
     * @param contact of whom is the information
     * @param publicKey public key you want to store
     * @param iban IBAN number to store in this block
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    public Block createRevokeBlock(String owner, String contact, String publicKey, String iban) throws Exception {
        return createBlock(owner, contact, publicKey, iban, true);
    }

    /**
     * Creates a block with given revoke status. The block is added to the blockchain automatically
     * with all fields set correctly.
     * @param owner owner of the block
     * @param contact of whom is the information
     * @param publicKey public key you want to store
     * @param iban IBAN number to store in this block
     * @param revoke whether to revoke?
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    private Block createBlock(String owner, String contact, String publicKey, String iban, boolean revoke) throws Exception {
        Block latest = getLatestBlock(owner);
        String previousBlockHash = latest.getOwnHash();

        // always link to genesis of contact blocks
        String contactBlockHash;
        if (owner.equals(contact))
            contactBlockHash = "N/A";
        else
            contactBlockHash = getBlocks(contact).get(0).getOwnHash();
        int index = latest.getSequenceNumber() + 1;

        ConversionController conversionController = new ConversionController(
                owner, publicKey, previousBlockHash, contactBlockHash, iban
        );
        String hash = conversionController.hashKey();
        Block block = new Block(index, owner, hash, previousBlockHash, contactBlockHash, publicKey, iban, 0, revoke);
        addBlock(block);
        return block;
    }
}
