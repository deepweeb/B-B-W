package nl.tudelft.b_b_w.controller;

import android.content.Context;
import android.content.res.Resources;

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
 * Performs the actions on the blockchain
 */

public class BlockController implements BlockControllerInterface {
    @Deprecated
    private static final String REVOKE = "REVOKE";

    /**
     * For when info is not available.
     */
    private static final String NA = "N/A";

    /**
     * Databasehandlers to use
     */
    private GetDatabaseHandler getDatabaseHandler;
    private MutateDatabaseHandler mutateDatabaseHandler;

    /**
     * Constructor to initialize all the involved entities
     *
     * @param _context the instance
     */
    public BlockController(Context _context) {
        this.getDatabaseHandler = new GetDatabaseHandler(_context);
        this.mutateDatabaseHandler = new MutateDatabaseHandler(_context);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final boolean blockExists(String owner, String key, boolean revoked) {
        return getDatabaseHandler.blockExists(owner, key, revoked);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final List<Block> addBlockToChain(Block block) throws HashException {
        // Check if the block already exists
        String owner = block.getOwner().getName();
        Block latest = getDatabaseHandler.getLatestBlock(owner);

        if (latest == null) {
            mutateDatabaseHandler.addBlock(block);
        } else if (latest.isRevoked()) {
            throw new RuntimeException("Error - Block is already revoked");
        } else {
            if (block.isRevoked()) {
                latest = revokedTrustValue(latest);
                mutateDatabaseHandler.updateBlock(latest);
                mutateDatabaseHandler.addBlock(block);
            } else {
                throw new RuntimeException("Error - Block already exists");
            }
        }
        return getBlocks(owner);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final void addBlock(Block block) {

        if (blockExists(block.getOwner().getName(), block.getPublicKey(), block.isRevoked()))
            throw new RuntimeException("block already exists");
        mutateDatabaseHandler.addBlock(block);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final void clearAllBlocks() {
        mutateDatabaseHandler.clearAllBlocks();
    }

    /**
     * @inheritDoc
     */
    @Override
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

    /**
     * @inheritDoc
     */
    @Override
    public final String getContactName(String hash) throws HashException {
        return getDatabaseHandler.getContactName(hash);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block getLatestBlock(String owner) throws HashException {
        return getDatabaseHandler.getLatestBlock(owner);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final int getLatestSeqNumber(String owner) {
        return getDatabaseHandler.lastSeqNumberOfChain(owner);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final List<Block> revokeBlock(Block block) throws HashException {
        String owner = block.getOwner().getName();
        addBlock(BlockFactory.getBlock(
                REVOKE,
                owner,
                getLatestSeqNumber(owner) + 1,
                block.getOwnHash(),
                block.getPreviousHashChain(),
                block.getPreviousHashSender(),
                block.getPublicKey(),
                block.getOwner().getIBAN(),
                block.getTrustValue()));
        return getBlocks(owner);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final List<Block> removeBlock(List<Block> list, Block block) {
        List<Block> res = new ArrayList<>();
        for (Block blc : list) {
            if (!(blc.getOwner().equals(block.getOwner()) && blc.getPublicKey().equals(block.
                    getPublicKey()))) {
                res.add(blc);
            }
        }
        return res;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block verifyIBAN(Block block) {
        block.setTrustValue(TrustValues.VERIFIED.getValue());
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block successfulTransaction(Block block) {
        final int maxCeil = 100;
        final int newTrust = block.getTrustValue() + TrustValues.SUCCESFUL_TRANSACTION.getValue();
        if (newTrust > maxCeil) block.setTrustValue(maxCeil);
        block.setTrustValue(newTrust);
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block failedTransaction(Block block) {
        final int minCeil = 0;
        final int newTrust = block.getTrustValue() + TrustValues.FAILED_TRANSACTION.getValue();
        if (newTrust < minCeil) block.setTrustValue(minCeil);
        block.setTrustValue(newTrust);
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block revokedTrustValue(Block block) {
        block.setTrustValue(TrustValues.REVOKED.getValue());
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final boolean isDatabaseEmpty() {
        return getDatabaseHandler.isDatabaseEmpty();
    }

    /**
     * Create genesis block for an owner
     * @param owner Owner of the block
     * @return the freshly created block
     * @throws Exception when the key hashing method does not work
     */
    public Block createGenesis(User owner) throws HashException {
        BlockData blockData = new BlockData();
        blockData.setBlockType(BlockType.GENESIS);
        blockData.setSequenceNumber(1);
        blockData.setOwner(owner);
        blockData.setIban(owner);
        blockData.setPreviousHashChain(NA);
        blockData.setPreviousHashSender(NA);
        blockData.setPublicKey(NA);
        Block block = BlockFactory.createBlock(blockData);
        addBlock(block);
        return block;
    }

    /**
     * Create a block which adds a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     *
     * @param owner     owner of the block
     * @param contact   of whom is the information
     * @param publicKey public key you want to store
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    public Block createKeyBlock(User owner, User contact, String publicKey) throws
            HashException {
        return createBlock(owner, contact, publicKey, contact.getIBAN(), BlockType.ADD_KEY);
    }

    /**
     * Create a block which revokes a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     *
     * @param owner     owner of the block
     * @param contact   of whom is the information
     * @param publicKey public key you want to store
     * @param iban      IBAN number to store in this block
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    public Block createRevokeBlock(User owner, User contact, String publicKey, String iban)
            throws HashException {
        return createBlock(owner, contact, publicKey, iban, BlockType.REVOKE_KEY);
    }

    /**
     * Creates a block with given revoke status. The block is added to the blockchain automatically
     * with all fields set correctly.
     *
     * @param owner     owner of the block
     * @param contact   of whom is the information
     * @param publicKey public key you want to store
     * @param iban      IBAN number to store in this block
     * @param blockType type of the block
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    private Block createBlock(User owner, User contact, String publicKey, String iban,
                              BlockType blockType) throws HashException {
        Block latest = getLatestBlock(owner.getName());
        if (latest == null)
            throw new IllegalArgumentException("No genesis found for user " + owner);
        String previousBlockHash = latest.getOwnHash();

        // always link to genesis of contact blocks
        String contactBlockHash;
        if (owner.equals(contact)) {
            contactBlockHash = NA;
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
        blockData.setPublicKey(publicKey);
        blockData.setTrustValue(TrustValues.INITIALIZED.getValue());
        Block block = BlockFactory.createBlock(blockData);
        addBlock(block);
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Block backtrack(Block block) throws HashException {
        String previousHashSender = block.getPreviousHashSender();
        Block loopBlock = block;

        while (!previousHashSender.equals("N/A")) {
            loopBlock = getDatabaseHandler.getByHash(previousHashSender);
            if (loopBlock == null) throw new
                    Resources.NotFoundException("Error - Block cannot be backtracked: " + block.toString());
            previousHashSender = loopBlock.getPreviousHashSender();
        }

        return loopBlock;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean verifyTrustworthiness(Block block) throws HashException {
        return blockExists(block.getOwner().getName(), block.getPublicKey(), block.isRevoked())
                && block.verifyBlock(backtrack(block));
    }
}
