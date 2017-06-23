package nl.tudelft.bbw.database.write;

import android.content.ContentValues;

import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.KeyWriter;

import static nl.tudelft.bbw.database.Database.KEY_IBAN;
import static nl.tudelft.bbw.database.Database.KEY_NAME;
import static nl.tudelft.bbw.database.Database.KEY_PUBLICKEY;
import static nl.tudelft.bbw.database.Database.USER_TABLE_NAME;

/**
 * Query to add a block to the database
 */
public class UserAddQuery extends WriteQuery {
    /**
     * The user to add
     */
    private User user;

    /**
     * Create a query to add a user
     *
     * @param user the user to add
     */
    public UserAddQuery(User user) {
        this.user = user;
    }

    /**
     * Our content
     *
     * @return the attributes of the new user
     */
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_IBAN, user.getIban());
        values.put(KEY_PUBLICKEY, KeyWriter.publicKeyToString(user.getPublicKey()));
        return values;
    }

    /**
     * We modify the user table
     *
     * @return the user table name
     */
    @Override
    protected String getTableName() {
        return USER_TABLE_NAME;
    }
}
