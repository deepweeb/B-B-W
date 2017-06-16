package nl.tudelft.bbw.blockchain;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.bbw.controller.ED25519;

import static junit.framework.Assert.assertEquals;

/**
 * This class tests the User object.
 */
public class UserTest {

    /**
     * The user attributes
     */
    private String name;
    private String iban;
    private EdDSAPublicKey publicKey;
    private User user;

    /**
     * Setting up the test object
     */
    @Before
    public void setUpUser(){
        name = "testName";
        iban = "NL642335674446";
        publicKey = ED25519.getPublicKey(ED25519.generatePrivateKey());
        user = new User(name, iban, publicKey);
    }

    /**
     * getName() getter method testing
     */
    @Test
    public void getNameTest() {
        assertEquals(name, user.getName());
    }

    /**
     * getIban() getter method testing
     */
    @Test
    public void getIbanTest() {
        assertEquals(iban, user.getIban());
    }

    /**
     * getPublicKey() getter method testing
     */
    @Test
    public void getPublicKeyTest() {
        assertEquals(publicKey, user.getPublicKey());
    }

}