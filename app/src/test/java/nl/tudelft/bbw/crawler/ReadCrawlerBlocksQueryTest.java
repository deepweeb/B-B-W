package nl.tudelft.bbw.crawler;

import static junit.framework.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.bbw.BuildConfig;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class ReadCrawlerBlocksQueryTest {

    /**
     * The example database
     */
    private BlockDatabase database;

    /**
     * Initialise database empty test
     */
    @Before
    public void init() {
        database = new BlockDatabase(RuntimeEnvironment.application);
    }

    @Test
    public void testEmpty() {
        ReadCrawlerBlocksQuery query = new ReadCrawlerBlocksQuery();
        database.read(query);
        assertFalse(query.getChain().isEmpty());
    }
}