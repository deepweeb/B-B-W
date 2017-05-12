package nl.tudelft.b_b_w.ModelsUnitTest;

import android.database.sqlite.SQLiteDatabase;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;


import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.Models.Block;
import nl.tudelft.b_b_w.Models.DatabaseHandler;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class DatabaseHandlerUnitTest {

    private DatabaseHandler databaseHandler;

    /**
     * setUp method
     * Does this method before every test
     * Initializes the database handler
     */
    @Before
    public void setUp() {
        this.databaseHandler = DatabaseHandler.getInstance(RuntimeEnvironment.application);
    }

    /**
     * singleton test
     * Tests whether the singleton design pattern works
     */
    @Test
    public void singleton() {
        DatabaseHandler databaseHandler1 = databaseHandler.getInstance(RuntimeEnvironment.application);
        assertEquals(databaseHandler, databaseHandler1);
    }

    /**
     * addBlock test
     * Tests adding a block
     */
    @Test
    public void addBlock() {
        String owner = "owner";
        String previous_hash = "prev_hash";
        String public_key = "pub_key";
        boolean isRevoked = false;
        int sequence_number = 0;
        Block block = new Block(owner, sequence_number, previous_hash, public_key, isRevoked);
        databaseHandler.addBlock(block);
        assertEquals(block, databaseHandler.getBlock(owner, public_key, sequence_number));
    }

    /**
     * getNullBlock test
     * Tests getting a non-existing block
     */
    @Test
    public void getNullBlock() {
        assertNull(databaseHandler.getLatestBlock("null", "null"));
    }

    /**
     * containsBlock/2 test
     * Tests whether a block exists
     */
    @Test
    public void containsBlock2() {
        String owner = "owner";
        String previous_hash = "prev_hash";
        String public_key = "pub_key";
        boolean isRevoked = false;
        int sequence_number = 0;
        Block block = new Block(owner, sequence_number, previous_hash, public_key, isRevoked);
        databaseHandler.addBlock(block);
        assertTrue(databaseHandler.containsBlock(owner, public_key));
    }

    /**
     * containsBlock/3 test
     * Tests whether a block exists
     */
    @Test
    public void containsBlock3() {
        String owner = "owner";
        String previous_hash = "prev_hash";
        String public_key = "pub_key";
        boolean isRevoked = false;
        int sequence_number = 0;
        Block block = new Block(owner, sequence_number, previous_hash, public_key, isRevoked);
        databaseHandler.addBlock(block);
        assertTrue(databaseHandler.containsBlock(owner, public_key, sequence_number));
    }

    /**
     * containsBlock/2 test
     * Tests whether a block exists
     * Forces a false
     */
    @Test
    public void containsBlock2_false() {
        String owner = "owner";
        String previous_hash = "prev_hash";
        String public_key = "pub_key";
        boolean isRevoked = false;
        int sequence_number = 0;
        Block block = new Block(owner, sequence_number, previous_hash, public_key, isRevoked);
        databaseHandler.addBlock(block);
        assertFalse(databaseHandler.containsBlock(owner, "pub_key2", sequence_number));
    }

    /**
     * getLatestSeqNum test
     * Tests the latest sequence number of a block
     */
    @Test
    public void getLatestSeqNum() {
        String owner = "owner";
        String previous_hash = "prev_hash";
        String public_key = "pub_key";
        boolean isRevoked = false;
        int sequence_number = 0;
        Block block = new Block(owner, sequence_number, previous_hash, public_key, isRevoked);
        Block block2 = new Block(owner, 1, previous_hash, public_key, isRevoked);
        databaseHandler.addBlock(block);
        databaseHandler.addBlock(block2);
        assertEquals(databaseHandler.getLatestSeqNum(owner, public_key), 1);
    }

    /**
     * getLatestBlock test
     * Tests the latest block
     */
    @Test
    public void getLatestBlock() {
        String owner = "owner";
        String previous_hash = "prev_hash";
        String public_key = "pub_key";
        boolean isRevoked = false;
        int sequence_number = 0;
        Block block = new Block(owner, sequence_number, previous_hash, public_key, isRevoked);
        Block block2 = new Block(owner, 1, previous_hash, public_key, isRevoked);
        databaseHandler.addBlock(block);
        databaseHandler.addBlock(block2);
        assertEquals(databaseHandler.getLatestBlock(owner, public_key), block2);
    }

    /**
     * getBlockAfter test
     * Tests the block after a block
     */
    @Test
    public void getBlockAfter() {
        String owner = "owner";
        String previous_hash = "prev_hash";
        String public_key = "pub_key";
        boolean isRevoked = false;
        int sequence_number = 0;
        Block block = new Block(owner, sequence_number, previous_hash, public_key, isRevoked);
        Block block2 = new Block(owner, 1, previous_hash, public_key, isRevoked);
        databaseHandler.addBlock(block);
        databaseHandler.addBlock(block2);
        assertEquals(databaseHandler.getBlockAfter(owner, public_key, sequence_number), block2);
    }

    /**
     * getBlockBefore test
     * Tests the block before a block
     */
    @Test
    public void getBlockBefore() {
        String owner = "owner";
        String previous_hash = "prev_hash";
        String public_key = "pub_key";
        boolean isRevoked = false;
        int sequence_number = 0;
        Block block = new Block(owner, sequence_number, previous_hash, public_key, isRevoked);
        Block block2 = new Block(owner, 1, previous_hash, public_key, isRevoked);
        databaseHandler.addBlock(block);
        databaseHandler.addBlock(block2);
        assertEquals(databaseHandler.getBlockBefore(owner, public_key, 1), block);
    }

    /**
     * getAllBlocks test
     * Tests getting all blocks
     */
    @Test
    public void getAllBlocks() {
        String owner = "owner";
        String previous_hash = "prev_hash";
        String public_key = "pub_key";
        boolean isRevoked = false;
        int sequence_number = 0;
        Block block = new Block(owner, sequence_number, previous_hash, public_key, isRevoked);
        Block block2 = new Block(owner, 1, previous_hash, public_key, isRevoked);
        databaseHandler.addBlock(block);
        databaseHandler.addBlock(block2);
        List<Block> result = new ArrayList<>();
        result.add(block);
        result.add(block2);
        assertEquals(databaseHandler.getAllBlocks(), result);
    }

    /**
     * Test for onUpgrade
     */
    @Test
    public void onUpgrade() {
        SQLiteDatabase database = databaseHandler.getReadableDatabase();
        databaseHandler.onUpgrade(database, 0, 1);
        assertEquals(databaseHandler.getReadableDatabase(), database);
    }

    /**
     *
     */
    @After
    public void tearDown() {
        databaseHandler.close();
    }
}