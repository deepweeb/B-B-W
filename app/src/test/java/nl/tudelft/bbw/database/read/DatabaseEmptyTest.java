package nl.tudelft.bbw.database.read;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.bbw.BuildConfig;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.ED25519;
import nl.tudelft.bbw.database.Database;
import nl.tudelft.bbw.database.write.BlockAddQuery;
import nl.tudelft.bbw.database.write.UserAddQuery;

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
