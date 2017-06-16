package nl.tudelft.bbw.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.BlockData;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.blockchain.Hash;
import nl.tudelft.bbw.controller.KeyReader;
import nl.tudelft.bbw.database.read.GetUserQuery;

import static nl.tudelft.bbw.database.Database.INDEX_CONTACT;
import static nl.tudelft.bbw.database.Database.INDEX_HASH;
import static nl.tudelft.bbw.database.Database.INDEX_OWNER;
import static nl.tudelft.bbw.database.Database.INDEX_PREV_HASH_CHAIN;
import static nl.tudelft.bbw.database.Database.INDEX_PREV_HASH_SENDER;
import static nl.tudelft.bbw.database.Database.INDEX_REVOKE;
import static nl.tudelft.bbw.database.Database.INDEX_SEQ_NO;
import static nl.tudelft.bbw.database.Database.INDEX_TRUST_VALUE;

/**
 * A query you can apply on the database.
 * Both for reading and writing.
 */
public abstract class Query {
    /**
     * Execute this query on a database
     * @param database the database to perform the query on
     */
    public abstract void execute(SQLiteDatabase database);

    /**
     * Helper method to construct a block from the database cursor.
     * TODO: apply luat's refactor
     *
     * @param cursor The cursor to extract from
     * @return A freshly constructed block
     */
    //public BlockData(BlockType blockType, int sequenceNumber, Hash previousHashChain,
    //                 Hash previousHashSender, double trustValue) {
    // public Block(User blockOwner, User contact, BlockData blockData) throws Exception {
    protected static Block toBlock(Database database, Cursor cursor) {
        // determine block type
        BlockType type;
        if (cursor.getInt(INDEX_SEQ_NO) == 1) {
            type = BlockType.GENESIS;
        } else if (cursor.getInt(INDEX_REVOKE) != 0) {
            type = BlockType.REVOKE_KEY;
        } else {
            type = BlockType.ADD_KEY;
        }

        final Hash previousHashChain = new Hash(cursor.getString(INDEX_PREV_HASH_CHAIN));
        final Hash previousHashSender = new Hash(cursor.getString(INDEX_PREV_HASH_SENDER));
        final String ownerKey = cursor.getString(INDEX_OWNER);
        final String contactKey = cursor.getString(INDEX_CONTACT);

        try {
            // get the users
            GetUserQuery ownerQuery = new GetUserQuery(KeyReader.readPublicKey(ownerKey));
            GetUserQuery contactQuery = new GetUserQuery(KeyReader.readPublicKey(contactKey));
            database.read(ownerQuery);
            database.read(contactQuery);

            BlockData blockData = new BlockData(type, cursor.getInt(INDEX_SEQ_NO), previousHashChain, previousHashSender, cursor.getInt(INDEX_TRUST_VALUE));
            Block block = new Block(ownerQuery.getUser(), contactQuery.getUser(), blockData);

            // verify
            final Hash expectedHash = new Hash(cursor.getString(INDEX_HASH));
            final Hash calculatedHash = block.getOwnHash();
            if (!expectedHash.equals(calculatedHash)) {
                throw new Error("key mismatch: expected " + expectedHash + ", actual "
                        + calculatedHash);
            }

            return block;
        } catch (Exception e) {
            throw new RuntimeException("block creation failed: " + e.getMessage());
        }
    }
}
