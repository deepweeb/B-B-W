package nl.tudelft.b_b_w.database.write;

import android.content.ContentValues;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.BlockType;

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
 * Query to update the trust value of a block
 */
public class UpdateTrustQuery extends WriteQuery {
    /**
     * The block to modify
     */
    private Block block;

    /*
     * Create a query to add a block
     * @param block the block to add
     */
    public UpdateTrustQuery(Block block) {
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
        values.put(KEY_OWNER, block.getOwnerPublicKey().toString());
        values.put(KEY_CONTACT, block.getContactPublicKey().toString());
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