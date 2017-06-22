package nl.tudelft.bbw.crawler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Map<String, List<BlockData>> chain;

    /**
     * Column indices for blocks
     */
    static final int INDEX_PUB_KEY = 1;
    static final int INDEX_SEQ_NO = 5;
    static final int INDEX_PREV_PUB = 3;
    static final int INDEX_PREV_HASH_CHAIN = 4;

    Hash previousHashChain;
    Hash previousHashSender;
    String contactKey;

    /**
     * Constructs a query for the blocks
     */
    public ReadCrawlerBlocksQuery() {
    }

    public Map<String, List<BlockData>> getChain() {
        return chain;
    }

    /**
     * @inheritDoc
     */
    public void parse(Cursor cursor) {
        if (cursor.getCount() == 0) {
            chain = null;
        } else {
            chain = new HashMap<String, List<BlockData>>();
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

        if (cursor.getType(INDEX_PREV_HASH_CHAIN) == Cursor.FIELD_TYPE_BLOB) {
            previousHashChain = new Hash(new String(cursor.getBlob(INDEX_PREV_HASH_CHAIN)));
        } else {
            previousHashChain = new Hash(cursor.getString(INDEX_PREV_HASH_CHAIN));
        }
        if (cursor.getType(INDEX_PREV_PUB) == Cursor.FIELD_TYPE_BLOB) {
            previousHashSender = new Hash(new String(cursor.getBlob(INDEX_PREV_PUB)));
        } else {
            previousHashSender = new Hash(cursor.getString(INDEX_PREV_PUB));
        }
        if (cursor.getType(INDEX_PUB_KEY) == Cursor.FIELD_TYPE_BLOB) {
            contactKey = new String(cursor.getBlob(INDEX_PUB_KEY));
        } else {
            contactKey = cursor.getString(INDEX_PUB_KEY);
        }

        BlockData blockData = new BlockData(type, cursor.getInt(INDEX_SEQ_NO), previousHashChain, previousHashSender,
                TrustValues.INITIALIZED.getValue());
        List<BlockData> blockDatas = chain.get(contactKey);
        if (blockDatas == null) {
            blockDatas = new ArrayList<>();
            blockDatas.add(blockData);
        } else {
            blockDatas.add(blockData);
        }
        chain.put(contactKey, blockDatas);
    }

}
