package nl.tudelft.b_b_w.database.write;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import nl.tudelft.b_b_w.database.Query;

/**
 * A query to write to the database.
 */
public abstract class WriteQuery extends Query {
    /**
     * Each writing query has some associated data to write to the database.
     * This method retrieves it.
     *
     * @return a ContentValues filled with data waiting to be written to the database
     */
    protected abstract ContentValues getContentValues();

    /**
     * Table name to store the new value in. Implementable by subclasses.
     * @return the table name
     */
    protected abstract String getTableName();

    /**
     * The default execution of a write query adds some content values to a table
     * @param database the database to perform the query on
     */
    public void execute(SQLiteDatabase database) {
        ContentValues values = getContentValues();
        database.insertOrThrow(getTableName(), null, values);
    }
}
