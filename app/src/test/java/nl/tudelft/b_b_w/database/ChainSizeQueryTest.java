package nl.tudelft.b_b_w.database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.model.User;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for the ChainSizeQuery
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class ChainSizeQueryTest {
    @Before
    public void init() {

    }

    /**
     * Check if empty chain returns zero
     */
    @Test
    public void testEmptyChainSize() {
        User adam = new User("Adam", "iban");
        ChainSizeQuery query = new ChainSizeQuery(adam);
        Database database = new Database(RuntimeEnvironment.application);
        database.read(query);
        int chainsize = query.getSize();
        assertEquals(0, chainsize);
    }

}
