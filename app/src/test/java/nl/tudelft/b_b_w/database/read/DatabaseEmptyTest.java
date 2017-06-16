package nl.tudelft.b_b_w.database.read;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.database.Database;
import nl.tudelft.b_b_w.database.write.BlockAddQuery;
import nl.tudelft.b_b_w.database.write.UserAddQuery;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class DatabaseEmptyTest {
    Database database;

    @Before
    public void init() {
        database = new Database(RuntimeEnvironment.application);
    }

    @Test
    public void testEmpty() {
        DatabaseEmptyQuery query = new DatabaseEmptyQuery();
        database.read(query);
        assertTrue(query.isDatabaseEmpty());
    }

    @Test
    public void testNonEmpty() {
        User alice = new User("Alice", "ibanA", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        database.write(new UserAddQuery(alice));
        database.write(new BlockAddQuery(new Block(alice)));
        DatabaseEmptyQuery query = new DatabaseEmptyQuery();
        database.read(query);
        assertFalse(query.isDatabaseEmpty());
    }
}