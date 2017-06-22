package nl.tudelft.bbw.crawler;

import static nl.tudelft.bbw.database.Database.BLOCK_TABLE_NAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.IOException;

import nl.tudelft.bbw.database.Database;

/**
 * Database handler for the block database implementation of the crawler
 * https://github.com/YourDaddyIsHere/even-cleaner-neighbor-discovery/blob/master
 * /HalfBlockDatabase.py
 */
public class BlockDatabase extends SQLiteAssetHelper {

    /**
     * Class attributes
     */
    static final String PATH = "app/src/main/assets/databases/";
    static final String DATABASE_NAME = "blockdatabase.db";
    static final int DATABASE_VERSION = 1;
    static final String BLOCKS_TABLE_NAME = "multi_chain";
    static final String USER_TABLE_NAME = "member";

    static final String MEMBER_ID = "identity";
    static final String MEMBER_PUBLIC_KEY = "public_key";

    static final String columnsMember =
            USER_TABLE_NAME + "." + MEMBER_ID + ", " + USER_TABLE_NAME + "." + MEMBER_PUBLIC_KEY;

    static final String CHAIN_SEQ_NO = "sequence_number";
    static final String CHAIN_LINK_PUBLIC_KEY = "link_public_key";
    static final String CHAIN_PREV_HASH = "previous_hash";
    static final String CHAIN_OWN_HASH = "block_hash";

    static final String columnsChain =
            BLOCKS_TABLE_NAME + "."
                    + CHAIN_OWN_HASH + ", " + BLOCKS_TABLE_NAME + "." + CHAIN_LINK_PUBLIC_KEY + ", "
                    + BLOCKS_TABLE_NAME + "." + CHAIN_PREV_HASH + ", " + BLOCKS_TABLE_NAME + "."
                    + CHAIN_SEQ_NO;

    public Context context;

    /**
     * Constructor
     *
     * @param context given context
     */
    public BlockDatabase(Context context) {
        super(context, DATABASE_NAME, PATH, null, DATABASE_VERSION);
    }

    public void read(ReadCrawlerBlocksQuery query) {
        SQLiteDatabase database = getWritableDatabase();
        
        query.execute(database);
        database.close();
    }

}
