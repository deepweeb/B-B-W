package nl.tudelft.bbw.database.read;

import android.database.Cursor;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.util.List;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.KeyWriter;

import static nl.tudelft.bbw.database.Database.INDEX_IBAN;
import static nl.tudelft.bbw.database.Database.INDEX_NAME;
import static nl.tudelft.bbw.database.Database.KEY_PUBLICKEY;
import static nl.tudelft.bbw.database.Database.USER_TABLE_NAME;
import static nl.tudelft.bbw.database.Database.getUserColumns;

/**
 * Query to get a user object (name, iban) given its public key
 */
public class GetUserQuery extends ReadQuery {
    /**
     * The public key to search for users
     */
    private EdDSAPublicKey publicKey;

    /**
     * The owner of the chain to retrieve
     */
    private User user;

    /**
     * The chain retrieved from the database
     */
    private List<Block> chain;

    /**
     * Construct the get user query
     * @param publicKey the public key of the user
     */
    public GetUserQuery(EdDSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Convert the item resulting from this query to a user, or null if there is no result
     * @param cursor the cursor resulting from the query
     */
    @Override
    public void parse(Cursor cursor) {
        if (cursor.getCount() == 0) {
            user = null;
        } else {
            cursor.moveToFirst();
            String name = cursor.getString(INDEX_NAME);
            String iban = cursor.getString(INDEX_IBAN);
            user = new User(name, iban, publicKey);
        }
    }

    /**
     * We want all information to reconstruct the blocks accurately
     * @return
     */
    @Override
    protected String[] getSelectedColumns() {
        return getUserColumns();
    }

    /**
     * We want to filter on owners
     * @return
     */
    @Override
    protected String getWhere() {
        return KEY_PUBLICKEY + " = ?";
    }

    /**
     * The only variable for this query is the owner name of the chain
     * @return
     */
    @Override
    protected String[] getWhereVariables() {
        return new String[] {KeyWriter.publicKeyToString(publicKey)};
    }

    /**
     * Retrieves the output of this query: the user
     * @return a list of blocks
     */
    public User getUser() {
        return user;
    }

    /**
     * Get Chain Query always operates on the block table
     * @return the block table name
     */
    @Override
    public String getTableName() {
        return USER_TABLE_NAME;
    }
}
