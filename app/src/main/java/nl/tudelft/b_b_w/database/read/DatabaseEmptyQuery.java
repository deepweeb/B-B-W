package nl.tudelft.b_b_w.database.read;

import android.database.Cursor;

import static nl.tudelft.b_b_w.database.Database.USER_TABLE_NAME;
import static nl.tudelft.b_b_w.database.Database.getUserColumns;

/**
 * Query to check if the database is empty. Does this by checking if there are any entries
 * inside the user table.
 */
public class DatabaseEmptyQuery extends ReadQuery {
    /**
     * If the database is empty
     */
    private boolean databaseEmpty;

    /**
     * {@inheritDoc}
     */
    public DatabaseEmptyQuery() {
    }

    /**
     * Retrieve whether the database is empty
     * @return whether the database is empty
     */
    public boolean isDatabaseEmpty() {
        return databaseEmpty;
    }

    /**
     * If the query returned any result, the database is not empty
     * @param cursor the cursor resulting from the query
     */
    @Override
    public void parse(Cursor cursor) {
        databaseEmpty = cursor.getCount() <= 0;
    }

    /**
     * We operate on the user table
     * @return the user table name
     */
    @Override
    protected String getTableName() {
        return USER_TABLE_NAME;
    }

    /**
     * Simply select all columns
     * @return all user columns
     */
    @Override
    protected String[] getSelectedColumns() {
        return getUserColumns();
    }

    /**
     * There is no condition
     * @return empty string
     */
    @Override
    protected String getWhere() {
        return "";
    }

    /**
     * No arguments either
     * @return empty array
     */
    @Override
    protected String[] getWhereVariables() {
        return new String[0];
    }
}
