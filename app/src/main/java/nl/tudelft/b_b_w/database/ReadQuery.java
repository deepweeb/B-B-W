package nl.tudelft.b_b_w.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static nl.tudelft.b_b_w.database.Database.TABLE_NAME;

/**
 * A query to perform a read operation on the database
 */
public abstract class ReadQuery extends Query {
    /**
     * Parse the result of the query to retrieve it later from other methods.
     * The database will call this upon query completion to let the queries fill up
     * their fields.
     * @param cursor the cursor resulting from the query
     */
    public abstract void parse(Cursor cursor);

    /**
     * The default behavour of read queries is to query the database and then let
     * subclasses parse the results.
     * @param database the database to perform the query on
     */
    @Override
    public void execute(SQLiteDatabase database) {
        Cursor cursor = database.query(
                TABLE_NAME,
                getSelectedColumns(),
                getWhere(),
                getWhereVariables(),
                null,
                null,
                getOrderBy(),
                null
        );

        // let our subclasses handle the result
        parse(cursor);

        // close cursor when done
        cursor.close();
    }

    /**
     * By default the results are not ordered
     * @return null to indicate no order
     */
    protected String getOrderBy() {
        return null;
    }

    protected abstract String[] getSelectedColumns();
    protected abstract String getWhere();
    protected abstract String[] getWhereVariables();
}
