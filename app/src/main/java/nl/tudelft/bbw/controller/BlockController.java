package nl.tudelft.bbw.controller;

import android.content.Context;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.bbw.blockchain.Acquaintance;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.BlockData;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.blockchain.Hash;
import nl.tudelft.bbw.blockchain.TrustValues;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.database.Database;
import nl.tudelft.bbw.database.read.BlockExistQuery;
import nl.tudelft.bbw.database.read.DatabaseToMultichainQuery;
import nl.tudelft.bbw.database.read.GetChainQuery;
import nl.tudelft.bbw.database.read.LatestBlockQuery;
import nl.tudelft.bbw.database.read.UserExistQuery;
import nl.tudelft.bbw.database.write.BlockAddQuery;
import nl.tudelft.bbw.database.write.UpdateTrustQuery;
import nl.tudelft.bbw.database.write.UserAddQuery;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

/**
 * Class which handles the the addition, revocation and creation of blocks.
 */
public class BlockController {

    /**
     * Variables for handling the database
     */
    private User chainOwner;
    private Database database;

    /**
     * Creation of a BlockController
     *
     * @param chainOwner The owner of the blockchain
     * @param context    The specific context which contains our database
     */
    public BlockController(User chainOwner, Context context) {
        this.chainOwner = chainOwner;
        this.database = new Database(context);
    }

    /**
     *Method to return the own chain
     * @return the list of block forming the own chain of the user
     */
    public final List<Block> returnOwnChain()
    {
        return this.getBlocks(chainOwner);
    }

    /**
     * Method for adding a user to our blockchain.
     *
     * @param user the user we want to add
     * @param signature byte array containing the signature
     * @param message byte array containing the message
     * @return the created block
     * @throws HashException               When there is an error calculating the hash
     * @throws BlockAlreadyExistsException When there already exists a block in the database
     */
    public final Block addBlockToChain(User user, byte[] signature, byte[] message)
            throws HashException, BlockAlreadyExistsException {
        UserExistQuery query = new UserExistQuery(user);
        database.read(query);
        if (!query.doesExist()) {
            UserAddQuery existQuery = new UserAddQuery(user);
            database.write(existQuery);
        }
        if (verifySignature(signature, message)) {
            return createKeyBlock(chainOwner, user);
        } else {
            throw new RuntimeException("Block cannot be verified");
        }
    }

    /**
     * Method for revoking a user to our blockchain.
     *
     * @param user the user we want to revoking
     * @return the created revoke block
     * @throws HashException               When there is an error calculating the hash
     * @throws BlockAlreadyExistsException When there already exists a block in the database
     */
    public final Block revokeBlockFromChain(User user) throws HashException,
            BlockAlreadyExistsException {
        return createRevokeBlock(chainOwner, user);
    }

    /**
     * Method for adding a block to our database
     *
     * @param block The block which we want to add
     * @throws BlockAlreadyExistsException When there already exists a block in the database
     */
    public void addBlock(Block block) throws BlockAlreadyExistsException {
        if (blockExists(block)) {
            throw new BlockAlreadyExistsException();
        }
        BlockAddQuery query = new BlockAddQuery(block);
        database.write(query);
    }

    /**
     * Method  for checking whether a block exists in the database
     *
     * @param block Block which we want to check
     * @return true if the block is already present, false if the block isn't present
     */
    public boolean blockExists(Block block) {
        BlockExistQuery query = new BlockExistQuery(block);
        database.read(query);
        return query.blockExists();
    }

    /**
     * Method for updating the trustValue of a block
     *
     * @param block The block which is already present in the database, but different trustValue
     */
    public final void updateTrustOfBlock(Block block) {
        UpdateTrustQuery query = new UpdateTrustQuery(block);
        database.write(query);
    }

    /**
     * Retrieve the owner of this app
     * @return the owner of this app
     */
    public User getOwnUser() {
        return chainOwner;
    }

    /**
     * Method for retrieving the blockchain of a user
     *
     * @param owner The user whose chain we want
     * @return A list containing the block in the blockchain
     */
    public final List<Block> getBlocks(User owner) {
        // retrieve all blocks in the database and then sort it in order of sequence number
        GetChainQuery query = new GetChainQuery(database, owner);
        database.read(query);
        List<Block> blocks = query.getChain();
        List<Block> res = new ArrayList<>();
        for (Block block : blocks) {
            if (block.getBlockType() == BlockType.REVOKE_KEY) {
                res = removeBlock(res, block);
            } else {
                res.add(block);
            }
        }
        return res;
    }

