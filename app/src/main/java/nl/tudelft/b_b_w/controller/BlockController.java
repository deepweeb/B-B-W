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

public class BlockController implements BlockControllerInterface {

    /**
     * Context of the block database
     */
    private Context context;

    /**
     *  Databasehandler to use
     */
    private DatabaseHandler databaseHandler;

    /**
     * Constructor to initialize all the involved entities
     *
     * @param _context the instance
     */
    public BlockController(Context _context) {
        this.context = _context;
        this.databaseHandler = new DatabaseHandler(this.context);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final boolean blockExists(String owner, String key, boolean revoked) {
        return databaseHandler.blockExists(owner, key, revoked);
    }

    /**
     * @inheritDoc
     */
    @Override
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
     * @inheritDoc
     */
    @Override
    public final void addBlock(Block block) {

        if (blockExists(block.getOwner(), block.getPublicKey(), block.isRevoked()))
            throw new RuntimeException("block already exists");

        databaseHandler.addBlock(block);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final void clearAllBlocks() {
        databaseHandler.clearAllBlocks();
    }

    /**
     * @inheritDoc
     */
    @Override
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
     * @inheritDoc
     */
    @Override
    public final String getContactName(String hash) {
        return databaseHandler.getContactName(hash);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block getLatestBlock(String owner) {
        return databaseHandler.getLatestBlock(owner);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final int getLatestSeqNumber(String owner) {
        return databaseHandler.lastSeqNumberOfChain(owner);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final List<Block> revokeBlock(Block block) {
        String owner = block.getOwner();
        Block newBlock = BlockFactory.getBlock("REVOKE", block.getOwner(),
                block.getOwnHash(), block.getPreviousHashChain(), block.getPreviousHashSender(),
                block.getPublicKey(), block.getIban(), block.getTrustValue());
        addBlock(newBlock);
        return getBlocks(owner);
    }

    /**
     * @inheritDoc
     */
    @Override
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
        block.setTrustValue(block.getTrustValue() + TrustValues.SUCCESFUL_TRANSACTION.getValue());
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block failedTransaction(Block block) {
        block.setTrustValue(block.getTrustValue() + TrustValues.FAILED_TRANSACTION.getValue());
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
        return databaseHandler.isDatabaseEmpty();
    }

}
