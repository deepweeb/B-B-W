package nl.tudelft.b_b_w.database.read;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.controller.KeyWriter;
import nl.tudelft.b_b_w.database.Database;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class GetUserQueryTest {
    private User alice;
    private Database database;
    private EdDSAPublicKey aliceKey;

    @Before
    public void init() {
        database = new Database(RuntimeEnvironment.application);
        aliceKey = ED25519.getPublicKey(ED25519.generatePrivateKey());
        alice = new User("Alice", "IBANA", aliceKey);
    }

    @Test
    public void testNonexistentUser() {
        GetUserQuery query = new GetUserQuery(alice.getPublicKey());
        database.read(query);
        assertNull(query.getUser());
    }

    @Test
    public void testExistentUser() {
        GetUserQuery query = new GetUserQuery(aliceKey);
        database.read(query);
        User user = query.getUser();

        // multiple asserts because user does not have an equals method
        assertEquals(alice.getName(), user.getName());
        assertEquals(alice.getIban(), user.getIban());
        assertEquals(KeyWriter.publicKeyToString(alice.getPublicKey()),
                KeyWriter.publicKeyToString(user.getPublicKey()));
    }
}
