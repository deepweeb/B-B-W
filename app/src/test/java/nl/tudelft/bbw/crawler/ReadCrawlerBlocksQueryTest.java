package nl.tudelft.bbw.crawler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;

import nl.tudelft.bbw.BuildConfig;

import static junit.framework.Assert.assertFalse;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class ReadCrawlerBlocksQueryTest {

    /**
     * Check if the chain isn't empty after parsing the database
     */
    @Test
    public void testEmpty() throws IOException {
        CrawledBlocksDatabase database = new CrawledBlocksDatabase(RuntimeEnvironment.application);
        ReadCrawlerBlocksQuery query = new ReadCrawlerBlocksQuery();
        database.read(query);
        query.getMultiChain();
        assertFalse(query.getMultiChain().isEmpty());
    }
}