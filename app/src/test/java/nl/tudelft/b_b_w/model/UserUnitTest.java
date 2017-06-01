package nl.tudelft.b_b_w.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserUnitTest {

    private final String name = "NAME";
    private final String IBAN = "IBAN";
    private User _user;

    /**
     * setUp method
     * Initializes a test user
     */
    @Before
    public void setUp() {
        this._user = new User(name, IBAN);
    }

    /**
     * Testing the getName method.
     */
    @Test
    public void getName(){
        assertEquals(name, _user.getName());
    }

    /**
     * Testing the getIBAN method.
     */
    @Test
    public void getIBAN(){
        assertEquals(IBAN, _user.getIBAN());
    }

    /**
     * Testing the generatePublicKey method.
     */
    @Test
    public void generatePublicKey(){
        final String PUBLIC_KEY = "PUBLIC_KEY";
        assertEquals(PUBLIC_KEY, _user.generatePublicKey());
    }
}