package nl.tudelft.b_b_w.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.BlockFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Test the API.
 * TODO revoke does not work yet, Luat is working on it. Also extra unittests.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk= 21,  manifest = "src/main/AndroidManifest.xml")
public class ApiUnitTest {
    private BlockController bc;
    private Api api;
    final private String userA = "Antro";
    final private String userB = "Besse";
    final private String userC = "Cacao";
    final private String userD = "Draffyg";
    final private String userE = "Erwti";

    /**
     * User A has one public key, user B two
     * The blockchain of each user:
     * A: add pkA
     * B: add pkA
     * C: empty
     * D: add pkA, revoke pkA
     * E: add pkE, revoke pkE
     * F: add pkF1, add pkF2
     * G: add pkF1, add pkF2, add pkG
     * All is tested from viewpoint of A.
     */
    @Before
    public void setUp() {
        bc = new BlockController(RuntimeEnvironment.application);
        api = new Api(RuntimeEnvironment.application);

        // A: add A
        bc.addBlock(BlockFactory.getBlock("BLOCK", "Antro", "roothashA", "prevhashchain", "root", "pkroot", "NL81...", 0));
        bc.addBlock(BlockFactory.getBlock("BLOCK", "Antro", "hashA", "roothash", "N/A", "pka", "NL81...", 0));

        // B: add A
        bc.addBlock(BlockFactory.getBlock("BLOCK", "Besse", "roothashB", "prevhashchain", "root", "pkroot", "NL81...", 0));
        bc.addBlock(BlockFactory.getBlock("BLOCK", "Besse", "hashB", "roothashB", "roothashA", "pka", "NL81...", 0));

        // C
        bc.addBlock(BlockFactory.getBlock("BLOCK", "Cacao", "roothashC", "prevhashchain", "root", "pkroot", "NL81...", 0));

        // E: add A, revoke A
        bc.addBlock(BlockFactory.getBlock("BLOCK", "Erwti", "roothashE", "prevhashchain", "root", "pkroot", "NL81...", 0));
        bc.addBlock(BlockFactory.getBlock("BLOCK", "Erwti", "hashE1", "roothashE", "roothashA", "pka", "NL81...", 0));
        bc.addBlock(BlockFactory.getBlock("REVOKE", "Erwti", "hashE2", "hashE1", "roothashA", "pka", "NL81...", 0));
    }


    /**
     * Add one own key
     */
    @Test
    public void ownKey() {
        List<String> keysA = api.getUserKeys(userA, userA);
        List<String> expectedA = new ArrayList<String>();
        expectedA.add("pka");
        assertEquals(expectedA, keysA);
    }

    /**
     * Add key of another user
     */
    @Test
    public void otherKey() {
        // is the other key there
        List<String> keysA = api.getUserKeys(userB, userA);
        List<String> expectedA = new ArrayList<String>();
        expectedA.add("pka");
        assertEquals(expectedA, keysA);

        // does it really filter
        List<String> keysB = api.getUserKeys(userB, userB);
        assertTrue(keysB.isEmpty());
    }

    /**
     * User without keys
     */
    @Test
    public void userWithoutKeys() {
        List<String> keys = api.getUserKeys(userC, userA);
        assertTrue(keys.isEmpty());
    }

    ///////////////////////////////////////////////////////////// WRITE UNIT TESTS

    /**
     * Add key to empty user C.
     */
    @Test
    public void addOwnKey() {
        try {
            api.addKey(userC, userC, "pkc", "ibanc");
            List<String> keys = api.getUserKeys(userC, userC);
            List<String> expected = new ArrayList<String>();
            expected.add("pkc");
            assertEquals(expected, keys);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    /**
     * Add key to of A to C.
     */
    @Test
    public void addOtherKey() {
        try {
            api.addKey(userC, userA, "pka", "ibana");
            List<String> keys = api.getUserKeys(userC, userA);
            List<String> expected = new ArrayList<String>();
            expected.add("pka");
            assertEquals(expected, keys);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

}
