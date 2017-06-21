package nl.tudelft.bbw.crawler;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * Test class for DatabaseReader
 */
public class DatabaseReaderUnitTest {

    /**
     * Test to check whether reading the crawled database file works
     */
    @Test
    public void testReadDatabase() throws IOException {
        assertNotNull(DatabaseReader.readDatabase());
    }

}