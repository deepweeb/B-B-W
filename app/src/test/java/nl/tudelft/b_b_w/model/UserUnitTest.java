package nl.tudelft.b_b_w.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserUnitTest {

    private final String name = "NAME";
    private final String iban = "IBAN";
    private User _user;

    /**
     * setUp method
     * Initializes a test user
     */
    @Before
    public void setUp() {
        this._user = mock(User.class);
        when(_user.generatePublicKey()).thenReturn("PUBLIC_KEY");
        when(_user.getName()).thenReturn(name);
        when(_user.getIBAN()).thenReturn(iban);
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
        assertEquals(iban, _user.getIBAN());
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