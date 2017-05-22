package nl.tudelft.b_b_w.ModelsUnitTest;

import org.junit.Test;
import nl.tudelft.b_b_w.model.User;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

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
        User user = new User();

        assertNotNull(user);
    }
}