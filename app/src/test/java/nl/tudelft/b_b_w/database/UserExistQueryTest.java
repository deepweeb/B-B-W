package nl.tudelft.b_b_w.database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.database.read.UserExistQuery;
import nl.tudelft.b_b_w.database.write.UserAddQuery;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for the UserExistQuery class
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class UserExistQueryTest {
    /**
     * Example users to test with
     */
    private User alice;
    private User bob;
    private User carol;

    /**
     * Test database
     */
    private Database database;

    /**
     * Create example users
     */
    @Before
    public void init() {
        alice = new User("Alice", "ibanA", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        bob = new User("Bob", "ibanB", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        carol = new User("Carol", "ibanC", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        database = new Database(RuntimeEnvironment.application);
    }

    /**
     * In the empty database should not be any user
     */
    @Test
    public void testEmptyDatabase() {
        UserExistQuery adamQuery = new UserExistQuery(alice);
        database.read(adamQuery);
        assertFalse(adamQuery.doesExist());
    }

    /**
     * Add two users, check if a third exists (spoiler: it does not)
     */
    @Test
    public void testNonexistentUser() {
        // add user
        database.write(new UserAddQuery(alice));
        database.write(new UserAddQuery(bob));

        // check other user
        UserExistQuery exist = new UserExistQuery(carol);
        database.read(exist);
        assertFalse(exist.doesExist());
    }

    /**
     * Add one user, check if this same user exists
     */
    @Test
    public void testExistentUser() {
        // add user
        UserAddQuery add = new UserAddQuery(alice);
        database.write(add);

        // check other user
        UserExistQuery exist = new UserExistQuery(alice);
        database.read(exist);
        assertTrue(exist.doesExist());
    }

    /**
     * Add two users, both should exist
     */
    @Test
    public void testTwoUsers() {
        // add users
        database.write(new UserAddQuery(alice));
        database.write(new UserAddQuery(bob));

        // check other user
        UserExistQuery existA = new UserExistQuery(alice);
        UserExistQuery existB = new UserExistQuery(bob);
        database.read(existA);
        database.read(existB);
        assertTrue(existA.doesExist());
        assertTrue(existB.doesExist());
    }
}
