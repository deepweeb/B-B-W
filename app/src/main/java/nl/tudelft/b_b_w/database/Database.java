package nl.tudelft.b_b_w.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Connection to the blocks database
 */
public class Database extends SQLiteOpenHelper {
    /**
     * Main table name
     */
    static final String TABLE_NAME = "blocks";

    /**
     * Column names
     */
    static final String KEY_OWNER = "owner";
    static final String KEY_SEQ_NO = "sequenceNumber";
    static final String KEY_PREV_HASH_SENDER = "previousHashSender";
    static final String KEY_OWN_HASH = "ownHash";
    static final String KEY_PREV_HASH_CHAIN = "previousHashChain";
    static final String KEY_PUBLIC_KEY = "publicKey";
    static final String KEY_IBAN_KEY = "iban";
    static final String KEY_TRUST_VALUE = "trustValue";
    static final String KEY_REVOKE = "revoke";
    static final String KEY_CREATED_AT = "created_at";

    /**
     * All columns
     */
    static final String[] COLUMNS = new String[] {
            KEY_OWNER, KEY_SEQ_NO, KEY_OWN_HASH, KEY_PREV_HASH_CHAIN, KEY_PREV_HASH_SENDER,
            KEY_PUBLIC_KEY, KEY_IBAN_KEY, KEY_TRUST_VALUE, KEY_REVOKE
    };

    /**
     * Column indices
     */
    static final int INDEX_OWNER = 0;
    static final int INDEX_SEQ_NO = 1;
    static final int INDEX_OWN_HASH = 2;
    static final int INDEX_PREV_HASH_CHAIN = 3;
    static final int INDEX_PREV_HASH_SENDER = 4;
    static final int INDEX_PUBLIC_KEY = 5;
    static final int INDEX_IBAN_KEY = 6;
    static final int INDEX_TRUST_VALUE = 7;
    static final int INDEX_REVOKE = 8;

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "blockChain";

    /**
     * Context to retrieve the database
     */
    private Context context;

    /**
     * Create a new connection to the database
     * @param context the specific context containing our database
     */
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createBlocksTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_OWNER + " TEXT NOT NULL,"
                + KEY_SEQ_NO + " INTEGER NOT NULL,"
                + KEY_OWN_HASH + " TEXT NOT NULL,"
                + KEY_PREV_HASH_CHAIN + " TEXT NOT NULL,"
                + KEY_PREV_HASH_SENDER + " TEXT NOT NULL,"
                + KEY_PUBLIC_KEY + " TEXT NOT NULL,"
                + KEY_IBAN_KEY + " TEXT NOT NULL,"
                + KEY_TRUST_VALUE + " INTEGER NOT NULL,"
                + KEY_REVOKE + " BOOLEAN DEFAULT FALSE NOT NULL,"
                + KEY_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                + " PRIMARY KEY (owner, publicKey, sequenceNumber)"
                + ")";
        db.execSQL(createBlocksTable);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String upgradeScript = "DROP TABLE IF EXISTS " + TABLE_NAME + ";"
                + "DROP TABLE IF EXISTS option;";

        // TODO: check if the db version is lower than the latest

        db.execSQL(upgradeScript);
    }

    /**
     * Perform a read query
     * @param query the query to execute
     */
    public void read(Query query) {
        query.execute(getReadableDatabase());
    }

    /**
     * Perform an update query
     * @param query the query to execute
     */
    public void write(Query query) {
        query.execute(getWritableDatabase());
    }
}
