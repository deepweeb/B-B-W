package nl.tudelft.b_b_w.controller;

import android.content.res.Resources;

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
import nl.tudelft.b_b_w.model.block.BlockFactory;

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
@Config(constants = BuildConfig.class,sdk= 21,  manifest = "src/main/AndroidManifest.xml")
public class BlockControllerUnitTest {

    /**
     * Attributes
     */
    private final String NA = "N/A";
    private BlockController blockController;
    private final String owner = "A";
    private final int sequenceNumber = 1;
    private final String ownHash = "ownHash";
    private final String previousHashChain = "previousHashChain";
    private final String previousHashSender = "previousHashSender";
    private final String iban = "iban";
    private final String publicKey = "publicKey";
    private Block _block;
    private final int trustValue = TrustValues.INITIALIZED.getValue();
    private final String TYPE_BLOCK = "BLOCK";
    private final String TYPE_REVOKE = "REVOKE";

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
        User a = new User("A", "ibanA");
        User b = new User("B", "ibanB");
        genesisA = blockController.createGenesis(a);
        genesisB = blockController.createGenesis(b);
        _block = genesisA;
        blockWithOwnerAAddsKeyKa = blockController.createKeyBlock(a, a, "ka", "ibanA");
        blockWithOwnerAAddsKeyKb = blockController.createKeyBlock(a, b, "kb", "ibanB");
        blockWithOwnerBAddsKeyKa = blockController.createKeyBlock(b, a, "ka", "ibanA");
        blockWithOwnerBRevokesKeyKa = blockController.createRevokeBlock(b, a, "ka", "ibanA");
    }

    /**
     * Tests adding a block
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void testAddBlock() throws Exception {
        blockController.addBlock(_block);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        assertEquals(blockController.getBlocks(owner), list);
    }


    /**
     * getOwnerName test no 2
     * Test getting the owner name given hash key.
     */
    @Test
    public final void getContactNameTest2() throws HashException {
        blockController.addBlock(_block);
        final Block block2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                blockController.getLatestSeqNumber(owner) + 1,
                "ownHash2",
                ownHash,
                "Hash44324",
                publicKey + "2",
                iban,
                trustValue
        );
        blockController.addBlock(block2);
        assertEquals("Unknown", blockController.getContactName(ownHash));
    }

    /**
     * Tests to return the latest block
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void testGetLatestBlock() throws Exception {
        Block expected = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                blockController.getLatestSeqNumber(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        blockController.addBlock(_block);
        assertEquals(expected, blockController.getLatestBlock(owner));
    }

    /**
     * Tests returning the latest sequence number of chain
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void testGetLatestSeqNumber() throws Exception {
        final String newOwner = owner + "2";
        final Block newBlock = BlockFactory.getBlock(
                TYPE_BLOCK,
                newOwner,
                blockController.getLatestSeqNumber(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        blockController.addBlock(_block);
        blockController.addBlock(newBlock);


        assertEquals(1, blockController.getLatestSeqNumber(owner));
    }


    /**
     * Tests adding two blocks
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void testAddBlock2() throws Exception {
        final String newOwner = owner + "2";
        Block newBlock = BlockFactory.getBlock(
                TYPE_BLOCK,
                newOwner,
                blockController.getLatestSeqNumber(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        blockController.addBlock(_block);
        blockController.addBlock(newBlock);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        list.add(newBlock);

        List<Block> newList = blockController.getBlocks(owner);
        newList.addAll(blockController.getBlocks(newOwner));

        assertEquals(list, newList);
    }

    /**
     * Tests adding a duplicate block
     */
    @Test(expected = RuntimeException.class)
    public final void testAddDupBlocks() {
        blockController.addBlock(_block);
        blockController.addBlock(_block);
    }

    /**
     * Tests adding an already revoked block
     */
    @Test(expected = RuntimeException.class)
    public final void alreadyRevoked() throws HashException {
        final Block newBlock = BlockFactory.getBlock(
                TYPE_REVOKE,
                owner,
                blockController.getLatestSeqNumber(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
        blockController.addBlockToChain(newBlock);
        blockController.addBlockToChain(_block);
    }

    /**
     * Tests filtering duplicates out of a list
     */
    @Test
    public final void testEmptyList() throws HashException {
        blockController.addBlock(_block);
        blockController.revokeBlock(_block);
        List<Block> list = new ArrayList<>();
        assertEquals(list, blockController.getBlocks(owner));
    }

    /**
     * Test removeBlock if the specified revoked block has no match
     */
    @Test
    public final void testRemoveWithNoMatch() throws Resources.NotFoundException, HashException {
        blockController.addBlock(_block);
        final Block blc2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner + "2",
                blockController.getLatestSeqNumber(owner) + 1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey + "2",
                iban,
                trustValue
        );
        blockController.revokeBlock(blc2);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        assertEquals(list, blockController.getBlocks(owner));
    }

    /**
     * verifyIBAN test
     * Tests whether verifying the IBAN updates the trust value
     */
    @Test
    public final void testVerifyIBAN() {
        blockController.verifyIBAN(_block);
        assertEquals(TrustValues.VERIFIED.getValue(), _block.getTrustValue());
    }

    /**
     * successfulTransaction test
     * Tests whether a successful transaction updates the trust value
     */
    @Test
    public final void testSuccessfulTransaction() {
        blockController.successfulTransaction(_block);
        assertEquals(TrustValues.INITIALIZED.getValue() + TrustValues.SUCCESFUL_TRANSACTION.getValue(),
                _block.getTrustValue());
    }

    /**
     * failedTransaction test
     * Tests whether a successful transaction updates the trust value
     */
    @Test
    public final void testFailedTransaction() {
        blockController.failedTransaction(_block);
        assertEquals(TrustValues.INITIALIZED.getValue() + TrustValues.FAILED_TRANSACTION.getValue(),
                _block.getTrustValue());
    }

    /**
     * revokedTrustValue test
     * Tests whether a revoked transaction updates the trust value
     */
    @Test
    public final void testRevokedTrustValue() {
        blockController.revokedTrustValue(_block);
        assertEquals(TrustValues.REVOKED.getValue(), _block.getTrustValue());
    }

    /** Check that the genesis block is created */
    @Test
    public final void verifyGenesisCreation() {
        assertNotNull(genesisA);
    }

    /** Check that genesis has the correct index */
    @Test
    public final void verifyGenesisIndex() {
        assertEquals(1, genesisA.getSequenceNumber());
    }

    /** Verify genesis owner A */
    @Test
    public final void verifyGenesisOwner() {
        assertEquals("A", genesisA.getOwner());
    }

    /** Hash of genesis block */
    @Test
    public final void verifyGenesisHashA() {
        ConversionController conversionController = new ConversionController("A", "PUBLIC_KEY", "N/A",
                "N/A", "ibanA");
        try {
            String hash = conversionController.hashKey();
            assertEquals(genesisA.getOwnHash(), hash);
        } catch (Exception e) {
            assertNotNull(e.getMessage(), null);
        }
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
    public final void verifyBlockChainHash() {
        assertEquals("N/A", blockWithOwnerAAddsKeyKa.getPreviousHashSender());
    }

    /** Verify block hash */
    @Test
    public final void verifySenderHash() throws Exception {
        ConversionController conversionController = new ConversionController("A", "kb", blockWithOwnerAAddsKeyKa.
                getOwnHash(), genesisB.getOwnHash(), "ibanB");
        String hash = conversionController.hashKey();
        assertEquals(hash, blockWithOwnerAAddsKeyKb.getOwnHash());
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
        final Block newBlock = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                blockController.getLatestSeqNumber(owner) + 1,
                ownHash,
                previousHashChain,
                NA,
                publicKey,
                iban,
                trustValue
        );
        final Block newBlock2 = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                blockController.getLatestSeqNumber(owner) + 1,
                previousHashSender,
                previousHashChain,
                ownHash,
                publicKey,
                iban,
                trustValue
        );
        blockController.addBlock(newBlock);
        assertEquals(newBlock, blockController.backtrack(newBlock2));
    }

    /**
     * verifyTrustworthiness test
     * Tests whether a block is trustworthy
     */
    @Test
    public void testVerifyTrustworthiness() throws HashException {
        final Block newBlock = BlockFactory.getBlock(TYPE_BLOCK, owner, blockController.getLatestSeqNumber(owner)+1,
                ownHash, previousHashChain, NA, publicKey, iban, trustValue);
        blockController.addBlock(newBlock);
        assertTrue(blockController.verifyTrustworthiness(newBlock));
    }

    /**
     * verifyTrustworthiness test
     * Tests whether a block is trustworthy
     * Forces a false
     */
    @Test
    public void testVerifyTrustworthinessFalse() throws HashException {
        final Block newBlock = BlockFactory.getBlock(TYPE_BLOCK, owner, blockController.getLatestSeqNumber(owner)+1,
                ownHash, previousHashChain, NA, publicKey+"1", iban, trustValue);
        assertFalse(blockController.verifyTrustworthiness(newBlock));
    }

    /**
     * Closes database connection after test
     */
    @After
    public final void tearDown() {
        blockController.clearAllBlocks();
    }
}
