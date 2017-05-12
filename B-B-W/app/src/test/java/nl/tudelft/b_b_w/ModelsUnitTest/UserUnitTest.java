package nl.tudelft.b_b_w.ModelsUnitTest;

import org.junit.Before;
import org.junit.Test;
import nl.tudelft.b_b_w.Models.User;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserUnitTest {

    /**
     * Testing the get User method.
     */
    @Test
    public void getUserTest(){
        User user = User.getUser();
        User user2 = User.getUser();

        assertEquals(user, user2);
    }
}