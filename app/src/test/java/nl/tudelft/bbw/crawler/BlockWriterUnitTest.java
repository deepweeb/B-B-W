package nl.tudelft.bbw.crawler;

import org.junit.After;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.bbw.blockchain.BlockData;

import static org.junit.Assert.assertEquals;

/**
 * Test class for BlockWriter
 */
public class BlockWriterUnitTest {

    /**
     * Test to check whether writing to a file works
     */
    @Test
    public void testWriteToJson() throws IOException {
        Map<String, BlockData> mapper = new HashMap<>();
        BlockWriter.writeToJson(mapper);
        BufferedReader br = new BufferedReader(new FileReader(BlockWriter.OUTPUT_PATH));
        assertEquals("{}", br.readLine());
    }

    /**
     * Remove test key files
     */
    @After
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void delete() {
        new File(BlockWriter.OUTPUT_PATH).delete();
    }

}