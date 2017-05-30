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
import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;
import nl.tudelft.b_b_w.model.TrustValues;

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
    private BlockController bc;
    private final String owner = "owner";
    private final int sequenceNumber = 1;
    private final String ownHash = "ownHash";
    private final String previousHashChain = "previousHashChain";
    private final String previousHashSender = "previousHashSender";
    private final String iban = "iban";
    private final String publicKey = "publicKey";
    private Block _block;
    private final int trustValue = 0;
    private final String TYPE_BLOCK = "BLOCK";
    private final String TYPE_REVOKE = "REVOKE";

    private Block genesisA;
    private Block genesisB;
    private Block blockA_A1;
    private Block blockA_B1;
    private Block blockB_A1;
    private Block blockB_rA1;

    /**
     * Initialize BlockController before every test
     * And initialize a dummy block _block
     */
    @Before
    public void setUp() {
        this.bc = new BlockController(RuntimeEnvironment.application);
        this._block = BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        _block.setSeqNumberTo(sequenceNumber);

        try {
            genesisA = bc.createGenesis("A");
            genesisB = bc.createGenesis("B");
            blockA_A1 = bc.createKeyBlock("A", "A", "ka", "ibanA");
            blockA_B1 = bc.createKeyBlock("A", "B", "kb", "ibanB");
            blockB_A1 = bc.createKeyBlock("B", "A", "ka", "ibanA");
            blockB_rA1 = bc.createRevokeBlock("B", "A", "ka", "ibanA");
        } catch (Exception e) {
            // we will check this in later tests
        }
    }

    /**
     * Tests adding a block
     *
     * @throws Exception RuntimeException
     */
    @Test
    public void testAddBlock() throws Exception {
        bc.addBlock(_block);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        assertEquals(bc.getBlocks(owner), list);
    }


    /**
     * getOwnerName test no 1
     * Test getting the owner name given hash key.
     */
    @Test
    public void getContactNameTest1() {

        Block block2 = BlockFactory.getBlock(TYPE_BLOCK, owner, "ownHash2",
                ownHash, "Hash44324", publicKey + "2", iban, trustValue);
        block2.setSeqNumberTo(1);
        bc.addBlock(_block);
        bc.addBlock(block2);
        assertEquals(owner, bc.getContactName(ownHash));
    }

    /**
     * getOwnerName test no 2
     * Test getting the owner name given hash key.
     */
    @Test
    public void getContactNameTest2() {

        Block block2 = BlockFactory.getBlock(TYPE_BLOCK, owner, "ownHash2",
                ownHash, "Hash44324", publicKey + "2", iban, trustValue);
        block2.setSeqNumberTo(1);
        bc.addBlock(_block);
        bc.addBlock(block2);
        assertEquals(owner + "'s friend #" + block2.getSequenceNumber(), bc.getContactName("ownHash2"));
    }


    /**
     * Tests to return the latest block
     *
     * @throws Exception RuntimeException
     */
    @Test
    public void testGetLatestBlock() throws Exception {
        Block expected = BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        expected.setSeqNumberTo(1);
        bc.addBlock(_block);
        assertEquals(expected, bc.getLatestBlock(owner));
    }

    /**
     * Tests returning the latest sequence number of chain
     *
     * @throws Exception RuntimeException
     */
    @Test
    public void testGetLatestSeqNumber() throws Exception {
        final String newOwner = owner + "2";
        final Block newBlock = BlockFactory.getBlock(TYPE_BLOCK, newOwner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        bc.addBlock(_block);
        bc.addBlock(newBlock);


        assertEquals(1, bc.getLatestSeqNumber(owner));
    }


    /**
     * Tests adding two blocks
     *
     * @throws Exception RuntimeException
     */
    @Test
    public void testAddBlock2() throws Exception {
        final String newOwner = owner + "2";
        Block newBlock = BlockFactory.getBlock(TYPE_BLOCK, newOwner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        newBlock.setSeqNumberTo(1);
        bc.addBlock(_block);
        bc.addBlock(newBlock);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        list.add(newBlock);

        List<Block> newList = bc.getBlocks(owner);
        newList.addAll(bc.getBlocks(newOwner));

        assertEquals(list, newList);
    }

    /**
     * Tests adding a duplicate block
     */
    @Test(expected = RuntimeException.class)
    public void testAddDupBlocks() {
        bc.addBlock(_block);
        bc.addBlock(_block);
    }

    /**
     * Tests adding an already revoked block
     */
    @Test(expected = RuntimeException.class)
    public void alreadyRevoked() {
        final Block newBlock = BlockFactory.getBlock(TYPE_REVOKE, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);
        bc.addBlockToChain(newBlock);
        bc.addBlockToChain(_block);
    }

    /**
     * Tests filtering duplicates out of a list
     */
    @Test
    public void testEmptyList() {
        bc.addBlock(_block);
        bc.revokeBlock(_block);
        List<Block> list = new ArrayList<>();
        assertEquals(list, bc.getBlocks(owner));
    }

    /**
     * Test removeBlock if the specified revoked block has no match
     */
    @Test
    public void testRemoveWithNoMatch() throws Resources.NotFoundException {
        bc.addBlock(_block);
        final Block blc2 = BlockFactory.getBlock(TYPE_BLOCK, owner + "2", ownHash,
                previousHashChain, previousHashSender, publicKey + "2", iban, trustValue);
        bc.revokeBlock(blc2);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        assertEquals(list, bc.getBlocks(owner));
    }

    /**
     * verifyIBAN test
     * Tests whether verifying the IBAN updates the trust value
     */
    @Test
    public void testVerifyIBAN() {
        bc.verifyIBAN(_block);
        assertEquals(TrustValues.VERIFIED.getValue(), _block.getTrustValue());
    }

    /**
     * successfulTransaction test
     * Tests whether a successful transaction updates the trust value
     */
    @Test
    public void testSuccessfulTransaction() {
        bc.successfulTransaction(_block);
        assertEquals(TrustValues.SUCCESFUL_TRANSACTION.getValue(), _block.getTrustValue());
    }

    /**
     * failedTransaction test
     * Tests whether a successful transaction updates the trust value
     */
    @Test
    public void testFailedTransaction() {
        bc.failedTransaction(_block);
        assertEquals(TrustValues.FAILED_TRANSACTION.getValue(), _block.getTrustValue());
    }

    /**
     * revokedTrustValue test
     * Tests whether a revoked transaction updates the trust value
     */
    @Test
    public void testRevokedTrustValue() {
        bc.revokedTrustValue(_block);
        assertEquals(TrustValues.REVOKED.getValue(), _block.getTrustValue());
    }

    /** Check that the genesis block is created */
    @Test
    public void verifyGenesisCreation() {
        assertNotNull(genesisA);
    }

    /** Check that genesis has the correct index */
    @Test
    public void verifyGenesisIndex() {
        assertEquals(1, genesisA.getSequenceNumber());
    }

    /** Verify genesis owner A */
    @Test
    public void verifyGenesisOwner() {
        assertEquals("A", genesisA.getOwner());
    }

    /** Hash of genesis block */
    @Test
    public void verifyGenesisHashA() {
        ConversionController conversionController = new ConversionController("A", "N/A", "N/A", "N/A", "N/A");
        try {
            String hash = conversionController.hashKey();
            assertEquals(genesisA.getOwnHash(), hash);
        } catch (Exception e) {
            assertNotNull(e.getMessage(), null);
        }
    }

    /** Verify the block A-A1 was created */
    @Test
    public void verifyBlockA_A1Creation() {
        assertNotNull(blockA_A1);
    }

    /** Verify the block A-B1 was created */
    @Test
    public void verifyBlockA_B1Creation() {
        assertNotNull(blockA_B1);
    }

    /** Blocks without sender should not have a sender hash */
    @Test
    public void verifyBlockChainHash() {
        assertEquals("N/A", blockA_A1.getPreviousHashSender());
    }

    /** Verify block hash */
    @Test
    public void verifySenderHash() throws Exception {
        ConversionController conversionController = new ConversionController("A", "kb", blockA_A1.getOwnHash(), genesisB.getOwnHash(), "ibanB");
        String hash = conversionController.hashKey();
        assertEquals(hash, blockA_B1.getOwnHash());
    }

    /** Is the revoked block indeed revoked? */
    @Test
    public void verifyRevoked() {
        assertTrue(blockB_rA1.isRevoked());
    }

    /**
     * Closes database connection after test
     */
    @After
    public void tearDown() {
        bc.clearAllBlocks();
    }
}
