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

/** This class verifies the workings of the GenesisBlock */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk= 21,  manifest = "src/main/AndroidManifest.xml")
public class GenesisBlockUnitTest {
    private final User a = new User("Alice", "IBANA");
    private final User b = new User("Bob", "IBANB");
    private final String PKA = "pka";
    private final String PKB = "pkb";
    private final String NA = "N/A";
    private final String HASH_GENESIS_A =
            "7875f4b585ea90f0db3f3fd3efd257a9104147a443877efac3389afe8f26390d";

    /** Example genesis block of user A */
    private GenesisBlock genesisBlockA;
    /** Copy of genesis block of user A */
    private GenesisBlock genesisBlockA2;
    /** Example genesis block of user B */
    private GenesisBlock genesisBlockB;

    @Before
    public void create() throws HashException {
        // alice block data
        BlockData blockDataA = new BlockData();
        blockDataA.setBlockType(BlockType.GENESIS);
        blockDataA.setSequenceNumber(1);
        blockDataA.setTrustValue(TrustValues.INITIALIZED.getValue());
        blockDataA.setOwner(a);
        blockDataA.setIban(a);
        blockDataA.setPreviousHashSender(NA);
        blockDataA.setPreviousHashChain(NA);
        blockDataA.setPublicKey(PKA);
        genesisBlockA = new GenesisBlock(blockDataA);
        genesisBlockA2 = new GenesisBlock(blockDataA);

        // bob block data
        BlockData blockDataB = new BlockData();
        blockDataB.setBlockType(BlockType.GENESIS);
        blockDataB.setSequenceNumber(1);
        blockDataB.setTrustValue(TrustValues.INITIALIZED.getValue());
        blockDataB.setOwner(b);
        blockDataB.setIban(b);
        blockDataB.setPreviousHashSender(NA);
        blockDataB.setPreviousHashChain(NA);
        blockDataB.setPublicKey(PKB);
        genesisBlockB = new GenesisBlock(blockDataB);
    }

    /** Test hash */
    @Test
    public void hash() {
        assertEquals(HASH_GENESIS_A, genesisBlockA.getOwnHash().toLowerCase());
    }

    /** Test previous hash sender not available A */
    @Test
    public void ownerA() {
        assertEquals(a, genesisBlockA.getOwner());
    }

    /** Test previous hash sender not available B */
    @Test
    public void ownerB() {
        assertEquals(b, genesisBlockB.getOwner());
    }

    /** Test previous hash sender not available */
    @Test
    public void previousHashSender() {
        assertEquals(NA, genesisBlockA.getPreviousHashSender());
    }

    /** Test previous hash chain not available */
    @Test
    public void previousHashChain() {
        assertEquals(NA, genesisBlockA.getPreviousHashChain());
    }

    /** Test iban */
    @Test
    public void testIbanGenesis() {
        assertEquals(genesisBlockA.getOwner().getIban(), a.getIban());
    }


    /** Test public key A */
    @Test
    public void publicKeyA() {
        assertEquals(genesisBlockA.getPublicKey(), PKA);
    }

    /** Test public key B */
    @Test
    public void publicKeyB() {
        assertEquals(genesisBlockB.getPublicKey(), PKB);
    }

    /** Test trust value */
    @Test
    public void testTrustGenesis() {
        assertEquals(TrustValues.INITIALIZED.getValue(), genesisBlockA.getTrustValue());
    }

    /** Test if non-revoked */
    @Test
    public void nonrevoked() {
        assertFalse(genesisBlockA.isRevoked());
    }

    /** Test equality */
    @Test
    public void testEqualsGenesis() {
        assertTrue(genesisBlockA.equals(genesisBlockA2));
    }

    /** Test inequality */
    @Test
    public void inequals() {
        assertFalse(genesisBlockA.equals(genesisBlockB));
    }

    /** Test verify block */
    @Test
    public void verify() {
        assertTrue(genesisBlockA.verifyBlock(genesisBlockA2));
    }

    /** Test not verify block */
    @Test
    public void unverify() {
        assertFalse(genesisBlockA.verifyBlock(genesisBlockB));
    }
}
