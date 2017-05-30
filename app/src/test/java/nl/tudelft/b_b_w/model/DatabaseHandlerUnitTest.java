package nl.tudelft.b_b_w.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk= 21,  manifest = "src/main/AndroidManifest.xml")
public class DatabaseHandlerUnitTest {

    private GetDatabaseHandler getDatabaseHandler;
    private MutateDatabaseHandler mutateDatabaseHandler;
    private final String TYPE_BLOCK = "BLOCK";
    private final String TYPE_REVOKE = "REVOKE";
    private final String owner = "owner";
    private final int sequenceNumber = 1;
    private final String ownHash = "ownHash";
    private final String previousHashChain = "previousHashChain";
    private final String previousHashSender = "N/A";
    private final String iban = "iban";
    private final String publicKey = "publicKey";
    private Block _block;
    private final int trustValue = 0;
    /**
     * setUp method
     * Does this method before every test
     * Initializes the database handler
     */
    @Before
    public void setUp() {
        this.getDatabaseHandler = new GetDatabaseHandler(RuntimeEnvironment.application);
        this.mutateDatabaseHandler = new MutateDatabaseHandler(RuntimeEnvironment.application);
        _block =  BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        _block.setSeqNumberTo(sequenceNumber);
    }

    /**
     * onAddBlock test
     * Tests adding a block
     */
    @Test
    public void addBlock() {
        mutateDatabaseHandler.addBlock(_block);
        assertEquals(_block, getDatabaseHandler.getBlock(owner, publicKey, sequenceNumber));
    }

