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
import nl.tudelft.b_b_w.model.block.Block;
import nl.tudelft.b_b_w.model.block.BlockFactory;

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
    private final String owner2 = "owner2";
    private final int sequenceNumber = 1;
    private String ownHash;
    private final String previousHashChain = "N/A";
    private final String previousHashSender = "N/A";
    private final String NA = "N/A";
    private final String iban = "iban";
    private final String publicKey = "publicKey";
    private final String chainhash = "chainhash";
    private final int FIRST_BLOCK_INDEX = 1;
    private final int SECOND_BLOCK_INDEX = FIRST_BLOCK_INDEX + 1;
    private Block _block;
    private final int trustValue = TrustValues.INITIALIZED.getValue();
    /**
     * setUp method
     * Does this method before every test
     * Initializes the database handler
     */
    @Before
    public void setUp() throws HashException{
        this.getDatabaseHandler = new GetDatabaseHandler(RuntimeEnvironment.application);
        this.mutateDatabaseHandler = new MutateDatabaseHandler(RuntimeEnvironment.application);
        _block =  BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                FIRST_BLOCK_INDEX,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        ownHash = _block.getOwnHash();
    }

    /**
     * onAddBlock test
     * Tests adding a block
     */
    @Test
    public void addBlock() throws HashException{
        mutateDatabaseHandler.addBlock(_block);
        assertEquals(_block, getDatabaseHandler.getBlock(owner, publicKey, sequenceNumber));
    }

    /**
     * addBlock2 test
     * Tests adding a block and a revoke block
     */
    @Test
    public void addBlock2() throws HashException {
        mutateDatabaseHandler.addBlock(_block);
        final Block newBlock = BlockFactory.getBlock(
                TYPE_REVOKE,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                _block.getOwnHash(),
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
    public void getNullBlock() throws HashException {
        assertNull(getDatabaseHandler.getLatestBlock("null"));
    }

    /**
     * containsBlock/2 test
     * Tests whether a block exists
     */
    @Test
    public void containsBlock2() throws HashException {
        mutateDatabaseHandler.addBlock(_block);
        assertTrue(getDatabaseHandler.containsBlock(owner, publicKey));
    }

    /**
     * containsBlock/3 test
     * Tests whether a block exists
     */
    @Test
    public void containsBlock3() throws HashException {
        mutateDatabaseHandler.addBlock(_block);
        assertTrue(getDatabaseHandler.containsBlock(owner, publicKey, sequenceNumber));
    }

    /**
     * containsBlock/2 test
     * Tests whether a block exists
     * Forces a false
     */
    @Test
    public void containsBlock2False() throws HashException {
        mutateDatabaseHandler.addBlock(_block);
        assertFalse(getDatabaseHandler.containsBlock(owner, "pub_key2", sequenceNumber));
    }

    /**
     * getLatestSeqNum test
     * Tests the latest sequence number of a block
     */
    @Test
    public void getLatestSeqNum() throws HashException {
        mutateDatabaseHandler.addBlock(_block);
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                "chainhash",
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
    public void getContactName() throws HashException {
        mutateDatabaseHandler.addBlock(_block);
        final String hash = "ownHash2";
        final String randomSenderHash = "Hash44324";
        Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                randomSenderHash,
                ownHash,
                hash,
                publicKey,
                iban,
                trustValue);
        mutateDatabaseHandler.addBlock(_block);
        mutateDatabaseHandler.addBlock(block2);
        assertEquals("Unknown", getDatabaseHandler.getContactName(hash));

    }

    /**
     * getOwnerName test
     * Test getting the owner name given hash key.
     */
    @Test
    public void getContactName1() throws HashException {
        final String hash = "ownHash2";
        final String randomSenderHash = "N/A";
        Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                "Jack",
                SECOND_BLOCK_INDEX,
                hash,
                chainhash,
                ownHash,
                publicKey,
                iban,
                trustValue);
        mutateDatabaseHandler.addBlock(_block);
        mutateDatabaseHandler.addBlock(block2);
        assertEquals(_block.getOwner().getName(), getDatabaseHandler.getContactName(ownHash));
    }

    /**
     * getLatestBlock test
     * Tests the latest block
     */
    @Test
    public void getLatestBlock() throws HashException {
        mutateDatabaseHandler.addBlock(_block);
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner2,
                getDatabaseHandler.lastSeqNumberOfChain(owner) + 1,
                ownHash,
                NA,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        mutateDatabaseHandler.addBlock(block2);
        final Block expectBlock = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner2,
                getDatabaseHandler.lastSeqNumberOfChain(owner),
                ownHash,
                NA,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        assertEquals(expectBlock, getDatabaseHandler.getLatestBlock("owner2"));
    }

    /**
     * getAllBlocks test
     * Tests getting all blocks
     */
    @Test
    public void getAllBlocks() throws HashException {
        mutateDatabaseHandler.addBlock(_block);
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner2,
                getDatabaseHandler.lastSeqNumberOfChain(owner),
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
    public void updateBlockTest() throws HashException {
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
    public void checkExistsRegular() throws HashException {
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
    public void checkExistsRevoked() throws HashException {
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
    public void checkExistsAgain() throws HashException {
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
                "chainhash",
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
    public void checkExistsOtherKey() throws HashException {
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
     * Test to check whether getting a block by its hash value and owner works
     */
    @Test
    public void getByHash() throws HashException {
        mutateDatabaseHandler.addBlock(_block);
        assertEquals(_block, getDatabaseHandler.getByHash(ownHash));
    }

    /**
     * Closes database connection after test
     */
    @After
    public void tearDown() throws HashException {
        mutateDatabaseHandler.clearAllBlocks();
        getDatabaseHandler.close();
        mutateDatabaseHandler.close();
    }
}
