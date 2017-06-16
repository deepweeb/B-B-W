package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.BlockData;
import nl.tudelft.b_b_w.blockchain.BlockType;
import nl.tudelft.b_b_w.blockchain.Hash;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.database.Database;
import nl.tudelft.b_b_w.database.read.BlockExistQuery;
import nl.tudelft.b_b_w.database.read.GetChainQuery;
import nl.tudelft.b_b_w.database.read.LatestBlockQuery;
import nl.tudelft.b_b_w.database.read.UserExistQuery;
import nl.tudelft.b_b_w.database.write.BlockAddQuery;
import nl.tudelft.b_b_w.database.write.UpdateTrustQuery;
import nl.tudelft.b_b_w.database.write.UserAddQuery;
import nl.tudelft.b_b_w.exception.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.exception.HashException;
import nl.tudelft.b_b_w.blockchain.TrustValues;

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
     * Method for adding a user to our blockchain.
     *
     * @param user the user we want to add
     * @return the created block
     * @throws HashException               When there is an error calculating the hash
     * @throws BlockAlreadyExistsException When there already exists a block in the database
     */
    public final Block addBlockToChain(User user)
            throws HashException, BlockAlreadyExistsException {
        UserExistQuery query = new UserExistQuery(user);
        database.read(query);
        if (!query.doesExist()) {
            UserAddQuery existQuery = new UserAddQuery(user);
            database.write(existQuery);
        }
        return createKeyBlock(chainOwner, user);
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
}
