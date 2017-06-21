package nl.tudelft.bbw.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Connection to the blocks database
 */
public class Database extends SQLiteOpenHelper {
    /**
     * Block table name
     */
    public static final String BLOCK_TABLE_NAME = "blocks";

    /**
     * User table name
     */
    public static final String USER_TABLE_NAME = "users";

    /**
     * String for when text is not null in the block table
     */
    private final String textNotNull = " TEXT NOT NULL,";

    /**
     * String for when text is not null in the block table
     */
    private final String integerNotNull = " INTEGER NOT NULL,";

    /**
     * SQL code to create the blocks table
     */
    private final String createBlockTable = "CREATE TABLE IF NOT EXISTS " + BLOCK_TABLE_NAME + "("
            + KEY_SEQ_NO + integerNotNull
            + KEY_OWNER + textNotNull
            + KEY_CONTACT + textNotNull
            + KEY_HASH + textNotNull
            + KEY_PREV_HASH_CHAIN + textNotNull
            + KEY_PREV_HASH_SENDER + textNotNull
            + KEY_REVOKE + " BOOLEAN DEFAULT FALSE NOT NULL,"
            + KEY_TRUST_VALUE + integerNotNull
            + " PRIMARY KEY (" + KEY_HASH + ")"
            + ")";
    /**
     * SQL code to create the user table
     */
    private final String createUserTable = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + "("
            + KEY_NAME + textNotNull
            + KEY_IBAN + textNotNull
            + KEY_PUBLICKEY + textNotNull
            + " PRIMARY KEY (" + KEY_PUBLICKEY + ")"
            + ")";

    /**
     * Column names for blocks
     */
    public static final String KEY_SEQ_NO = "sequenceNumber";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_HASH = "ownHash";
    public static final String KEY_PREV_HASH_CHAIN = "previousHashChain";
    public static final String KEY_PREV_HASH_SENDER = "previousHashSender";
    public static final String KEY_REVOKE = "revoke";
    public static final String KEY_TRUST_VALUE = "trustValue";

    /**
     * All columns
     */
    private static final String[] BLOCK_COLUMNS = new String[] {
            KEY_SEQ_NO, KEY_OWNER, KEY_CONTACT, KEY_HASH, KEY_PREV_HASH_CHAIN, KEY_PREV_HASH_SENDER,
            KEY_REVOKE, KEY_TRUST_VALUE
    };

    /**
     * All columns
     */
    public static final String[] getBlockColumns() {
        return BLOCK_COLUMNS;
    }

    /**
     * Column indices for blocks
     */
    static final int INDEX_SEQ_NO = 0;
    static final int INDEX_OWNER = 1;
    static final int INDEX_CONTACT = 2;
    static final int INDEX_HASH = 3;
    static final int INDEX_PREV_HASH_CHAIN = 4;
    static final int INDEX_PREV_HASH_SENDER = 5;
    static final int INDEX_REVOKE = 6;
    static final int INDEX_TRUST_VALUE = 7;

    /**
     * Column names for users
     */
    public static final String KEY_NAME = "name";
    public static final String KEY_IBAN = "iban";
    public static final String KEY_PUBLICKEY = "publicKey";

    /**
     * Column indices for users
     */
    public static final int INDEX_NAME = 0;
    public static final int INDEX_IBAN = 1;
    public static final int INDEX_PUBLICKEY = 2;

    /**
     * Columns in the user table
     */
    private static final String[] USER_COLUMNS = new String[] {
            KEY_NAME, KEY_IBAN, KEY_PUBLICKEY
    };

    /**
     * Columns in the user table
     */
    public static final String[] getUserColumns() {
        return USER_COLUMNS;
    }

    /**
     * Our version is one
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * How our database is called
     */
    public static final String DATABASE_NAME = "blockChain";

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
        db.execSQL(createBlockTable);
        db.execSQL(createUserTable);
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
        final String upgradeScript = "DROP TABLE IF EXISTS " + BLOCK_TABLE_NAME + ";"
                + "DROP TABLE IF EXISTS option;";

        // TODO: check if the db version is lower than the latest

        db.execSQL(upgradeScript);
    }

    /**
     * Perform a read query
     * @param query the query to execute
     * TODO: think about if we need to open a new connection every time if it is too slow
     */
    public void read(Query query) {
        SQLiteDatabase database = getReadableDatabase();
        query.execute(database);
        database.close();
    }

    /**
     * Perform an update query
     * @param query the query to execute
     */
    public void write(Query query) throws DatabaseException{
        SQLiteDatabase database = getWritableDatabase();
        query.execute(database);

        database.close();
    }

}
