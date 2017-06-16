package nl.tudelft.bbw.blockchain;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
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
    private EdDSAPrivateKey privateKey;
    private User user;

    /**
     * Setting up the test object
     */
    @Before
    public void setUpUser(){
        name = "testName";
        iban = "NL642335674446";
        privateKey = ED25519.generatePrivateKey();
        publicKey = ED25519.getPublicKey(privateKey);
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

    /**
     * Test for getter and setter of private key
     */
    @Test
    public void getsetPrivateKey() throws Exception {
        user.setPrivateKey(privateKey);
        assertEquals(privateKey, user.getPrivateKey());
    }

}