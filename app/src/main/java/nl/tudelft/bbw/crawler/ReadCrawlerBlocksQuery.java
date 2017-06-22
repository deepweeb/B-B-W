package nl.tudelft.bbw.crawler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.BlockData;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.blockchain.Hash;
import nl.tudelft.bbw.blockchain.TrustValues;
import nl.tudelft.bbw.blockchain.User;

/**
 * Class which constructs a query to read the crawled database.
 */
public class ReadCrawlerBlocksQuery {

    private static final String NA = "N/A";

    /**
     * The blocks retrieved from the database
     */
    private Map<String, List<Block>> chain;

    /**
     * Column indices for blocks
     */
    private static final int INDEX_ID = 0;
    private static final int INDEX_PUB_KEY = 1;
    private static final int INDEX_SEQ_NO = 5;
    private static final int INDEX_PREV_PUB = 3;
    private static final int INDEX_PREV_HASH_CHAIN = 4;


    /**
     * Constructs a query for the blocks
     */
    public ReadCrawlerBlocksQuery() {
    }

    public Map<String, List<Block>> getChain() {
        return chain;
    }

    /**
     * Method to go through all the rows of the database using the cursor,
     * and make a block with the values, and eventually write it into a JSON file.
     */
    private void parse(Cursor cursor) {
        if (cursor.getCount() == 0) {
            chain = null;
        } else {
            chain = new HashMap<>((int) (cursor.getCount()*1.33));
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                makeBlock(cursor);
            }
        }
        try {
            BlockWriter.writeToJson(chain);
        } catch (IOException e) {
            System.out.println("IOException when writing blocks: " + e);
        }
    }

    /**
     * The query to make the table we want.
     */
    public void execute(SQLiteDatabase database) {
        final String query = "SELECT member.identity, member.public_key, multi_chain.block_hash, multi_chain.link_public_key, multi_chain.previous_hash, multi_chain.sequence_number FROM member JOIN multi_chain ON member.public_key = multi_chain.public_key;";
        Cursor cursor = database.rawQuery(query, new String[]{});
        parse(cursor);
        cursor.close();
    }

    /**
     * Helper to construct a block from the database cursor.
     *
     * @param cursor the cursor to extract from
     */
    private void makeBlock(Cursor cursor) {
        // determine block type
        BlockType type = BlockType.ADD_KEY;

        Hash previousHashChain = new Hash(getStringOfCursor(cursor, INDEX_PREV_HASH_CHAIN));
        Hash previousHashSender = new Hash(getStringOfCursor(cursor, INDEX_PREV_PUB));
        String contactKey = getStringOfCursor(cursor, INDEX_PUB_KEY);

        User user = new User(getStringOfCursor(cursor, INDEX_ID), contactKey);
        User contact = new User(NA, getStringOfCursor(cursor, INDEX_PUB_KEY));
        BlockData blockData = new BlockData(type, cursor.getInt(INDEX_SEQ_NO), previousHashChain, previousHashSender,
                TrustValues.INITIALIZED.getValue());
        Block block = new Block(user, contact, blockData);
        List<Block> blocks = chain.get(contactKey);
        if (blocks == null) {
            blocks = new ArrayList<>();
        }
        blocks.add(block);
        chain.put(contactKey, blocks);
    }

    private String getStringOfCursor(Cursor cursor, int index) {
        if (cursor.getType(index) == Cursor.FIELD_TYPE_BLOB) {
            return new String(cursor.getBlob(index));
        } else {
            return cursor.getString(index);
        }
    }

}
