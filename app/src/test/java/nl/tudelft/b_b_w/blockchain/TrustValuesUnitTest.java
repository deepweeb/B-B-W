package nl.tudelft.b_b_w.blockchain;


import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import nl.tudelft.b_b_w.blockchain.TrustValues;

public class TrustValuesUnitTest {

    /**
     * testTrustValuesInitialized
     * tests whether the trust value of the initalized is correct
     */
    @Test
    public void testTrustValuesInitialized() {
        assertEquals(10.0, TrustValues.INITIALIZED.getValue());
    }

    /**
     * testTrustValuesRevoke
     * tests whether the trust value of the revoke is correct
     */
    @Test
    public void testTrustValuesRevoke() {
        assertEquals(0.0, TrustValues.REVOKED.getValue());
    }
}
