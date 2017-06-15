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
import nl.tudelft.b_b_w.database.write.BlockAddQuery;
import nl.tudelft.b_b_w.database.write.UpdateTrustQuery;
import nl.tudelft.b_b_w.model.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.TrustValues;


public class BlockController {

    private User chainOwner;
    private Database database;
    private final Hash notAvailable = new Hash("N/A");
    private final int firstSequenceNumber = 1;

    public BlockController(User chainOwner, Context context) {
        this.chainOwner = chainOwner;
        this.database = new Database(context);
    }

    public final void addBlockToChain(User user) throws HashException, BlockAlreadyExistsException {
        createKeyBlock(chainOwner, user);
    }

    public final void revokeBlockFromChain(User user) throws HashException,
            BlockAlreadyExistsException {
        createRevokeBlock(chainOwner, user);
    }

    public final void addBlock(Block block) throws BlockAlreadyExistsException {
        if (blockExists(block)) {
            throw new BlockAlreadyExistsException();
        }
        BlockAddQuery query = new BlockAddQuery(block);
        database.write(query);
    }

    public final boolean blockExists(Block block) {
        BlockExistQuery query = new BlockExistQuery(block);
        database.read(query);
        return query.blockExists();
    }

    public final void updateBlock(Block block) {
        UpdateTrustQuery query = new UpdateTrustQuery(block);
        database.write(query);
    }

    public final List<Block> getBlocks(User owner) throws HashException {
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

    private List<Block> removeBlock(List<Block> list, Block block) {
        final List<Block> res = new ArrayList<>();
        for (Block listBlock : list) {
            if (!(listBlock.getBlockOwner().equals(block.getBlockOwner())
                    && listBlock.getContact().equals(block.getContact()))) {
                res.add(listBlock);
            }
        }
        return res;
    }

    private final Block getLatestBlock(User owner) throws HashException {
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
        addBlock(genesisBlock);
        return genesisBlock;
    }

    /**
     * Create a block which adds a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     *
     * @param contact   of whom is the information
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
     * @param contact   of whom is the information
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
        if (owner.equals(contact)) {
            contactBlockHash = notAvailable;
        } else {
            contactBlockHash = getBlocks(contact).get(0).getOwnHash();
        }
        int seqNumber = latest.getSequenceNumber() + 1;

        BlockData blockData = new BlockData(blockType, seqNumber, previousBlockHash,
                contactBlockHash, TrustValues.INITIALIZED.getValue()
        );
        final Block block = new Block(owner, contact, blockData);
        addBlock(block);
        return block;
    }
}
