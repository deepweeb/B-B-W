package nl.tudelft.bbw.crawler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Database handler for the block database implementation of the crawler
 * https://github.com/YourDaddyIsHere/even-cleaner-neighbor-discovery/blob/master
 * /HalfBlockDatabase.py
 */
public class BlockDatabase extends SQLiteAssetHelper {

    /**
     * Attributes for the member table
     */
    static final String USER_TABLE_NAME = "member";
    static final String MEMBER_ID = "identity";
    static final String MEMBER_PUBLIC_KEY = "public_key";
    /**
     * Attributes for the multi_chain table
     */
    static final String BLOCKS_TABLE_NAME = "multi_chain";
    static final String CHAIN_SEQ_NO = "sequence_number";
    static final String CHAIN_LINK_PUBLIC_KEY = "link_public_key";
    static final String CHAIN_PREV_HASH = "previous_hash";
    static final String CHAIN_OWN_HASH = "block_hash";
    static final String CHAIN_PUBLIC_KEY = "public_key";
    /**
     * Attributes which combine the columns of a table
     */
    static final String COLUMNS_MEMBER =
            USER_TABLE_NAME + "." + MEMBER_ID + ", " + USER_TABLE_NAME + "." + MEMBER_PUBLIC_KEY;
    static final String COLUMNS_CHAIN =
            BLOCKS_TABLE_NAME + "."
                    + CHAIN_OWN_HASH + ", " + BLOCKS_TABLE_NAME + "." + CHAIN_LINK_PUBLIC_KEY + ", "
                    + BLOCKS_TABLE_NAME + "." + CHAIN_PREV_HASH + ", " + BLOCKS_TABLE_NAME + "."
                    + CHAIN_SEQ_NO;
    /**
     * Attribute which describe the join of the two tables
     */
    static final String JOIN_TABLES = USER_TABLE_NAME + "."
            + MEMBER_PUBLIC_KEY + " = "
            + BLOCKS_TABLE_NAME + "." + CHAIN_PUBLIC_KEY;
    /**
     * Database attributes
     */
    private static final String PATH = "app/src/main/assets/databases/";
    private static final String DATABASE_NAME = "blockdatabase.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor
     *
     * @param context given context
     */
    public BlockDatabase(Context context) {
        super(context, DATABASE_NAME, PATH, null, DATABASE_VERSION);
    }

    /**
     * Read a predefined query and execute it
     *
     * @param query The query which you want to execute
     */
    public void read(ReadCrawlerBlocksQuery query) {
        SQLiteDatabase database = getWritableDatabase();
        query.execute(database);
        database.close();
    }

}
