package nl.tudelft.b_b_w.ModelsUnitTest;

import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.DatabaseHandler;

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
@Config(constants = BuildConfig.class,sdk= 21,  manifest = "src/main/AndroidManifest.xml")
public class DatabaseHandlerUnitTest {

    private DatabaseHandler databaseHandler;
    final String owner = "owner";
    final int sequenceNumber = 1;
    final String ownHash = "ownHash";
    final String previousHashChain = "previousHashChain";
    final String previousHashSender = "previousHashSender";
    private final String iban = "iban";
    final String publicKey = "publicKey";
    final boolean isRevoked = false;
    Block _block;

    /**
     * setUp method
     * Does this method before every test
     * Initializes the database handler
     */
    @Before
    public void setUp() {
        this.databaseHandler = new DatabaseHandler(RuntimeEnvironment.application);
        _block = new Block(owner, sequenceNumber, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
}

    /**
     * onAddBlock test
     * Tests adding a block
     */
    @Test
    public void addBlock() {
        databaseHandler.addBlock(_block);
        assertEquals(_block, databaseHandler.getBlock(owner, publicKey, sequenceNumber));
    }

    /**
     * addBlock2 test
     * Tests adding a block and a revoke block
     */
    @Test
    public void addBlock2() {
        Block newBlock = new Block(owner, sequenceNumber+1, ownHash, previousHashChain, previousHashSender, publicKey,iban, !isRevoked);
        databaseHandler.addBlock(_block);
        for(Block e:databaseHandler.getAllBlocks(owner) )
        {
            System.out.println(e.getSequenceNumber());
        }

        databaseHandler.addBlock(newBlock);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        list.add(newBlock);
        assertEquals(list, databaseHandler.getAllBlocks(owner));
    }

    /**
     * getNullBlock test
     * Tests getting a non-existing block
     */
    @Test
    public void getNullBlock() {
        assertNull(databaseHandler.getLatestBlock("null"));
    }

    /**
     * containsBlock/2 test
     * Tests whether a block exists
     */
    @Test
    public void containsBlock2() {
        databaseHandler.addBlock(_block);
        assertTrue(databaseHandler.containsBlock(owner, publicKey));
    }

    /**
     * containsBlock/3 test
     * Tests whether a block exists
     */
    @Test
    public void containsBlock3() {
        databaseHandler.addBlock(_block);
        assertTrue(databaseHandler.containsBlock(owner, publicKey, sequenceNumber));
    }

    /**
     * containsBlock/2 test
     * Tests whether a block exists
     * Forces a false
     */
    @Test
    public void containsBlock2_false() {
        databaseHandler.addBlock(_block);
        assertFalse(databaseHandler.containsBlock(owner, "pub_key2", sequenceNumber));
    }

    /**
     * getLatestSeqNum test
     * Tests the latest sequence number of a block
     */
    @Test
    public void getLatestSeqNum() {

        Block block2 = new Block(owner, 99, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
        databaseHandler.addBlock(_block);
        databaseHandler.addBlock(block2);
        assertEquals(2, databaseHandler.getLatestSeqNum(owner, publicKey));

    }

    /**
     * getLatestBlock test
     * Tests the latest block
     */
    @Test
    public void getLatestBlock() {
        Block block2 = new Block(owner, 99, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
        databaseHandler.addBlock(_block);
        databaseHandler.addBlock(block2);
        Block expectBlock = new Block(owner, 2, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
        assertEquals(expectBlock, databaseHandler.getLatestBlock(owner));
    }

    /**
     * getBlockAfter test
     * Tests the block after a block
     */
    @Test
    public void getBlockAfter() {
        Block block2 = new Block(owner, 99, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
        databaseHandler.addBlock(_block);
        databaseHandler.addBlock(block2);
        Block expectBlock = new Block(owner, 2, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
        assertEquals(expectBlock, databaseHandler.getBlockAfter(owner, sequenceNumber));
    }

    /**
     * getBlockBefore test
     * Tests the block before a block
     */
    @Test
    public void getBlockBefore() {
        Block block2 = new Block(owner, 99, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
        databaseHandler.addBlock(_block);
        databaseHandler.addBlock(block2);
        assertEquals(databaseHandler.getBlockBefore(owner, 2), _block);
    }

    /**
     * getAllBlocks test
     * Tests getting all blocks
     */
    @Test
    public void getAllBlocks() {
        String owner2 = "owner2";
        Block block2 = new Block(owner2, 1, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
        databaseHandler.addBlock(_block);
        databaseHandler.addBlock(block2);
        List<Block> result = new ArrayList<>();
        result.add(block2);
        assertEquals(result, databaseHandler.getAllBlocks(owner2));
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
     * Closes database connection after test
     */
    @After
    public void tearDown() {
        databaseHandler.close();
    }
}