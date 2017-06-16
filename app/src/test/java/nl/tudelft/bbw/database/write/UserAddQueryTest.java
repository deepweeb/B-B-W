package nl.tudelft.bbw.database.write;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.bbw.BuildConfig;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.ED25519;
import nl.tudelft.bbw.database.Database;
import nl.tudelft.bbw.database.read.GetUserQuery;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for the ChainSizeQuery
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class UserAddQueryTest {
    /**
     * Example user
     */
    User alice;

    /**
     * Example database
     */
    Database database;

    /**
     * Initialize the user add query
     */
    @Before
    public void init() {
        alice = new User("Alice", "IBANA", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        database = new Database(RuntimeEnvironment.application);
    }

    /**
     * Add one user
     */
    @Test
    public void testSimpleAdd() throws Exception {
        database.write(new UserAddQuery(alice));

        // verify
        GetUserQuery query = new GetUserQuery(alice.getPublicKey());
        database.read(query);
        assertEquals(alice, query.getUser());
    }
}
