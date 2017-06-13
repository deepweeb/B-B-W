package nl.tudelft.b_b_w.model;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.block.BlockData;
import nl.tudelft.b_b_w.model.block.BlockType;
import nl.tudelft.b_b_w.model.block.GenesisBlock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * This class verifies the workings of the GenesisBlock
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class GenesisBlockUnitTest {
    private final User userA = new User("Alice", "IBANA");
    private final User userB = new User("Bob", "IBANB");
    private final String publicKeyA = "pka";
    private final String publicKeyB = "pkb";
    private final String notAvailable = "N/A";
    private final String hashGenesisA =
            "7875f4b585ea90f0db3f3fd3efd257a9104147a443877efac3389afe8f26390d";

    /**
     * Example genesis block of user A
     */
    private GenesisBlock genesisBlockA;

    /**
     * Copy of genesis block of user A
     */
    private GenesisBlock genesisBlockA2;

    /**
     * Example genesis block of user B
     */
    private GenesisBlock genesisBlockB;

    /**
     * Create example block data for block A and block B
     *
     * @throws HashException
     */
    @Before
    public void create() throws HashException {
        // alice block data
        BlockData blockDataA = new BlockData();
        blockDataA.setBlockType(BlockType.GENESIS);
        blockDataA.setSequenceNumber(1);
        blockDataA.setTrustValue(TrustValues.INITIALIZED.getValue());
        blockDataA.setOwner(userA);
        blockDataA.setIban(userA);
        blockDataA.setPreviousHashSender(notAvailable);
        blockDataA.setPreviousHashChain(notAvailable);
        blockDataA.setPublicKey(publicKeyA);
        genesisBlockA = new GenesisBlock(blockDataA);
        genesisBlockA2 = new GenesisBlock(blockDataA);

        // bob block data
        BlockData blockDataB = new BlockData();
        blockDataB.setBlockType(BlockType.GENESIS);
        blockDataB.setSequenceNumber(1);
        blockDataB.setTrustValue(TrustValues.INITIALIZED.getValue());
        blockDataB.setOwner(userB);
        blockDataB.setIban(userB);
        blockDataB.setPreviousHashSender(notAvailable);
        blockDataB.setPreviousHashChain(notAvailable);
        blockDataB.setPublicKey(publicKeyB);
        genesisBlockB = new GenesisBlock(blockDataB);
    }

    /**
     * Test the hash function
     */
    @Test
    public void testHash() {
        assertEquals(hashGenesisA, genesisBlockA.getOwnHash().toLowerCase());
    }

    /**
     * Test previous hash sender not available A
     */
    @Test
    public void testOwnerA() {
        assertEquals(userA, genesisBlockA.getOwner());
    }

    /**
     * Test previous hash sender not available B
     */
    @Test
    public void testOwnerB() {
        assertEquals(userB, genesisBlockB.getOwner());
    }

    /**
     * Test previous hash sender not available
     */
    @Test
    public void testPreviousHashSender() {
        assertEquals(notAvailable, genesisBlockA.getPreviousHashSender());
    }

    /**
     * Test previous hash chain not available
     */
    @Test
    public void testPreviousHashChain() {
        assertEquals(notAvailable, genesisBlockA.getPreviousHashChain());
    }

    /**
     * Test iban
     */
    @Test
    public void testIbanGenesis() {
        assertEquals(genesisBlockA.getOwner().getIban(), userA.getIban());
    }

    /**
     * Test public key A
     */
    @Test
    public void testPublicKeyA() {
        assertEquals(genesisBlockA.getPublicKey(), publicKeyA);
    }

    /**
     * Test public key B
     */
    @Test
    public void testPublicKeyB() {
        assertEquals(genesisBlockB.getPublicKey(), publicKeyB);
    }

    /**
     * Test trust value
     */
    @Test
    public void testTrustGenesis() {
        assertEquals(TrustValues.INITIALIZED.getValue(), genesisBlockA.getTrustValue(), 1E-15);
    }

    /**
     * Test if non-revoked
     */
    @Test
    public void testNonrevoked() {
        assertFalse(genesisBlockA.isRevoked());
    }

    /**
     * Test equality
     */
    @Test
    public void testEqualsGenesis() {
        assertTrue(genesisBlockA.equals(genesisBlockA2));
    }

    /**
     * Test inequality
     */
    @Test
    public void testInequals() {
        assertFalse(genesisBlockA.equals(genesisBlockB));
    }

    /**
     * Test verify block
     */
    @Test
    public void testVerify() {
        assertTrue(genesisBlockA.verifyBlock(genesisBlockA2));
    }

    /**
     * Test not verify block
     */
    @Test
    public void testUnverify() {
        assertFalse(genesisBlockA.verifyBlock(genesisBlockB));
    }
}
