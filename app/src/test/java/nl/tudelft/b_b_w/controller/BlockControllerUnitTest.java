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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk= 21,  manifest = "src/main/AndroidManifest.xml")
public class BlockControllerUnitTest {
    private final String NA = "N/A";
    private BlockController blockController;
    private final String publicKey = "publicKey";

    @Deprecated
    private final String TYPE_BLOCK = "BLOCK";

    @Deprecated
    private final String TYPE_REVOKE = "REVOKE";

    private final User a = new User("Alice", "ibanA");
    private final User b = new User("Bob", "ibanB");
    private final User c = new User("Clara", "ibanC");

    private Block genesisA;
    private Block genesisB;
    private Block blockWithOwnerAAddsKeyKa;
    private Block blockWithOwnerAAddsKeyKb;
    private Block blockWithOwnerBAddsKeyKa;
    private Block blockWithOwnerBRevokesKeyKa;

    /**
     * Initialize BlockController before every test
     * And initialize a dummy block _block
     */
    @Before
    public final void setUp() throws HashException {
        blockController = new BlockController(RuntimeEnvironment.application);
        // construct an easy blockchain
        genesisA = blockController.createGenesis(a);
        genesisB = blockController.createGenesis(b);
        blockWithOwnerAAddsKeyKa = blockController.createKeyBlock(a, a, "ka");
        blockWithOwnerAAddsKeyKb = blockController.createKeyBlock(a, b, "kb");
        blockWithOwnerBAddsKeyKa = blockController.createKeyBlock(b, a, "ka");
        blockWithOwnerBRevokesKeyKa = blockController.createRevokeBlock(b, a, "ka", "ibanA");
    }

    /**
     * Tests adding a block
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void testAddBlock() throws Exception {
        Block genesisC = blockController.createGenesis(c);
        Block blockC = blockController.createKeyBlock(c, c, "kc");
        List<Block> list = new ArrayList<>();
        list.add(genesisC);
        list.add(blockC);
        assertEquals(list, blockController.getBlocks(c.getName()));
    }

    /** Non-existing contact */
    @Test
    public final void getContactNameTest() throws HashException {
        assertEquals("Unknown", blockController.getContactName("INVALID"));
    }

    /**
     * Tests to return the latest block
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void testGetLatestBlock() throws Exception {
        blockController.createGenesis(c);
        Block blockC = blockController.createKeyBlock(c, c, "kc");
        assertEquals(blockC, blockController.getLatestBlock(c.getName()));
    }

    /**
     * Tests returning the latest sequence number of chain
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void testGetLatestSeqNumber() throws Exception {
        assertEquals(3, blockController.getLatestSeqNumber(a.getName()));
    }

    /**
     * Tests adding a duplicate block thrice
     */
    @Test(expected = RuntimeException.class)
    public final void testAddDupBlocks() throws HashException {
        Block blockC = blockController.createKeyBlock(c, c, "pkc");
        blockController.addBlock(blockC);
        blockController.addBlock(blockC);
    }

    /**
     * Tests adding an already revoked block: in this case block
     */
    @Test(expected = RuntimeException.class)
    public final void alreadyRevoked() throws HashException {
        blockController.createKeyBlock(b, a, "pka");
    }

    /**
     * Tests filtering duplicates out of a list
     */
    @Test
    public final void testEmptyList() throws HashException {
        Block genesisC = blockController.createGenesis(c);
        blockController.createKeyBlock(c, c, "pkc");
        blockController.createRevokeBlock(c, c, "pkc", "ibanC");
        List<Block> list = new ArrayList<>();
        list.add(genesisC);
        assertEquals(list, blockController.getBlocks(c.getName()));
    }

    /**
     * verifyIBAN test
     * Tests whether verifying the IBAN updates the trust value
     */
    @Test
    public final void testVerifyIBAN() {
        Block b = blockWithOwnerAAddsKeyKb;
        blockController.verifyIBAN(b);
        assertEquals(TrustValues.VERIFIED.getValue(), b.getTrustValue());
    }

    /**
     * successfulTransaction test
     * Tests whether a successful transaction updates the trust value
     */
    @Test
    public final void testSuccessfulTransaction() {
        Block b = blockWithOwnerAAddsKeyKb;
        blockController.successfulTransaction(b);
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
        blockController.failedTransaction(b);
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
        blockController.revokedTrustValue(b);
        assertEquals(TrustValues.REVOKED.getValue(), b.getTrustValue());
    }

    /** Check that the genesis block is created */
    @Test
    public final void verifyGenesisCreation() { assertNotNull(genesisA); }

    /** Check that genesis has the correct index */
    @Test
    public final void verifyGenesisIndex() {
        assertEquals(1, genesisA.getSequenceNumber());
    }

    /** Verify genesis owner A */
    @Test
    public final void verifyGenesisOwner() {
        assertEquals(a, genesisA.getOwner());
    }

    /** Verify the block A-A1 was created */
    @Test
    public final void verifyBlockA_A1Creation() {
        assertNotNull(blockWithOwnerAAddsKeyKa);
    }

    /** Verify the block A-B1 was created */
    @Test
    public final void verifyBlockA_B1Creation() {
        assertNotNull(blockWithOwnerAAddsKeyKb);
    }

    /** Blocks without sender should not have a sender hash */
    @Test
    public final void verifyBlockChainHash() throws HashException {
        assertEquals("N/A", blockWithOwnerAAddsKeyKa.getPreviousHashSender());
    }

    /** Is the revoked block indeed revoked? */
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
        blockController.createGenesis(c);
        Block fresh = blockController.createKeyBlock(c, c, "pkc");
        assertEquals(fresh, blockController.backtrack(fresh));
    }

    /**
     * verifyTrustworthiness test
     * Tests whether a block is trustworthy
     */
    @Test
    public void testVerifyTrustworthiness() throws HashException {
        blockController.createGenesis(c);
        Block b = blockController.createKeyBlock(c, c, "pk");
        assertTrue(blockController.verifyTrustworthiness(b));
    }

    /**
     * verifyTrustworthiness test
     * Tests whether a block is trustworthy
     * Forces a false
     */
    @Test
    public void testVerifyTrustworthinessFalse() throws HashException {
        blockController.createGenesis(c);
        Block b1 = blockController.createKeyBlock(c, c, "pk1");
        Block b2 = blockController.createKeyBlock(c, c, "pk2");
        assertFalse(blockController.verifyTrustworthiness(b2));
    }

    /**
     * Closes database connection after test
     */
    @After
    public final void tearDown() {
        blockController.clearAllBlocks();
    }
}
