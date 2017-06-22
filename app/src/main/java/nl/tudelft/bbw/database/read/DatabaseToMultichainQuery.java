package nl.tudelft.bbw.database.read;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.database.Database;

import static nl.tudelft.bbw.database.Database.BLOCK_TABLE_NAME;
import static nl.tudelft.bbw.database.Database.KEY_OWNER;
import static nl.tudelft.bbw.database.Database.KEY_SEQ_NO;
import static nl.tudelft.bbw.database.Database.getBlockColumns;

/**
 * Query to convert all blocks in the database to a multichain
 */
public class DatabaseToMultichainQuery extends ReadQuery {
    /**
     * The multichain retrieved from the database
     */
    private List<List<Block>> multichain;

    /**
     * Database to convert the blocks to multichain later
     */
    private Database database;

    /**
     * Blocks of the user we are currently reading
     */
    private List<Block> currentUserBlocks;

    /**
     * The user which blocks we read the previous block of
     */
    private User currentUser;

    /**
     * Construct a new DatabaseToMultichainQuery
     *
     * @param database the database to construct the blocks from
     */
    public DatabaseToMultichainQuery(Database database) {
        this.database = database;
    }

    /**
     * Convert all blocks to a list of list of blocks.
     * This works by looping all blocks, adding them to the current block list, and when
     * encountering a new user add the block list to the multichain and then reset it.
     *
     * @param cursor the cursor resulting from the query
     */
    @Override
    public void parse(Cursor cursor) {
        multichain = new ArrayList<List<Block>>();
        currentUserBlocks = new ArrayList<Block>();
        currentUser = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            handleBlock(toBlock(database, cursor));
        }

        // add the final block list since no new user is to be found in the end while the blocks
        // still must be added
        if (currentUser != null) {
            multichain.add(currentUserBlocks);
        }
    }

    /**
     * Handle a block retrieved from the database and add it to this query's multichain
     * @param block the block to add
     */
    private void handleBlock(Block block) {
        // initialize previous block owner
        if (currentUser == null) {
            currentUser = block.getBlockOwner();
        }

        // if encountering a new user, add current blocks to multichain and initialize
        // a new block array. Also set the previous user.
        else if (!block.getBlockOwner().equals(currentUser)) {
            multichain.add(currentUserBlocks);
            currentUserBlocks = new ArrayList<Block>();
            currentUser = block.getBlockOwner();
        }

        currentUserBlocks.add(block);
    }

    /**
     * We operate on the blocks table when retrieving blocks
     *
     * @return the block table name
     */
    @Override
    protected String getTableName() {
        return BLOCK_TABLE_NAME;
    }

    /**
     * We need all columns to construct the blocks
     *
     * @return all block columns
     */
    @Override
    protected String[] getSelectedColumns() {
        return getBlockColumns();
    }

    /**
     * We need every block, so no filter
     *
     * @return null
     */
    @Override
    protected String getWhere() {
        return null;
    }

    /**
     * No arguments either
     *
     * @return an empty array
     */
    @Override
    protected String[] getWhereVariables() {
        return new String[0];
    }

    /**
     * Sort by owner and sequence number to add them easily to a multichain later
     *
     * @return the sequence number column
     */
    @Override
    protected String getOrderBy() {
        return KEY_OWNER + ", " + KEY_SEQ_NO;
    }

    /**
     * Retrieve the queried multichain
     *
     * @return the multichain resulting from the query
     */
    public List<List<Block>> getMultichain() {
        return multichain;
    }
}
