package nl.tudelft.bbw.database.read;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.KeyWriter;
import nl.tudelft.bbw.database.Database;

import static nl.tudelft.bbw.database.Database.BLOCK_TABLE_NAME;
import static nl.tudelft.bbw.database.Database.KEY_OWNER;
import static nl.tudelft.bbw.database.Database.KEY_SEQ_NO;
import static nl.tudelft.bbw.database.Database.getBlockColumns;

/**
 * Query to get the latest block of a chain
 */
public class LatestBlockQuery extends ReadQuery {
    /**
     * Reference to the database for the toBlock function later
     */
    private Database database;

    /**
     * The owner of the chain we want to get the latest block of
     */
    private User owner;

    /**
     * The last sequence number of our chain, also the chain size
     */
    private int lastSeqNo;

    /**
     * The resulting query
     */
    private Block latestBlock;

    /**
     * Construct a query to get the latest block of a chain
     * @param database the database, for the user toBlock function later
     * @param owner the owner of the chain
     */
    public LatestBlockQuery(Database database, User owner) {
        this.database = database;
        this.owner = owner;
    }

    /**
     * Get our query result. Null when the user does not have any blocks.
     * @return the latest block of a chain
     */
    public Block getLatestBlock() {
        return latestBlock;
    }

    /**
     * First execute the ChainSizeQuery so that we can get the right latest block
     * @param database the database to perform the query on
     */
    @Override
    public void execute(SQLiteDatabase database) {
        ChainSizeQuery sizeQuery = new ChainSizeQuery(owner);
        sizeQuery.execute(database);
        lastSeqNo = sizeQuery.getSize();
        super.execute(database);
    }

    /**
     * Parse the query result by converting the result to a block. Null when no result.
     * @param cursor the cursor resulting from the query
     */
    @Override
    public void parse(Cursor cursor) {
        if (cursor.getCount() <= 0) {
            latestBlock = null;
        } else {
            cursor.moveToFirst();
            latestBlock = toBlock(database, cursor);
        }
    }

    /**
     * Query the block table
     * @return the block table name
     */
    @Override
    protected String getTableName() {
        return BLOCK_TABLE_NAME;
    }

    /**
     * We want all block columns to create a block faithful to the original
     * @return all block columns
     */
    @Override
    protected String[] getSelectedColumns() {
        return getBlockColumns();
    }

    /**
     * Filter on chain owner and the latest sequence number
     * @return those two filters
     */
    @Override
    protected String getWhere() {
        return KEY_OWNER + " = ? AND " + KEY_SEQ_NO + " = ?";
    }

    /**
     * The arguments are the owner (identified by public key) and the latest sequence number
     * @return those two arguments
     */
    @Override
    protected String[] getWhereVariables() {
        return new String[] { KeyWriter.publicKeyToString(owner.getPublicKey()),
                Integer.toString(lastSeqNo) };
    }
}
