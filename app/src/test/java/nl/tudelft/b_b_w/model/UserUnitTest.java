package nl.tudelft.b_b_w.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for User class
 */
public class UserUnitTest {

    /**
     * Class attributes
     */
    private final String name = "NAME";
    private final String iban = "iban";
    private final String publicKey = "publicKey";
    private User user;

    /**
     * setUp method
     * Initializes a test user
     */
    @Before
    public void setUp() {
        this.user = mock(User.class);
        when(user.generatePublicKey()).thenReturn(publicKey);
        when(user.getName()).thenReturn(name);
        when(user.getIban()).thenReturn(iban);
    }

    /**
     * Testing the getName method.
     */
    @Test
    public void getName() {
        assertEquals(name, user.getName());
    }

    /**
     * Testing the getIBAN method.
     */
    @Test
    public void getIban() {
        assertEquals(iban, user.getIban());
    }

    /**
     * Testing the generatePublicKey method.
     */
    @Test
    public void generatePublicKey() {
        assertEquals(publicKey, user.generatePublicKey());
    }
}