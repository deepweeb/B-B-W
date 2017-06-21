package nl.tudelft.bbw.database.read;

import android.database.Cursor;

import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.KeyWriter;

import static nl.tudelft.bbw.database.Database.KEY_PUBLICKEY;
import static nl.tudelft.bbw.database.Database.USER_TABLE_NAME;

/**
 * Query to check if a user exists
 */
public class UserExistQuery extends ReadQuery {
    /**
     * The user to check
     */
    private User user;

    /**
     * Whether this user exists
     */
    private boolean exists;

    /**
     * Construct query to check if a user exists
     *
     * @param user the user to check
     */
    public UserExistQuery(User user) {
        this.user = user;
    }

    /**
     * The user exists if the query result has content
     *
     * @param cursor the cursor resulting from the query
     */
    @Override
    public void parse(Cursor cursor) {
        exists = cursor.getCount() > 0;
    }

    /**
     * The user exist query operates on the user table
     *
     * @return the user table name
     */
    @Override
    protected String getTableName() {
        return USER_TABLE_NAME;
    }

    /**
     * To determine if a user exists, we only need one column, the public key
     *
     * @return the public key column name
     */
    @Override
    protected String[] getSelectedColumns() {
        return new String[]{KEY_PUBLICKEY};
    }

    /**
     * Since each user can be identified by its public key we only need to filter on that
     *
     * @return
     */
    @Override
    protected String getWhere() {
        return KEY_PUBLICKEY + " = ? ";
    }

    /**
     * The where argument is the user public key
     *
     * @return user key
     */
    @Override
    protected String[] getWhereVariables() {
        return new String[]{
                KeyWriter.publicKeyToString(user.getPublicKey())
        };
    }

    /**
     * Retrieve our result
     *
     * @return whether the user existed
     */
    public boolean doesExist() {
        return exists;
    }
}
