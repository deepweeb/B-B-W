package nl.tudelft.b_b_w.blockchain;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
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
     * equals() and hashCode() method testing.
     */
    @Test
    public void testEquals_Symmetric() {
        Hash x = new Hash("testHash");  // equals and hashCode check name field value
        Hash y = new Hash("testHash");
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
        assertTrue(x.equals(x));
        assertFalse(x.equals(hashString));
    }



}