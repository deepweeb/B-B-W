package nl.tudelft.b_b_w.database;

import android.database.Cursor;

import nl.tudelft.b_b_w.model.User;

import static nl.tudelft.b_b_w.database.Database.KEY_OWNER;
import static nl.tudelft.b_b_w.database.Database.KEY_SEQ_NO;

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
     * @param cursor the cursor resulting from the query
     */
    @Override
    public void parse(Cursor cursor) {
        cursor.moveToFirst();
        size = cursor.getInt(0);
    }

    @Override
    public String[] getSelectedColumns() {
        return new String[] {"MAX(" + KEY_SEQ_NO + ")"};
    }

    @Override
    public String getWhere() {
        return KEY_OWNER + " = ?";
    }

    @Override
    public String[] getWhereVariables() {
        return new String[] { owner.getName() };
    }
}
