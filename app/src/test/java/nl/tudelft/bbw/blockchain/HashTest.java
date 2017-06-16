package nl.tudelft.bbw.blockchain;

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
     */
    @Test
    public void toStringTest() {
        assertEquals(hashString, hash.toString());
    }

    /**
     * equals() and hashCode() method testing.
     */
    @Test
    public void testEqualsSymmetric() {
        Hash x = new Hash("testHash");  // equals and hashCode check name field value
        Hash y = new Hash("testHash");
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
        assertTrue(x.equals(x));
        assertFalse(x.equals(hashString));
    }



}