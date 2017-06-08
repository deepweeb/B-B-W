package nl.tudelft.b_b_w.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * A query you can apply on the database.
 * Both for reading and writing.
 */
public abstract class Query {
    /**
     * Execute this query on a database
     * @param database the database to perform the query on
     */
    public abstract void execute(SQLiteDatabase database);
}
