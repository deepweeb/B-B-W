package nl.tudelft.b_b_w.blockchain;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * This class tests the Hash object.
 */
public class HashTest {

    /**
     * The hash attributes
     */
    private String hashString;
    private Hash hash;

    /**
     * Setting up the test object
     */
    @Before
    public void setUpHash(){
        hashString = "testHash123456789";
        hash = new Hash(hashString);
    }

    /**
     * toString() method testing
     * @throws Exception
     */
    @Test
    public void toStringTest() throws Exception {
        assertEquals(hashString, hash.toString());
    }

    /**
     * equals() method testing
     * @throws Exception
     */
    @Test
    public void equalsTest() throws Exception {
        final Hash testHash = new Hash("testHash123456789");
        assertTrue(hash.equals(testHash));
    }

}