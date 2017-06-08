package nl.tudelft.b_b_w.Objects;

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
    private Chain chain;
    private User user;

    @Before
    public void setUpUser(){
        name = "testName";
        iban = "NL642335674446";
        publicKey = new Public_Key();
        chain = new Chain();
        user = new User(name, iban, publicKey, chain);
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(name, user.getName());
    }

    @Test
    public void testGetIban() throws Exception {
        assertEquals(iban, user.getIban());
    }

    @Test
    public void testPublicKey() throws Exception {
        assertEquals(publicKey, user.getPublicKey());
    }

    @Test
    public void testGetChain() throws Exception {
        assertEquals(chain, user.getChain());
    }

    @Test
    public void testAddContact() throws Exception {

    }

    @Test
    public void testRevokeContact() throws Exception {

    }

    @Test
    public void testQueryContact() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {

    }

}