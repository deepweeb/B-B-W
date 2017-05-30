package nl.tudelft.b_b_w.model;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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

    private GetDatabaseHandler getDatabaseHandler;
    private MutateDatabaseHandler mutateDatabaseHandler;
    private final String TYPE_BLOCK = "BLOCK";
    private final String TYPE_REVOKE = "REVOKE";
    private final String owner = "owner";
    private final int sequenceNumber = 1;
    private final String ownHash = "ownHash";
    private final String previousHashChain = "previousHashChain";
    private final String previousHashSender = "previousHashSender";
    private final String iban = "iban";
    private final String publicKey = "publicKey";
    private Block _block;
    private final int trustValue = TrustValues.INITIALIZED.getValue();
    /**
     * setUp method
     * Does this method before every test
     * Initializes the database handler
     */
    @Before
    public void setUp() {
        this.getDatabaseHandler = new GetDatabaseHandler(RuntimeEnvironment.application);
        this.mutateDatabaseHandler = new MutateDatabaseHandler(RuntimeEnvironment.application);
        _block =  BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
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
        mutateDatabaseHandler.addBlock(_block);
        final Block newBlock = BlockFactory.getBlock(
                TYPE_REVOKE,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(newBlock);
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
        mutateDatabaseHandler.addBlock(_block);
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(block2);
        assertEquals(2, getDatabaseHandler.getLatestSeqNum(owner, publicKey));

    }



    /**
     * getOwnerName test
     * Test getting the owner name given hash key.
     */
    @Test
    public void getContactName() {
        mutateDatabaseHandler.addBlock(_block);
        final String hash = "ownHash2";
        final String randomSenderHash = "Hash44324";
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                hash,
                ownHash,
                randomSenderHash,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(block2);
        System.out.println("block2.getSequenceNumber() = " + block2.getSequenceNumber());
        assertEquals(owner+"'s friend #" + block2.getSequenceNumber(), getDatabaseHandler.getContactName(hash));
    }

    /**
     * getLatestBlock test
     * Tests the latest block
     */
    @Test
    public void getLatestBlock() {
        mutateDatabaseHandler.addBlock(_block);
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                "owner2",
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(block2);
        final Block expectBlock = BlockFactory.getBlock(
                TYPE_BLOCK,
                "owner2",
                getDatabaseHandler.lastSeqNumberOfChain(owner),
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        assertEquals(expectBlock, getDatabaseHandler.getLatestBlock("owner2"));
    }

    /**
     * getBlockAfter test
     * Tests the block after a block
     */
    @Test
    public void getBlockAfter() {
        mutateDatabaseHandler.addBlock(_block);
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(block2);
        final Block expectBlock = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner),
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        assertEquals(expectBlock, getDatabaseHandler.getBlockAfter(owner, sequenceNumber));
    }

    /**
     * getBlockBefore test
     * Tests the block before a block
     */
    @Test
    public void getBlockBefore() {
        mutateDatabaseHandler.addBlock(_block);
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(block2);
        assertEquals(getDatabaseHandler.getBlockBefore(owner, 2), _block);
    }

    /**
     * getAllBlocks test
     * Tests getting all blocks
     */
    @Test
    public void getAllBlocks() {
        mutateDatabaseHandler.addBlock(_block);
        final String owner2 = "owner2";
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner2,getDatabaseHandler.lastSeqNumberOfChain(owner),
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
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
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
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
        Block b = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(b);
        boolean exists = getDatabaseHandler.blockExists(owner, publicKey, false);
        assertTrue(exists);
    }

    /** blockExists call for a revoked block */
    @Test
    public void checkExistsRevoked() {
        Block b = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(b);
        boolean exists = getDatabaseHandler.blockExists(owner, publicKey, true);
        assertFalse(exists);
    }

    /** Add, revoke, then add is not possible. */
    @Test
    public void checkExistsAgain() {
        Block b1 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(b1);
        Block b2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(b2);
        boolean exists = getDatabaseHandler.blockExists(owner, publicKey, false);
        assertTrue(exists);
    }

    /** Add different key should not hit exist. */
    @Test
    public void checkExistsOtherKey() {
        Block b1 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(b1);
        boolean exists = getDatabaseHandler.blockExists(owner, publicKey+"2", false);
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
