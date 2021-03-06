package nl.tudelft.bbw.blockchain;


import static junit.framework.Assert.assertEquals;

import org.junit.Test;

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
