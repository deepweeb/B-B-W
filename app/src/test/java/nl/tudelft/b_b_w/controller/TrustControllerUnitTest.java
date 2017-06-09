package nl.tudelft.b_b_w.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.User;
import nl.tudelft.b_b_w.model.block.Block;

import static org.junit.Assert.assertEquals;

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
    private TrustController trustController;
    private Block block;

    /**
     * initialize method
     * Initialized the TrustController, BlockController and a test block
     */
    @Before
    public void initialize() throws HashException {
        this.trustController = new TrustController();
        final String name = "name";
        final String iban = "iban";
        final User user = new User(name, iban);
        this.block = new BlockController(RuntimeEnvironment.application).createGenesis(user);
    }

    /**
     * Tests whether a successful transaction updates the trust value
     */
    @Test
    public void testSuccesfulTransaction() {
        block = trustController.succesfulTransaction(block);
        assertEquals(14.389351794935735, block.getTrustValue(), DELTA);
    }

    /**
     * Tests whether a failed transaction updates the trust value
     */
    @Test
    public void testFailedTransaction() {
        block = trustController.failedTransaction(block);
        assertEquals(5.38560132615784, block.getTrustValue(), DELTA);
    }

    /**
     * Tests whether verifying the iban updates the trust value
     */
    @Test
    public void testVerifiedIBAN() {
        block = trustController.verifiedIBAN(block);
        assertEquals(22.536282121744804, block.getTrustValue(), DELTA);
    }

    /**
     * Tests whether the trust value does not go under the limit
     */
    @Test
    public void testUnderCeiling() {
        block.setTrustValue(0);
        block = trustController.failedTransaction(block);
        assertEquals(0, block.getTrustValue(), DELTA);
    }

}