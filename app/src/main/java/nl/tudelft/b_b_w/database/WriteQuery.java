package nl.tudelft.b_b_w.database;

import android.content.ContentValues;

/**
 * A query to write to the database.
 */
public interface WriteQuery {
    /**
     * Each writing query has some associated data to write to the database.
     * This method retrieves it.
     * @return a ContentValues filled with data waiting to be written to the database
     */
    ContentValues getContentValues();
}
