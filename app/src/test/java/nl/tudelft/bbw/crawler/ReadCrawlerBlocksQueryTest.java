package nl.tudelft.bbw.crawler;

import static junit.framework.Assert.assertFalse;

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
     * Check if the chain isn't empty after parsing the database
     */
    @Test
    public void testEmpty() {
        CrawledBlocksDatabase database = new CrawledBlocksDatabase(RuntimeEnvironment.application);
        ReadCrawlerBlocksQuery query = new ReadCrawlerBlocksQuery();
        database.read(query);
        assertFalse(query.getChain().isEmpty());
    }
}