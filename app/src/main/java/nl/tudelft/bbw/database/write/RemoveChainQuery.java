package nl.tudelft.bbw.database.write;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.KeyWriter;
import nl.tudelft.bbw.database.DatabaseException;

import static nl.tudelft.bbw.database.Database.BLOCK_TABLE_NAME;
import static nl.tudelft.bbw.database.Database.KEY_OWNER;

/**
 * Query to remove a complete chain from the database. Useful for when you revoke someone and want
 * to remove its contacts from your database.
 * You have to pass an chain owner as argument to determine which chain to remove.
 */
public class RemoveChainQuery extends WriteQuery {
    /**
     * The string to fill in which users to remove
     */
    private final String whereString = KEY_OWNER + " = ?";

    /**
     * The user of which to remove the chain from the database
     */
    private User owner;

    /**
     * Construct a new remove chain query with a certain owner
     * @param owner the owner of the chain to remove from the database
     */
    public RemoveChainQuery(User owner) {
        this.owner = owner;
    }

    /**
     * We do not want to add content from the chain
     * @return null
     */
    @Override
    protected ContentValues getContentValues() {
        return null;
    }

    /**
     * The chain is removed from the block table
     * @return
     */
    @Override
    protected String getTableName() {
        return BLOCK_TABLE_NAME;
    }

    /**
     * We perform the query be executing a delete command on the database with the given owner
     * @param database the database to perform the query on
     */
    public void execute(SQLiteDatabase database) throws DatabaseException {
        database.delete(getTableName(), whereString,
                new String[]{KeyWriter.publicKeyToString(owner.getPublicKey())});
    }
}
