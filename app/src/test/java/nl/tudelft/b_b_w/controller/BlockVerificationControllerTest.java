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
import nl.tudelft.b_b_w.database.Database;
import nl.tudelft.b_b_w.database.write.UserAddQuery;
import nl.tudelft.b_b_w.model.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.model.HashException;

/**
 * Created by Ashay on 15/06/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class BlockVerificationControllerTest {

    private BlockVerificationController blockVerificationController;

    @Before
    public final void setUp() throws HashException, BlockAlreadyExistsException {
        blockVerificationController = new BlockVerificationController(
                RuntimeEnvironment.application);
    }

    @Test
    public final void isDatabaseEmptyTest() {
        assertEquals(blockVerificationController.isDatabaseEmpty(), true);
    }

    @Test
    public final void verifyTrustworthinessTest() throws Exception {
        User bob = new User("Bob", "ibanBob", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        UserAddQuery query = new UserAddQuery(bob);
        Database database = new Database(RuntimeEnvironment.application);
        database.write(query);
        BlockController blockController = new BlockController(bob, RuntimeEnvironment.application);
        assertEquals(true,
                blockVerificationController.verifyTrustworthiness(
                        blockController.createGenesis(bob)));
    }
}
