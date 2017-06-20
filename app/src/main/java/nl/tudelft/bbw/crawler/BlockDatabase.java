package nl.tudelft.bbw.crawler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import nl.tudelft.bbw.database.Query;

/**
 * Database handler for the block database implementation of the crawler
 * https://github.com/YourDaddyIsHere/even-cleaner-neighbor-discovery/blob/master/HalfBlockDatabase.py
 */
public class BlockDatabase extends SQLiteAssetHelper {

    /**
     * Class attributes
     */
    private static final String DATABASE_NAME = "BlockDataBase.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor
     * @param context given context
     */
    public BlockDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void read(Query query) {
        SQLiteDatabase database = getReadableDatabase();
        query.execute(database);
        database.close();
    }

}
