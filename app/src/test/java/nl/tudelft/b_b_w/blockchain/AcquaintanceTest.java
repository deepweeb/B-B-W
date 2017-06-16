package nl.tudelft.b_b_w.blockchain;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import nl.tudelft.b_b_w.controller.ED25519;

import static junit.framework.Assert.assertEquals;

/**
 * Testing class for Acquaintance.java
 */
public class AcquaintanceTest {

    /**
     * The user attributes
     */
    private String name;
    private String iban;
    private EdDSAPublicKey publicKey;
    private ArrayList<Chain> multiChain;

    private Acquaintance acquaintance;

    /**
     * Setting up the test object
     */
    @Before
    public void setUpUser(){
        name = "testName";
        iban = "NL642335674446";
        publicKey = ED25519.getPublicKey(ED25519.generatePrivateKey());

        multiChain = new ArrayList<Chain>();
        acquaintance = new Acquaintance(name, iban, publicKey, multiChain);

    }

    @Test
    public void getMultichain() {
        assertEquals(multiChain, acquaintance.getMultichain());
    }

}