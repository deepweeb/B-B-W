package nl.tudelft.b_b_w.controller;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.blockchain.User;

/**
 * Unit test for BlockVerificationController class
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class BlockVerificationControllerTest {

    /**
     * Attribute which is used more than once
     */
    private BlockVerificationController blockVerificationController;

    /**
     * Initialising the BlockVerificationController before every test
     *
     * @throws Exception RuntimeException
     */
    @Before
    public final void setUp() throws Exception {
        blockVerificationController = new BlockVerificationController(
                RuntimeEnvironment.application);
    }

    /**
     * Test to check whether the database is indeed empty
     */
    @Test
    public final void isDatabaseEmptyTest() {
        assertEquals(blockVerificationController.isDatabaseEmpty(), true);
    }

    /**
     * Test to check whether is block is indeed the same and thus is legit
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void verifyTrustworthinessTest() throws Exception {
        User bob = new User("Bob", "ibanBob", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        BlockController blockController = new BlockController(bob, RuntimeEnvironment.application);
        assertEquals(true,
                blockVerificationController.verifyTrustworthiness(
                        blockController.createGenesis(bob)));
    }
}
