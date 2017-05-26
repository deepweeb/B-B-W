package nl.tudelft.b_b_w.model;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrustValuesUnitTest {

    /**
     * testTrustValuesRevoke
     * tests whether the trust value of the revoke is correct
     */
    @Test
    public void testTrustValuesRevoke() {
        assertEquals(0, TrustValues.REVOKED.getValue());
    }

    /**
     * testTrustValuesVerified
     * tests whether the trust value of the verified is correct
     */
    @Test
    public void testTrustValuesVerified() {
        assertEquals(50, TrustValues.VERIFIED.getValue());
    }

    /**
     * testTrustValuesSuccessfulTransaction
     * tests whether the trust value of the successful transaction is correct
     */
    @Test
    public void testTrustValuesSuccessfulTransaction() {
        assertEquals(10, TrustValues.SUCCESFUL_TRANSACTION.getValue());
    }

    /**
     * testTrustValuesFailedTransaction
     * tests whether the trust value of the failed transaction is correct
     */
    @Test
    public void testTrustValuesFailedTransaction() {
        assertEquals(-10, TrustValues.FAILED_TRANSACTION.getValue());
    }
}
