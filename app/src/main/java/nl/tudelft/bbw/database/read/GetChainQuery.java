package nl.tudelft.bbw.database.read;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.KeyWriter;
import nl.tudelft.bbw.database.Database;

import static nl.tudelft.bbw.database.Database.BLOCK_TABLE_NAME;
import static nl.tudelft.bbw.database.Database.KEY_OWNER;
import static nl.tudelft.bbw.database.Database.KEY_SEQ_NO;
import static nl.tudelft.bbw.database.Database.getBlockColumns;


/**
 * Query to get a chain of blocks of a user. This includes the genesis block.
 * The resulting list is sorted.
 */
public class GetChainQuery extends ReadQuery {
    /**
     * The owner of the chain to retrieve
     */
    private User owner;

    /**
     * The chain retrieved from the database
     */
    private List<Block> chain;

    /**
     * Store the database so we can perform user queries on it
     */
    private Database database;

    /**
     * Construct a chain query for a user
     *
     * @param database the database to use for user queries
     * @param owner    the owner of the chain
     */
    public GetChainQuery(Database database, User owner) {
        this.database = database;
        this.owner = owner;
    }

    /**
     * Loop through all items under the cursor converting them to blocks
     *
     * @param cursor the cursor resulting from the query
     */
    @Override
    public void parse(Cursor cursor) {
        if (cursor.getCount() == 0) {
            chain = null;
        } else {
            chain = new ArrayList<Block>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Block block = toBlock(database, cursor);
                chain.add(block);
            }
        }
    }

    /**
     * We want all information to reconstruct the blocks accurately
     */
    @Override
    protected String[] getSelectedColumns() {
        return getBlockColumns();
    }

    /**
     * We want to filter on owners
     */
    @Override
    protected String getWhere() {
        return KEY_OWNER + " = ?";
    }

    /**
     * The only variable for this query is the owner name of the chain
     */
    @Override
    protected String[] getWhereVariables() {
        return new String[]{KeyWriter.publicKeyToString(owner.getPublicKey())};
    }

    /**
     * Retrieves the output of this query: a chain of blocks
     *
     * @return a list of blocks
     */
    public List<Block> getChain() {
        return chain;
    }

    /**
     * We want the results sorted on sequence number
     *
     * @return the name of the sequence number column
     */
    @Override
    public String getOrderBy() {
        return KEY_SEQ_NO;
    }

    /**
     * Get Chain Query always operates on the block table
     *
     * @return the block table name
     */
    @Override
    public String getTableName() {
        return BLOCK_TABLE_NAME;
    }
}