    /**
     * addBlock2 test
     * Tests adding a block and a revoke block
     */
    @Test
    public void addBlock2() {
        final Block newBlock = BlockFactory.getBlock("REVOKE", owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        mutateDatabaseHandler.addBlock(_block);
        mutateDatabaseHandler.addBlock(newBlock);
        newBlock.setSeqNumberTo(2);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        list.add(newBlock);
        assertEquals(list, getDatabaseHandler.getAllBlocks(owner));
    }

    /**
     * getNullBlock test
     * Tests getting a non-existing block
     */
    @Test
    public void getNullBlock() {
        assertNull(getDatabaseHandler.getLatestBlock("null"));
    }

    /**
     * containsBlock/2 test
     * Tests whether a block exists
     */
    @Test
    public void containsBlock2() {
        mutateDatabaseHandler.addBlock(_block);
        assertTrue(getDatabaseHandler.containsBlock(owner, publicKey));
    }

    /**
     * containsBlock/3 test
     * Tests whether a block exists
     */
    @Test
    public void containsBlock3() {
        mutateDatabaseHandler.addBlock(_block);
        assertTrue(getDatabaseHandler.containsBlock(owner, publicKey, sequenceNumber));
    }

    /**
     * containsBlock/2 test
     * Tests whether a block exists
     * Forces a false
     */
    @Test
    public void containsBlock2_false() {
        mutateDatabaseHandler.addBlock(_block);
        assertFalse(getDatabaseHandler.containsBlock(owner, "pub_key2", sequenceNumber));
    }

    /**
     * getLatestSeqNum test
     * Tests the latest sequence number of a block
     */
    @Test
    public void getLatestSeqNum() {
        final Block block2 = BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        mutateDatabaseHandler.addBlock(_block);
        mutateDatabaseHandler.addBlock(block2);
        assertEquals(2, getDatabaseHandler.getLatestSeqNum(owner, publicKey));

    }



    /**
     * getOwnerName test
     * Test getting the owner name given hash key.
     */
    @Test
    public void getContactName() {
        final String hash = "ownHash2";
        final String randomSenderHash = "Hash44324";
        Block block2 = BlockFactory.getBlock(TYPE_BLOCK, owner, randomSenderHash,
                ownHash, hash, publicKey, iban, trustValue);
        mutateDatabaseHandler.addBlock(_block);
        mutateDatabaseHandler.addBlock(block2);
        assertEquals("Unknown", getDatabaseHandler.getContactName(hash));

    }

    /**
     * getOwnerName test
     * Test getting the owner name given hash key.
     */
    @Test
    public void getContactName1() {
        final String hash = "ownHash2";
        final String randomSenderHash = "Hash44324";
        Block block2 = BlockFactory.getBlock(TYPE_BLOCK, "Jack", hash,
                randomSenderHash, ownHash, publicKey, iban, trustValue);
        mutateDatabaseHandler.addBlock(_block);
        mutateDatabaseHandler.addBlock(block2);
        System.out.println(getDatabaseHandler.getAllBlocks(owner).toString());
        assertEquals(_block.getOwner(), getDatabaseHandler.getContactName(ownHash));

    }

    /**
     * getLatestBlock test
     * Tests the latest block
     */
    @Test
    public void getLatestBlock() {
        final Block block2 = BlockFactory.getBlock(TYPE_BLOCK, "owner2", ownHash,
                previousHashChain, previousHashSender, publicKey, iban,trustValue);
        mutateDatabaseHandler.addBlock(_block);
        mutateDatabaseHandler.addBlock(block2);
        final Block expectBlock = BlockFactory.getBlock(TYPE_BLOCK, "owner2", ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        expectBlock.setSeqNumberTo(1);
        assertEquals(expectBlock, getDatabaseHandler.getLatestBlock("owner2"));
    }

    /**
     * getBlockAfter test
     * Tests the block after a block
     */
    @Test
    public void getBlockAfter() {
        final Block block2 = BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        mutateDatabaseHandler.addBlock(_block);
        mutateDatabaseHandler.addBlock(block2);
        final Block expectBlock = BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        expectBlock.setSeqNumberTo(2);
        assertEquals(expectBlock, getDatabaseHandler.getBlockAfter(owner, sequenceNumber));
    }

    /**
     * getBlockBefore test
     * Tests the block before a block
     */
    @Test
    public void getBlockBefore() {
        final Block block2 = BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        mutateDatabaseHandler.addBlock(_block);
        mutateDatabaseHandler.addBlock(block2);
        assertEquals(getDatabaseHandler.getBlockBefore(owner, 2), _block);
    }

    /**
     * getAllBlocks test
     * Tests getting all blocks
     */
    @Test
    public void getAllBlocks() {
        final String owner2 = "owner2";
        final Block block2 = BlockFactory.getBlock(TYPE_BLOCK, owner2, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        block2.setSeqNumberTo(1);
        mutateDatabaseHandler.addBlock(_block);
        mutateDatabaseHandler.addBlock(block2);
        List<Block> result = new ArrayList<>();
        result.add(block2);
        assertEquals(result, getDatabaseHandler.getAllBlocks(owner2));
    }

    /**
     * updateBlockTest
     * Tests whether updating a block works
     */
    @Test
    public void updateBlockTest() {
        final Block block2 = BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        _block.setTrustValue(TrustValues.SUCCESFUL_TRANSACTION.getValue());
        mutateDatabaseHandler.updateBlock(_block);
        assertNotEquals(getDatabaseHandler.getBlock(owner, publicKey, sequenceNumber), block2);
    }

    /**
     * Test for onUpgrade
     */
    @Test
    public void onUpgrade() {
        SQLiteDatabase database = getDatabaseHandler.getReadableDatabase();
        getDatabaseHandler.onUpgrade(database, 0, 1);
        assertEquals(getDatabaseHandler.getReadableDatabase(), database);
    }

    /** Test for if the database is empty. Should not be empty since we add blocks. */
    @Test
    public void checkDatabaseEmpty() {
        getDatabaseHandler = new GetDatabaseHandler(RuntimeEnvironment.application);
        assertTrue(getDatabaseHandler.isDatabaseEmpty());
    }

    /** blockExists call for a regular block */
    @Test
    public void checkExistsRegular() {
        Block b = BlockFactory.getBlock("BLOCK", "Barry", "ownHash", "prevHashChain", "prevHashSender", "pubKey", "IBAN", 0);
        mutateDatabaseHandler.addBlock(b);
        boolean exists = getDatabaseHandler.blockExists("Barry", "pubKey", false);
        assertTrue(exists);
    }

    /** blockExists call for a revoked block */
    @Test
    public void checkExistsRevoked() {
        Block b = BlockFactory.getBlock("BLOCK", "Barry", "ownHash", "prevHashChain", "prevHashSender", "pubKey", "IBAN", 0);
        mutateDatabaseHandler.addBlock(b);
        boolean exists = getDatabaseHandler.blockExists("Barry", "pubKey", true);
        assertFalse(exists);
    }

    /** Add, revoke, then add is not possible. */
    @Test
    public void checkExistsAgain() {
        Block b1 = BlockFactory.getBlock("BLOCK", "Barry", "ownHash", "prevHashChain", "prevHashSender", "pubKey", "IBAN", 0);
        Block b2 = BlockFactory.getBlock("REVOKE", "Barry", "ownHash", "prevHashChain", "prevHashSender", "pubKey", "IBAN", 0);
        mutateDatabaseHandler.addBlock(b1);
        mutateDatabaseHandler.addBlock(b2);
        boolean exists = getDatabaseHandler.blockExists("Barry", "pubKey", false);
        assertTrue(exists);
    }

    /** Add different key should not hit exist. */
    @Test
    public void checkExistsOtherKey() {
        Block b1 = BlockFactory.getBlock("BLOCK", "Barry", "ownHash", "prevHashChain", "prevHashSender", "pubKey1", "IBAN", 0);
        mutateDatabaseHandler.addBlock(b1);
        boolean exists = getDatabaseHandler.blockExists("Barry", "pubKey2", false);
        assertFalse(exists);
    }

    /**
     * Closes database connection after test
     */
    @After
    public void tearDown() {
        mutateDatabaseHandler.clearAllBlocks();
        getDatabaseHandler.close();
        mutateDatabaseHandler.close();
    }
}
