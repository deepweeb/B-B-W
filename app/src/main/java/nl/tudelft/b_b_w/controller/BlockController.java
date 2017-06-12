package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.model.GetDatabaseHandler;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.MutateDatabaseHandler;
import nl.tudelft.b_b_w.model.TrustValues;
import nl.tudelft.b_b_w.model.User;
import nl.tudelft.b_b_w.model.block.Block;
import nl.tudelft.b_b_w.model.block.BlockData;
import nl.tudelft.b_b_w.model.block.BlockFactory;
import nl.tudelft.b_b_w.model.block.BlockType;

/**
 * Created by Ashay on 08/06/2017.
 */

public class BlockController {

    private User chainOwner;
    private GetDatabaseHandler getDatabaseHandler;
    private MutateDatabaseHandler mutateDatabaseHandler;
    private final String notAvailable = "N/A";
    private final int firstSequenceNumber = 1;

    public BlockController(User chainOwner, Context context) {
        this.chainOwner = chainOwner;
        this.getDatabaseHandler = new GetDatabaseHandler(context);
        this.mutateDatabaseHandler = new MutateDatabaseHandler(context);
    }

    private final boolean blockExists(String owner, String key, boolean revoked) {
        return getDatabaseHandler.blockExists(owner, key, revoked);
    }

    public final void addBlockToChain(User user) throws HashException {
        createKeyBlock(chainOwner, user);
    }

    public final void revokeBlockFromChain(User user) throws HashException {
        createRevokeBlock(chainOwner, user);
    }

    public final void addBlock(Block block) {
        if (getDatabaseHandler.containsRevoke(block.getOwner().getName(), block.getPublicKey())) {
            throw new RuntimeException("Block already revoked");
        } else if (blockExists(block.getOwner().getName(), block.getPublicKey(), block.isRevoked())) {
            throw new RuntimeException("block already exists");
        }
        mutateDatabaseHandler.addBlock(block);
    }

    public final List<Block> getBlocks(String owner) throws HashException {
        // retrieve all blocks in the database and then sort it in order of sequence number
        List<Block> blocks = getDatabaseHandler.getAllBlocks(owner);
        List<Block> res = new ArrayList<>();

        for (Block block : blocks) {
            if (block.isRevoked()) {
                res = removeBlock(res, block);
            } else {
                res.add(block);
            }
        }
        return res;
    }

    public final Block getLatestBlock(String owner) throws HashException {
        return getDatabaseHandler.getLatestBlock(owner);
    }

    public final int getLatestSeqNumber(String owner) {
        return getDatabaseHandler.lastSeqNumberOfChain(owner);
    }

    public final List<Block> revokeBlock(Block block) throws HashException {
        final String owner = block.getOwner().getName();
        addBlock(BlockFactory.getBlock(
                "REVOKE",
                owner,
                getLatestSeqNumber(owner) + 1,
                block.getOwnHash(),
                block.getPreviousHashChain(),
                block.getPreviousHashSender(),
                block.getPublicKey(),
                block.getOwner().getIban(),
                block.getTrustValue()));
        return getBlocks(owner);
    }

    public final List<Block> removeBlock(List<Block> list, Block block) {
        final List<Block> res = new ArrayList<>();
        for (Block blc : list) {
            if (!(blc.getOwner().equals(block.getOwner()) && blc.getPublicKey().equals(block.
                    getPublicKey()))) {
                res.add(blc);
            }
        }
        return res;
    }

    /**
     * Create genesis block for an owner
     *
     * @param owner Owner of the block
     * @return the freshly created block
     * @throws Exception when the key hashing method does not work
     */
    public Block createGenesis(User owner) throws HashException {
        BlockData blockData = new BlockData();
        blockData.setBlockType(BlockType.GENESIS);
        blockData.setSequenceNumber(firstSequenceNumber);
        blockData.setOwner(owner);
        blockData.setIban(owner);
        blockData.setPreviousHashChain(notAvailable);
        blockData.setPreviousHashSender(notAvailable);
        blockData.setPublicKey(owner.generatePublicKey());
        blockData.setTrustValue(TrustValues.INITIALIZED.getValue());
        final Block block = BlockFactory.createBlock(blockData);
        addBlock(block);
        return block;
    }

    /**
     * Create a block which adds a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     *
     * @param contact   of whom is the information
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    public Block createKeyBlock(User owner, User contact) throws
            HashException {
        return createBlock(owner, contact, BlockType.ADD_KEY);
    }

    /**
     * Create a block which revokes a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     *
     * @param contact   of whom is the information
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    public Block createRevokeBlock(User owner, User contact)
            throws HashException {
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
     * @throws Exception when the hashing algorithm is not available
     */
    private Block createBlock(User owner, User contact,
            BlockType blockType) throws HashException {
        Block latest = getLatestBlock(owner.getName());
        if (latest == null) {
            throw new IllegalArgumentException("No genesis found for user " + owner);
        }
        String previousBlockHash = latest.getOwnHash();

        // always link to genesis of contact blocks
        String contactBlockHash;
        if (owner.equals(contact)) {
            contactBlockHash = notAvailable;
        } else {
            contactBlockHash = getBlocks(contact.getName()).get(0).getOwnHash();
        }
        int seqNumber = latest.getSequenceNumber() + 1;

        BlockData blockData = new BlockData();
        blockData.setBlockType(blockType);
        blockData.setSequenceNumber(seqNumber);
        blockData.setPreviousHashChain(previousBlockHash);
        blockData.setPreviousHashSender(contactBlockHash);
        blockData.setOwner(owner);
        blockData.setIban(contact);
        blockData.setPublicKey(contact.generatePublicKey());
        blockData.setTrustValue(TrustValues.INITIALIZED.getValue());
        final Block block = BlockFactory.createBlock(blockData);
        addBlock(block);
        return block;
    }
}
