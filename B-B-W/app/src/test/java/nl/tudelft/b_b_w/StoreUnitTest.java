package nl.tudelft.b_b_w;

import org.junit.Test;
import java.io.FileWriter;
import java.util.Set;
import java.util.HashSet;
import nl.tudelft.b_b_w.Store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class StoreUnitTest {


    /**
     * Test getStore method.
     */
    @Test
    public void testGetStore(){
        Store store = Store.getStore();
    }

    @Test
    public void testSave(){
        String key = new String("hello");

        Store.getStore().addKey(key);

    }
}