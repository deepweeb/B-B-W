package nl.tudelft.b_b_w.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;
import nl.tudelft.b_b_w.model.User;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Test the API
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk= 21,  manifest = "src/main/AndroidManifest.xml")
public class ApiUnitTest {
    private BlockController bc;
    private User userA, userB, userC, userD;

    public List<Block> addKeys(User owner, User user) {
        List<Block> res = new ArrayList<>();
        for (String key : user.getPublicKeys())
            res.add(BlockFactory.getBlock(owner, user, "prev", "send", key, false));
        return res;
    }

    /**
     * User A has one public key, user B two
     * The blockchain of each user:
     * A:   adds key pka, adds key pkb
     * B:   adds key pkb, revokes key pkb
     * C:   empty
     * D:   adds key pkd1 and pkd2 and pkd3
     * All is tested from viewpoint of A.
     */
    @Before
    public void setUp() {
        bc = new BlockController(RuntimeEnvironment.application);
        Api.init(RuntimeEnvironment.application);

        userA = Api.createUser("Antro", "NL81INGB0000000000");
        userB = Api.createUser("Besse", "NL81INGB0000000000");
        userC = Api.createUser("Cacao", "NL81INGB0000000000");
        userD = Api.createUser("Draffyg", "NL81INGB0000000000");
        userA.addPublicKey("pka");
        userB.addPublicKey("pkb");
        userD.addPublicKey("pkd1");
        userD.addPublicKey("pkd2");
        userD.addPublicKey("pkd3");

        // user A has
        bc.addBlock(BlockFactory.getBlock(userA, userA, "prev", "send", "pka", false));

        // user B
        bc.addBlock(BlockFactory.getBlock(userA, userB, "prev", "send", "pkb", false));
        bc.addBlock(BlockFactory.getBlock(userA, userB, "prev", "send", "pkb", true));

        // user C is empty

        // user D
        bc.addBlock(BlockFactory.getBlock(userA, userD, "prev", "send", "pkd1", false));
        bc.addBlock(BlockFactory.getBlock(userA, userD, "prev", "send", "pkd2", false));
        bc.addBlock(BlockFactory.getBlock(userA, userD, "prev", "send", "pkd3", false));

    }


    /**
     * Simple API test to obtain a list of keys from user A
     */
    @Test
    public void simpleUser() {
        List<String> keysA = Api.getUserKeys(userA, userA);
        List<String> expectedA = new ArrayList<String>();
        expectedA.add("pka");
        assertEquals(expectedA, keysA);
    }

    /**
     * User with one key that is revoked
     */
    @Test
    public void revokedKey() {
        List<String> keys = Api.getUserKeys(userA, userB);
        assertTrue(keys.isEmpty());
    }

    /**
     * User without keys
     */
    @Test
    public void userWithoutKeys() {
        List<String> keys = Api.getUserKeys(userA, userC);
        assertTrue(keys.isEmpty());
    }

    /**
     * User with multiple keys
     */
    @Test
    public void userMultipleKeys() {
        List<String> keys = Api.getUserKeys(userA, userD);
        Collections.sort(keys);
        List<String> expected = new ArrayList<>();
        expected.add("pkd1");
        expected.add("pkd2");
        expected.add("pkd3");
        assertEquals(expected, keys);
    }
}

