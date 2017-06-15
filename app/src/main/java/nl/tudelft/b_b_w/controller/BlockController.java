package nl.tudelft.b_b_w.controller;

import android.app.ActivityManager;
import android.content.Context;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.BlockData;
import nl.tudelft.b_b_w.blockchain.BlockType;
import nl.tudelft.b_b_w.blockchain.Hash;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.database.Database;
import nl.tudelft.b_b_w.database.read.GetChainQuery;
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

    public final void addBlockToChain(User user, byte[] signature, byte[] message) throws HashException {
        if (verifySignature(signature, message)) {
            createKeyBlock(chainOwner, user);
        } else {
            throw new RuntimeException("Block cannot be verified");
        }
    }

    public final void revokeBlockFromChain(User user) throws HashException {
        createRevokeBlock(chainOwner, user);
    }

    public final void addBlock(Block block) {
        if (getDatabaseHandler.containsRevoke(block, block.getPublicKey())) {
            throw new RuntimeException("Block already revoked");
        } else if (blockExists(block.getOwner().getName(), block.getPublicKey(), block.isRevoked())) {
            throw new RuntimeException("block already exists");
        }
        mutateDatabaseHandler.addBlock(block);
    }

    public final boolean blockExists(String owner, String key, boolean revoked) {
        return getDatabaseHandler.blockExists(owner, key, revoked);
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
        for (Block blc : list) {
            if (!(blc.getOwnerName().equals(block.getOwnerName()) && blc.getOwnHash().equals(block.
                    getPublicKey()))) {
                res.add(blc);
            }
        }
        return res;
    }

    private final Block getLatestBlock(String owner) throws HashException {
        return null;//getDatabaseHandler.getLatestBlock(owner);
    }

    /**
     * Create genesis block for an owner
     *
     * @param owner Owner of the block
     * @return the freshly created block
     * @throws HashException when the key hashing method does not work
     */
    public Block createGenesis(User owner) throws HashException {
        BlockData blockData = new BlockData(BlockType.GENESIS, firstSequenceNumber,
                notAvailable, notAvailable, TrustValues.INITIALIZED.getValue());
        Block genesisBlock = new Block(owner, owner, blockData);
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
            HashException {
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
     * @throws HashException when the hashing algorithm is not available
     */
    private Block createBlock(User owner, User contact,
            BlockType blockType) throws HashException {
        Block latest = getLatestBlock(owner.getName());
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

    /**
     * verifySignature method
     * Verifies the signature given a signature and message byte array
     *
     * @param signature given signature to use
     * @param message given message to verify
     * @return boolean if the signature of the block is verified
     */
    boolean verifySignature(byte[] signature, byte[] message) {
        try {
            return ED25519.verifySignature(signature, message, chainOwner.getPublicKey());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
