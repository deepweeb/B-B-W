package nl.tudelft.bbw.database.read;

import android.database.Cursor;

import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.KeyWriter;

import static nl.tudelft.bbw.database.Database.BLOCK_TABLE_NAME;
import static nl.tudelft.bbw.database.Database.KEY_OWNER;
import static nl.tudelft.bbw.database.Database.KEY_SEQ_NO;

/**
 * Query to retrieve the chain size of a user
 */
public class ChainSizeQuery extends ReadQuery {
    /**
     * Owner of the chain to query
     */
    private User owner;

    /**
     * The resulting chain size
     */
    private int size;

    /**
     * Construct a chain size query
     * @param owner the user that owns the chain
     */
    public ChainSizeQuery(User owner) {
        this.owner = owner;
    }

    /**
     * Getter for the chain size
     * @return the chain size resulting from the query
     */
    public int getSize() {
        return size;
    }

    /**
     * This query returns only a single int, so
     * @param cursor the query result containing the maximum sequence number
     */
    @Override
    public void parse(Cursor cursor) {
        cursor.moveToFirst();
        size = cursor.getInt(0);
    }

    /**
     * Chain size always operates on the block table
     * @return the block table name
     */
    @Override
    protected String getTableName() {
        return BLOCK_TABLE_NAME;
    }

    /**
     * Select the max of the sequence number to get the chain size
     * @return array containing one SQL max command
     */
    @Override
    public String[] getSelectedColumns() {
        return new String[] {"MAX(" + KEY_SEQ_NO + ")"};
    }

    /**
     * We only consider blocks of one owner
     * @return query for getting entries of one owner
     */
    @Override
    public String getWhere() {
        return KEY_OWNER + " = ?";
    }

    /**
     * The only variable is the owner name
     * @return the owner name
     */
    @Override
    public String[] getWhereVariables() {
        return new String[] {KeyWriter.publicKeyToString(owner.getPublicKey()) };
    }
}