    /**
     * Helper method to remove a specific block in a given list
     *
     * @param list  The list containing blocks
     * @param block The block which we want to remove from the list
     * @return A list without the specific block
     */
    private List<Block> removeBlock(List<Block> list, Block block) {
        final List<Block> res = new ArrayList<>();
        for (Block listBlock : list) {
            if (!(listBlock.getOwnerName().equals(block.getOwnerName())
                    && listBlock.getContactName().equals(block.getContactName()))) {
                res.add(listBlock);
            }
        }
        return res;
    }

    /**
     * Helper method to get the latest block of a blockchain
     *
     * @param owner The owner of the blockchain
     * @return The last block
     */
    private Block getLatestBlock(User owner) {
        LatestBlockQuery query = new LatestBlockQuery(database, owner);
        database.read(query);
        return query.getLatestBlock();
    }

    /**
     * Create genesis block for an owner
     *
     * @param owner Owner of the block
     * @return the freshly created block
     * @throws HashException when the key hashing method does not work
     */
    public Block createGenesis(User owner) throws HashException, BlockAlreadyExistsException {
        Block genesisBlock = new Block(owner);
        UserAddQuery query = new UserAddQuery(owner);
        database.write(query);
        addBlock(genesisBlock);
        return genesisBlock;
    }

    /**
     * Create a block which adds a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     *
     * @param contact of whom is the information
     * @return the newly created block
     * @throws HashException when the hashing algorithm is not available
     */
    public Block createKeyBlock(User owner, User contact) throws
            HashException, BlockAlreadyExistsException {
        return createBlock(owner, contact, BlockType.ADD_KEY);
    }

    /**
     * Create a block which revokes a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     *
     * @param contact of whom is the information
     * @return the newly created block
     * @throws HashException when the hashing algorithm is not available
     */
    public Block createRevokeBlock(User owner, User contact)
            throws HashException, BlockAlreadyExistsException {
        return createBlock(owner, contact, BlockType.REVOKE_KEY);
    }

    /**
     * Creates a block with given revoke status. The block is added to the blockchain automatically
     * with all fields set correctly.
     *
     * @param owner     owner of the block
     * @param contact   of whom is the information
     * @param blockType type of the block
     * @return the newly created block
     * @throws HashException when the hashing algorithm is not available
     */
    private Block createBlock(User owner, User contact,
                              BlockType blockType) throws HashException, BlockAlreadyExistsException {
        Block latest = getLatestBlock(owner);
        if (latest == null) {
            throw new IllegalArgumentException("No genesis found for user " + owner);
        }
        Hash previousBlockHash = latest.getOwnHash();
        // always link to genesis of contact blocks
        Hash contactBlockHash;
        contactBlockHash = getBlocks(contact).get(0).getOwnHash();
        int seqNumber = latest.getSequenceNumber() + 1;

        BlockData blockData = new BlockData(blockType, seqNumber, previousBlockHash,
                contactBlockHash, TrustValues.INITIALIZED.getValue()
        );
        final Block block = new Block(owner, contact, blockData);
        addBlock(block);
        return block;
    }

    /**
     * verifySignature method
     * Verifies the signature given a signature and message byte array
     *
     * @param signature given signature to use
     * @param message given message to verify
     * @return boolean if the signature of the block is verified
     */
    private boolean verifySignature(byte[] signature, byte[] message) {
        try {
            return ED25519.verifySignature(signature, message, chainOwner.getPublicKey());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Method to add the multichain (the pairing person database) into your database
     *
     * @param multichain given the multichain of the pairing partner
     * @throws BlockAlreadyExistsException
     * @throws HashException
     */
    public void addMultichain(List<List<Block>> multichain) throws BlockAlreadyExistsException, HashException {
        if (multichain.isEmpty()) {
            return;
        }
        for (List<Block> chain : multichain) {
            for (Block block : chain) {
                if (block.getBlockType() == BlockType.GENESIS) {
                   this.createGenesis(block.getBlockOwner());
                } else if (block.isRevoked()) {
                   this.createRevokeBlock(block.getBlockOwner(), block.getContact());
                } else {
                    this.createKeyBlock(block.getBlockOwner(), block.getContact());
                }
            }
        }
    }

    /**
     * Create an acquintance object that you can send over the network
r    * @return a new acquaintance object
     */
    public Acquaintance makeAcquaintanceObject() {
        DatabaseToMultichainQuery query = new DatabaseToMultichainQuery( database);
        database.read(query);
        User owner = getOwnUser();
        return new Acquaintance(owner.getName(), owner.getIban(), owner.getPublicKey(),
                query.getMultichain());
    }
}
