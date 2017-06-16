package nl.tudelft.bbw.controller;

import static org.junit.Assert.assertEquals;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

<<<<<<< HEAD:app/src/test/java/nl/tudelft/b_b_w/controller/TrustControllerUnitTest.java
import nl.tudelft.bbw.BuildConfig;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
=======

import static org.junit.Assert.assertEquals;
>>>>>>> 1e99e1f5d99a7f027cc09e71ecf4ab4aa3b94723:app/src/test/java/nl/tudelft/bbw/controller/TrustControllerUnitTest.java

/**
 * Test class for CryptoController
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class TrustControllerUnitTest {

    /**
     * Class attributes
     */
    private static final double DELTA = 1e-15;
    private Block block;

    /**
     * initialize method
     * Initialized the TrustController, BlockController and a test block
     */
    @Before
    public void initialize() throws HashException, BlockAlreadyExistsException {
        final String name = "name";
        final String iban = "iban";
        EdDSAPrivateKey edDSAPrivateKey1 =
                ED25519.generatePrivateKey(Utils.hexToBytes(
                        "0000000000000000000000000000000000000000000000000000000000000000"));
        EdDSAPublicKey ownerPublicKey = ED25519.getPublicKey(edDSAPrivateKey1);
        final User user = new User(name, iban, ownerPublicKey);
        this.block = new BlockController(user, RuntimeEnvironment.application).createGenesis(user);
    }

    /**
     * Tests whether a successful transaction updates the trust value
     */
    @Test
    public void testSuccessfulTransaction() {
        block = TrustController.successfulTransaction(block);
        assertEquals(14.389351794935735, block.getTrustValue(), DELTA);
    }

    /**
     * Tests whether a failed transaction updates the trust value
     */
    @Test
    public void testFailedTransaction() {
        block = TrustController.failedTransaction(block);
        assertEquals(5.38560132615784, block.getTrustValue(), DELTA);
    }

    /**
     * Tests whether verifying the iban updates the trust value
     */
    @Test
    public void testVerifiedIBAN() {
        block = TrustController.verifiedIBAN(block);
        assertEquals(22.536282121744804, block.getTrustValue(), DELTA);
    }

    /**
     * Tests whether the trust value does not go under the limit
     */
    @Test
    public void testUnderCeiling() {
        block.setTrustValue(0);
        block = TrustController.failedTransaction(block);
        assertEquals(0, block.getTrustValue(), DELTA);
    }

    /**
     * Test to check whether revoking a block works
     */
    @Test
    public void testRevokeBlock() {
        block = TrustController.revokeBlock(block);
        assertEquals(0, block.getTrustValue(), DELTA);
    }

}