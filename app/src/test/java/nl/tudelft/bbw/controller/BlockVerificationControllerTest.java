package nl.tudelft.bbw.controller;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.bbw.BuildConfig;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

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
     */
    @Before
    public final void setUp() {
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
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void verifyTrustworthinessTest() throws HashException,
            BlockAlreadyExistsException {
        User bob = new User("Bob", "ibanBob", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        BlockController blockController = new BlockController(bob, RuntimeEnvironment.application);
        assertEquals(true,
                blockVerificationController.verifyTrustworthiness(
                        blockController.createGenesis(bob)));
    }
}
