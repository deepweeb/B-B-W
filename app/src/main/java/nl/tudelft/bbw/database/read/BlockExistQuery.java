package nl.tudelft.bbw.database.read;

import android.database.Cursor;

import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.controller.KeyWriter;

import static nl.tudelft.bbw.database.Database.BLOCK_TABLE_NAME;
import static nl.tudelft.bbw.database.Database.KEY_CONTACT;
import static nl.tudelft.bbw.database.Database.KEY_OWNER;
import static nl.tudelft.bbw.database.Database.KEY_REVOKE;
import static nl.tudelft.bbw.database.Database.getBlockColumns;

/**
 * This query checks if there exist a block in the multichain for a (user, publickey) pair.
 * This block could be an add block or a revoke block; in most cases this does not matter since
 * if either exists we can abort the block addition.
 */
public class BlockExistQuery extends ReadQuery {
    /**
     * The block to look for
     */
    private Block block;

    /**
     * Whether the block exists in the database
     */
    private boolean exists;

    /**
     * Construct a new BlockExistsQuery with a given block to look for
     *
     * @param block the block to look for in the database
     */
    public BlockExistQuery(Block block) {
        this.block = block;
    }

    /**
     * Get if the block exists
     *
     * @return whether the block exists
     */
    public boolean blockExists() {
        return exists;
    }

    /**
     * @param cursor the cursor resulting from the query
     */
    @Override
    public void parse(Cursor cursor) {
        exists = cursor.getCount() > 0;
    }

    /**
     * We obviously operate on the block table
     *
     * @return the block table name
     */
    @Override
    protected String getTableName() {
        return BLOCK_TABLE_NAME;
    }

    /**
     * Select all columns
     *
     * @return all block columns
     */
    @Override
    protected String[] getSelectedColumns() {
        return getBlockColumns();
    }

    /**
     * Filter on the owner and contact fields
     *
     * @return
     */
    @Override
    protected String getWhere() {
        return KEY_OWNER + " = ? AND " + KEY_CONTACT + " = ? AND " + KEY_REVOKE + " = ?";
    }

    /**
     * Our arguments are the public key of the owner and of the contact
     *
     * @return the public key of the owner and of the contact
     */
    @Override
    protected String[] getWhereVariables() {
        return new String[]{
                KeyWriter.publicKeyToString(block.getOwnerPublicKey()),
                KeyWriter.publicKeyToString(block.getContactPublicKey()),
                String.valueOf(block.getBlockType() == BlockType.REVOKE_KEY ? 1 : 0)
        };
    }
}
