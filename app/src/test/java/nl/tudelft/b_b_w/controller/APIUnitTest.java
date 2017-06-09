package nl.tudelft.b_b_w.controller;

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
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.TrustValues;
import nl.tudelft.b_b_w.model.User;
import nl.tudelft.b_b_w.model.block.Block;
import nl.tudelft.b_b_w.model.block.BlockData;
import nl.tudelft.b_b_w.model.block.BlockFactory;
import nl.tudelft.b_b_w.model.block.BlockType;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class APIUnitTest {
    private final String notAvailable = "N/A";
    private final String publicKey = "publicKey";
    private final String publicKeyC = "pkc";
    @Deprecated
    private final String typeBlock = "BLOCK";
    @Deprecated
    private final String typeRevoke = "REVOKE";
    private final User a = new User("Alice", "ibanA");
    private final User b = new User("Bob", "ibanB");
    private final User c = new User("Clara", "ibanC");
    private API mAPI;
    private Block genesisA;
    private Block genesisB;
    private Block blockWithOwnerAAddsKeyKa;
    private Block blockWithOwnerAAddsKeyKb;
    private Block blockWithOwnerBAddsKeyKa;
    private Block blockWithOwnerBRevokesKeyKa;

    /**
     * Initialize API before every test
     * And initialize a dummy block _block
     */
    @Before
    public final void setUp() throws HashException {
        mAPI = new API(RuntimeEnvironment.application);
        // construct an easy blockchain
        genesisA = mAPI.createGenesis(a);
        genesisB = mAPI.createGenesis(b);
        blockWithOwnerAAddsKeyKa = mAPI.createKeyBlock(a, a, "ka");
        blockWithOwnerAAddsKeyKb = mAPI.createKeyBlock(a, b, "kb");
        blockWithOwnerBAddsKeyKa = mAPI.createKeyBlock(b, a, "ka");
        blockWithOwnerBRevokesKeyKa = mAPI.createRevokeBlock(b, a, "ka", "ibanA");
    }

    /**
     * Tests adding a block
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void testAddBlock() throws Exception {
        Block genesisC = mAPI.createGenesis(c);
        Block blockC = mAPI.createKeyBlock(c, c, "kc");
        List<Block> list = new ArrayList<>();
        list.add(genesisC);
        list.add(blockC);
        assertEquals(list, mAPI.getBlocks(c.getName()));
    }

    /**
     * Non-existing contact
     */
    @Test
    public final void getContactNameTest() throws HashException {
        assertEquals("Unknown", mAPI.getContactName("INVALID"));
    }

    /**
     * Tests to return the latest block
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void testGetLatestBlock() throws Exception {
        mAPI.createGenesis(c);
        Block blockC = mAPI.createKeyBlock(c, c, "kc");
        assertEquals(blockC, mAPI.getLatestBlock(c.getName()));
    }

    /**
     * Tests returning the latest sequence number of chain
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void testGetLatestSeqNumber() throws Exception {
        assertEquals(3, mAPI.getLatestSeqNumber(a.getName()));
    }

    /**
     * Tests adding a duplicate block thrice
     */
    @Test(expected = RuntimeException.class)
    public final void testAddDupBlocks() throws HashException {
        Block blockC = mAPI.createKeyBlock(c, c, publicKeyC);
        mAPI.addBlock(blockC);
        mAPI.addBlock(blockC);
    }

    /**
     * Tests adding an already revoked block: in this case block
     */
    @Test(expected = RuntimeException.class)
    public final void alreadyRevoked() throws HashException {
        mAPI.createKeyBlock(b, a, "ka");
    }

    /**
     * Tests filtering duplicates out of a list
     */
    @Test
    public final void testEmptyList() throws HashException {
        Block genesisC = mAPI.createGenesis(c);
        mAPI.createKeyBlock(c, c, "pkc");
        mAPI.createRevokeBlock(c, c, "pkc", "ibanC");
        List<Block> list = new ArrayList<>();
        list.add(genesisC);
        assertEquals(list, mAPI.getBlocks(c.getName()));
    }

    /**
     * verifyIBAN test
     * Tests whether verifying the IBAN updates the trust value
     */
    @Test
    public final void testVerifyIBAN() {
        Block b = blockWithOwnerAAddsKeyKb;
        mAPI.verifyIBAN(b);
        assertEquals(TrustValues.VERIFIED.getValue(), b.getTrustValue());
    }

    /**
     * successfulTransaction test
     * Tests whether a successful transaction updates the trust value
     */
    @Test
    public final void testSuccessfulTransaction() {
        Block b = blockWithOwnerAAddsKeyKb;
        mAPI.successfulTransaction(b);
        assertEquals(TrustValues.INITIALIZED.getValue() + TrustValues.SUCCESFUL_TRANSACTION.getValue(),
                b.getTrustValue());
    }

    /**
     * failedTransaction test
     * Tests whether a successful transaction updates the trust value
     */
    @Test
    public final void testFailedTransaction() {
        Block b = blockWithOwnerAAddsKeyKb;
        mAPI.failedTransaction(b);
        assertEquals(TrustValues.INITIALIZED.getValue() + TrustValues.FAILED_TRANSACTION.getValue(),
                b.getTrustValue());
    }

    /**
     * revokedTrustValue test
     * Tests whether a revoked transaction updates the trust value
     */
    @Test
    public final void testRevokedTrustValue() {
        Block b = blockWithOwnerAAddsKeyKb;
        mAPI.revokedTrustValue(b);
        assertEquals(TrustValues.REVOKED.getValue(), b.getTrustValue());
    }

    /**
     * Check that the genesis block is created
     */
    @Test
    public final void verifyGenesisCreation() {
        assertNotNull(genesisA);
    }

    /**
     * Check that genesis has the correct index
     */
    @Test
    public final void verifyGenesisIndex() {
        assertEquals(1, genesisA.getSequenceNumber());
    }

    /**
     * Verify genesis owner A
     */
    @Test
    public final void verifyGenesisOwner() {
        assertEquals(a, genesisA.getOwner());
    }

    /**
     * Verify the block A-A1 was created
     */
    @Test
    public final void verifyBlockAA1Creation() {
        assertNotNull(blockWithOwnerAAddsKeyKa);
    }

    /**
     * Verify the block A-B1 was created
     */
    @Test
    public final void verifyBlockAB1Creation() {
        assertNotNull(blockWithOwnerAAddsKeyKb);
    }

    /**
     * Blocks without sender should not have a sender hash
     */
    @Test
    public final void verifyBlockChainHash() throws HashException {
        assertEquals("N/A", blockWithOwnerAAddsKeyKa.getPreviousHashSender());
    }

    /**
     * Is the revoked block indeed revoked?
     */
    @Test
    public final void verifyRevoked() {
        assertTrue(blockWithOwnerBRevokesKeyKa.isRevoked());
    }

    /**
     * backtrack test
     * Tests whether backtracking from previousHashSender works
     */
    @Test
    public void testBacktrack() throws HashException {
        mAPI.createGenesis(c);
        Block fresh = mAPI.createKeyBlock(c, c, "pkc");
        assertEquals(fresh, mAPI.backtrack(fresh));
    }

    /**
     * verifyTrustworthiness test
     * Tests whether a block is trustworthy
     */
    @Test
    public void testVerifyTrustworthiness() throws HashException {
        mAPI.createGenesis(c);
        Block b = mAPI.createKeyBlock(c, c, "pk");
        assertTrue(mAPI.verifyTrustworthiness(b));
    }

    /**
     * verifyTrustworthiness test
     * Tests whether a block is trustworthy
     * Forces a false
     */
    @Test
    public void testVerifyTrustworthinessFalse() throws HashException {
        mAPI.createGenesis(c);
        Block b1 = mAPI.createKeyBlock(c, c, "pk1");
        BlockData data = new BlockData();
        data.setBlockType(BlockType.ADD_KEY);
        data.setPublicKey("pk2");
        data.setPreviousHashChain("DUMMY");
        data.setPreviousHashSender("DUMMY");
        data.setTrustValue(0);
        data.setOwner(c);
        data.setSequenceNumber(3);
        Block b2 = BlockFactory.createBlock(data);

        assertFalse(mAPI.verifyTrustworthiness(b2));
    }

    /**
     * Closes database connection after test
     */
    @After
    public final void tearDown() {
        mAPI.clearAllBlocks();
    }
}
