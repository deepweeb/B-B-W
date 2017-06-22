package nl.tudelft.bbw.database.read;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import nl.tudelft.bbw.database.Query;

/**
 * A query to perform a read operation on the database
 */
public abstract class ReadQuery extends Query {
    /**
     * Parse the result of the query to retrieve it later from other methods.
     * The database will call this upon query completion to let the queries fill up
     * their fields.
     *
     * @param cursor the cursor resulting from the query
     */
    public abstract void parse(Cursor cursor);

    /**
     * The default behavour of read queries is to query the database and then let
     * subclasses parse the results.
     *
     * @param database the database to perform the query on
     */
    @Override
    public void execute(SQLiteDatabase database) {
        Cursor cursor = database.query(
                getTableName(),
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
     *
     * @return null to indicate no order
     */
    protected String getOrderBy() {
        return null;
    }

    /**
     * The table name on which this query operates
     *
     * @return the table name
     */
    protected abstract String getTableName();

    /**
     * The columns to be returned by this query
     *
     * @return an array of column names
     */
    protected abstract String[] getSelectedColumns();

    /**
     * The condition on which entries are filtered
     *
     * @return the condition (WHERE-clause in SQL)
     */
    protected abstract String getWhere();

    /**
     * Variables that should be escaped inside the WHERE-clause
     *
     * @return a list of variables used by the WHERE-clause
     */
    protected abstract String[] getWhereVariables();
}
