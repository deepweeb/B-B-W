package nl.tudelft.b_b_w.model;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrustValuesUnitTest {

    /**
     * testTrustValuesInitialized
     * tests whether the trust value of the initalized is correct
     */
    @Test
    public void testTrustValuesInitialized() {
        assertEquals(10, TrustValues.INITIALIZED.getValue());
    }

    /**
     * testTrustValuesRevoke
     * tests whether the trust value of the revoke is correct
     */
    @Test
    public void testTrustValuesRevoke() {
        assertEquals(0, TrustValues.REVOKED.getValue());
    }
}
