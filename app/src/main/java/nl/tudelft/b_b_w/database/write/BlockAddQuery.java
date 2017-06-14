package nl.tudelft.b_b_w.database.write;

import android.content.ContentValues;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.BlockType;
import nl.tudelft.b_b_w.controller.KeyWriter;

import static nl.tudelft.b_b_w.database.Database.BLOCK_TABLE_NAME;
import static nl.tudelft.b_b_w.database.Database.KEY_CONTACT;
import static nl.tudelft.b_b_w.database.Database.KEY_HASH;
import static nl.tudelft.b_b_w.database.Database.KEY_OWNER;
import static nl.tudelft.b_b_w.database.Database.KEY_PREV_HASH_CHAIN;
import static nl.tudelft.b_b_w.database.Database.KEY_PREV_HASH_SENDER;
import static nl.tudelft.b_b_w.database.Database.KEY_REVOKE;
import static nl.tudelft.b_b_w.database.Database.KEY_SEQ_NO;
import static nl.tudelft.b_b_w.database.Database.KEY_TRUST_VALUE;

/**
 * Query to add a block to the database
 */
public class BlockAddQuery extends WriteQuery {
    /**
     * The block to add
     */
    private Block block;

    /*
     * Create a query to add a block
     * @param block the block to add
     */
    public BlockAddQuery(Block block) {
        this.block = block;
    }

    /**
     * Put all our block attributes in the content values
     * @return
     */
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_SEQ_NO, block.getSequenceNumber());
        values.put(KEY_OWNER, KeyWriter.publicKeyToString(block.getOwnerPublicKey()));
        values.put(KEY_CONTACT, KeyWriter.publicKeyToString(block.getContactPublicKey()));
        values.put(KEY_HASH, block.getOwnHash().toString());
        values.put(KEY_PREV_HASH_CHAIN, block.getPreviousHashChain().toString());
        values.put(KEY_PREV_HASH_SENDER, block.getPreviousHashSender().toString());
        values.put(KEY_REVOKE, block.getBlockData().getBlockType() == BlockType.REVOKE_KEY);
        values.put(KEY_TRUST_VALUE, block.getTrustValue());
        return values;
    }

    /**
     * This query operates on the blocks table
     * @return the block table name
     */
    @Override
    protected String getTableName() {
        return BLOCK_TABLE_NAME;
    }
}
