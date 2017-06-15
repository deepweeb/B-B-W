package nl.tudelft.b_b_w.database.read;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.database.Database;
import nl.tudelft.b_b_w.database.read.GetUserQuery;

import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class GetUserQueryTest {
    private User alice;
    private Database database;

    @Before
    public void init() {
        database = new Database(RuntimeEnvironment.application);
        alice = new User("Alice", "IBANA", ED25519.getPublicKey(ED25519.generatePrivateKey()));
    }

    @Test
    public void testNonexistentUser() {
        GetUserQuery query = new GetUserQuery(alice.getPublicKey());
        database.read(query);
        assertNull(query.getUser());
    }
}
