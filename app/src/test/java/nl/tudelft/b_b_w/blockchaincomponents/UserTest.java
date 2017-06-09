package nl.tudelft.b_b_w.blockchaincomponents;

import org.junit.Before;
import org.junit.Test;

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
    private Public_Key publicKey;
    private User user;


    /**
     * Setting up the test object
     */
    @Before
    public void setUpUser(){
        name = "testName";
        iban = "NL642335674446";
        publicKey = new Public_Key();
        user = new User(name, iban, publicKey);
    }

    /**
     * getName() getter method testing
     */
    @Test
    public void testGetName() {
        assertEquals(name, user.getName());
    }

    /**
     * getIban() getter method testing
     */
    @Test
    public void testGetIban() {
        assertEquals(iban, user.getIban());
    }

    /**
     * getPublicKey() getter method testing
     */
    @Test
    public void testPublicKey() {
        assertEquals(publicKey, user.getPublicKey());
    }


}