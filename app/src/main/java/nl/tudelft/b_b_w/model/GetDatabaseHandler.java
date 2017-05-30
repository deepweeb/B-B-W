package nl.tudelft.b_b_w.model;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to create and handle the Database for get requests
 */

public class GetDatabaseHandler extends AbstractDatabaseHandler {

    /**
     * Constructor
     * creates a database connection
     *
     * @param context given context
     */
    public GetDatabaseHandler(Context context) {
        super(context);
    }

    /**
     * Find the last sequence number of the chain of a specific owner
     *
     * @param owner the owner of the chain which you want to get the last sequence number from
     *              There is no need for a public key because each block of the chain is
     *              supposed to have different public key from the contact
     */
    public final int lastSeqNumberOfChain(String owner) {

        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor c = db.query(TABLE_NAME,
                new String[]{"MAX(" + KEY_SEQ_NO + ")"},
                KEY_OWNER + " = ? ",
                new String[]{
                        owner
                }, null, null, null, null)) {
            c.moveToFirst();
            return c.getInt(0);
        }

    }

    /**
     * Function to backtrace the contact name given the hash that refer to their block
     * @param hash hash of the block which owner name we want to find from
     * @return name of owner
     */
    public final String getContactName(String hash) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                _columns,
                KEY_OWN_HASH + " = ? ",
                new String[]{
                        hash
                }, null, null, null, null);

        // When returning an exception the whole program crashes,
        // but we want to preserve the state.
        if (cursor.getCount() < 1) return "Unknown";

        cursor.moveToFirst();

        // Extract block from database
        Block block = extractBlock(cursor);

        db.close();
        cursor.close();

        if (block.getPreviousHashSender().equals("N/A")) {
            return block.getOwner();
        } else {
            return getContactName(block.getPreviousHashSender());
        }
    }

    /**
     * Method to get a specific block
     *
     * @param owner          The owner of the block you want
     * @param publicKey      The owner of the block you want
     * @param sequenceNumber The number of the block in the sequence
     * @return The block you were searching for
     */
    public final Block getBlock(String owner, String publicKey, int sequenceNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                _columns,
                KEY_OWNER + " = ? AND " + KEY_PUBLIC_KEY + " = ? AND " + KEY_SEQ_NO + " = ?",
                new String[]{
                        owner, publicKey, String.valueOf(sequenceNumber)
                }, null, null, null, null);

        //When returning an exception the whole program crashes,
        //but we want to preserve the state.
        if (cursor.getCount() < 1) return null;

        cursor.moveToFirst();

        // Extract block from database
        Block block = extractBlock(cursor);

        // Close database connection
        db.close();

        // Close cursor
        cursor.close();

        // return block
        return block;
    }

    /**
     * Method to check whether the blockchain contains a specific block,
     * uses the getBlock method to avoid duplication
     *
     * @param owner          the owner of the block you want
     * @param publicKey      the publickey of the block you want
     * @param sequenceNumber The number of the block in the sequence
     * @return true if the blockchain contains the specified block, otherwise false
     */
    public final boolean containsBlock(String owner, String publicKey, int sequenceNumber) {
        return this.getBlock(owner, publicKey, sequenceNumber) != null;
    }

    /**
     * Method to check whether the blockchain contains a specific block,
     * uses the getBlock method to avoid duplication
     *
     * @param owner     the owner of the block you want
     * @param publicKey the publickey of the block you want
     * @return true if the blockchain contains the specified block, otherwise false
     */
    public final boolean containsBlock(String owner, String publicKey) {
        return this.getLatestBlock(owner) != null;
    }

    /**
     * Method to get the latest sequence number of a block with a
     * certain owner and publickey
     *
     * @param owner     the owner of the block
     * @param publicKey the owner of the sequence number
     * @return the latest sequence number of the specified block
     */
    public final int getLatestSeqNum(String owner, String publicKey) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_NAME,
                new String[]{"MAX(" + KEY_SEQ_NO + ")"},
                KEY_OWNER + " = ? AND " + KEY_PUBLIC_KEY + " = ?",
                new String[]{
                        owner, publicKey
                }, null, null, null, null);

        if (c.getCount() < 1) return -1;
        c.moveToFirst();

        int result = c.getInt(0);
        db.close();
        c.close();

        return result;

    }

    /**
     * Helper method to construct a block from the database cursor.
     * @param cursor The cursor to extract from
     * @return A freshly constructed block
     */
    private Block extractBlock(Cursor cursor) {
        final String blockType = (cursor.getInt(INDEX_REVOKE) > 0) ? "REVOKE" : "BLOCK";
        Block block = BlockFactory.getBlock(
                blockType,cursor.getString(INDEX_OWNER),
                cursor.getString(INDEX_OWN_HASH),
                cursor.getString(INDEX_PREV_HASH_CHAIN),
                cursor.getString(INDEX_PREV_HASH_SENDER),
                cursor.getString(INDEX_PUBLIC_KEY),
                cursor.getString(INDEX_IBAN_KEY),
                cursor.getInt(INDEX_TRUST_VALUE));
        block.setSeqNumberTo(cursor.getInt(INDEX_SEQ_NO));
        return block;
    }

    /**
     * Method to get the latest block in a blockchain using the
     * owner and publickey
     *
     * @param owner the owner of the block
     * @return the latest block
     */
    public final Block getLatestBlock(String owner) {
        int maxSeqNum = this.lastSeqNumberOfChain(owner);
        SQLiteDatabase db = this.getReadableDatabase();

        if (maxSeqNum == 0){return null;}

        Cursor cursor = db.query(TABLE_NAME,
                _columns,
                KEY_OWNER + " = ? AND " + KEY_SEQ_NO + " = ?",
                new String[]{
                        owner, String.valueOf(maxSeqNum)
                }, null, null, null, null);

        //When returning an exception the whole program crashes,
        //but we want to preserve the state.
        if (cursor.getCount() < 1) return null;

        cursor.moveToFirst();

        Block block = extractBlock(cursor);
        // Close database connection
        db.close();

        // Close cursor
        cursor.close();

        // return block
        return block;
    }

    /**
     * Method to get the block after a specified block
     *
     * @param owner          the owner of the block before
     * @param sequenceNumber the sequencenumber of the block before
     * @return the block after the specified one
     */

    public final Block getBlockAfter(String owner, int sequenceNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                _columns,
                KEY_OWNER + " = ? AND " + KEY_SEQ_NO + " > ?",
                new String[]{
                        owner, String.valueOf(sequenceNumber)
                }, null, null, null, null);

        if (cursor.getCount() < 1) throw new NotFoundException();

        cursor.moveToFirst();

        // extract block from cursor
        Block block = extractBlock(cursor);

        // Close database connection
        db.close();

        // Close cursor
        cursor.close();

        // return block
        return block;
    }

    /**
     * Check if a block already exists in the database.
     * It is not possible to add a revoked key again.
     * @param owner owner of the block
     * @param key public key in the block
     * @param revoked whether the block is revoked
     * @return if the block already exists
     */
    public final boolean blockExists(String owner, String key, boolean revoked) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                _columns,
                KEY_OWNER + " = ? AND " + KEY_PUBLIC_KEY + " = ? AND " + KEY_REVOKE + " = ?",
                new String[]{
                        owner,
                        key,
                        String.valueOf(revoked ? 1 : 0)
                }, null, null, null, null);

        boolean exists = cursor.getCount() > 0;

        // Close database connection
        db.close();

        // Close cursor
        cursor.close();

        return exists;
    }

    /**
     * Method to get the block before a specified block
     *
     * @param owner          the owner of the block after
     * @param sequenceNumber the sequencenumber of the block after
     * @return the block before the specified one
     */
    public final Block getBlockBefore(String owner, int sequenceNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                _columns,
                KEY_OWNER + " = ? AND " + KEY_SEQ_NO + " < ?",
                new String[]{
                        owner, String.valueOf(sequenceNumber)
                }, null, null, null, null);

        if (cursor.getCount() < 1) throw new NotFoundException();

        cursor.moveToFirst();

        // Extract block from database
        Block block = extractBlock(cursor);

        // Close database connection
        db.close();

        // Close cursor
        cursor.close();

        // return block
        return block;
    }

    /**
     * Method which puts all the blocks currently in the
     * blockchain into a list
     *
     * @param owner the owner of the blocks that are going to be fetched
     * @return List of all the blocks
     */
    public final List<Block> getAllBlocks(String owner) {
        List<Block> blocks = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                _columns,
                KEY_OWNER + " = ?",
                new String[]{
                        owner
                }, null, null, KEY_SEQ_NO, null);


        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                // Extract block from database
                Block block = extractBlock(cursor);
                blocks.add(block);
            } while (cursor.moveToNext());
        }

        // Close database connection
        db.close();

        // Close cursor
        cursor.close();

        return blocks;
    }

    /**
     * Check if the database is empty.
     * @return if the database is empty
     */
    public final boolean isDatabaseEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,
                _columns,
                KEY_PREV_HASH_SENDER + " = ? ",
                new String[]{
                        "N/A"
                }, null, null, null, null);

        boolean empty = c.getCount() < 1;

        c.close();
        return empty;
    }
}
