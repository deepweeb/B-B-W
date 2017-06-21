package nl.tudelft.bbw.crawler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.bbw.blockchain.BlockData;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.blockchain.Hash;
import nl.tudelft.bbw.blockchain.TrustValues;

/**
 * Class which constructs a query to read the crawled database.
 */
public class ReadCrawlerBlocksQuery {

    /**
     * The blocks retrieved from the database
     */
    private Map<String, BlockData> chain;

    /**
     * Column indices for blocks
     */
    static final int INDEX_PUB_KEY = 1;
    static final int INDEX_SEQ_NO = 5;
    static final int INDEX_PREV_PUB = 3;
    static final int INDEX_PREV_HASH_CHAIN = 4;

    /**
     * Constructs a query for the blocks
     */
    public ReadCrawlerBlocksQuery() {
    }

    public Map<String, BlockData> getChain() {
        return chain;
    }

    /**
     * @inheritDoc
     */
    public void parse(Cursor cursor) {
        if (cursor.getCount() == 0) {
            chain = null;
        } else {
            chain = new HashMap<>();
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
     * @inheritDoc
     */
    public void execute(SQLiteDatabase database) {
//        final String QUERY =
//                "SELECT " + columnsMember + ", " + columnsChain + " FROM " + USER_TABLE_NAME
//                        + " JOIN " + BLOCKS_TABLE_NAME + " ON " + USER_TABLE_NAME + "." + MEMBER_PUBLIC_KEY + " = "
//                        + BLOCKS_TABLE_NAME + "." + CHAIN_PUBLIC_KEY + ";";
        final String QUERY = "SELECT member.identity, member.public_key, multi_chain.block_hash, multi_chain.link_public_key, multi_chain.previous_hash, multi_chain.sequence_number FROM member JOIN multi_chain ON member.public_key = multi_chain.public_key;";
        Cursor cursor = database.rawQuery(QUERY, new String[]{});
        parse(cursor);
        cursor.close();
    }

    /**
     * Helper to construct a block from the database cursor.
     * @param cursor the cursor to extract from
     */
    private void makeBlock(Cursor cursor) {
        // determine block type
        BlockType type = BlockType.ADD_KEY;

        final Hash previousHashChain = new Hash(cursor.getString(INDEX_PREV_HASH_CHAIN));
        final Hash previousHashSender = new Hash(cursor.getString(INDEX_PREV_PUB));
        final String contactKey = cursor.getString(INDEX_PUB_KEY);

        BlockData blockData = new BlockData(type, cursor.getInt(INDEX_SEQ_NO), previousHashChain, previousHashSender,
                TrustValues.INITIALIZED.getValue());
        chain.put(contactKey, blockData);
    }

}
