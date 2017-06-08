package nl.tudelft.b_b_w.Objects;

import org.junit.Before;
import org.junit.Test;

/**
 * This test class tests the functionality of the object.
 */
public class BlockTest {

    /**
     * Block attribute data.
     */
    private User user;
    private User chainOwner;
    private BlockData blockData;
    private Block block;


    /**
     * The user attributes
     */
    private String name;
    private String iban;
    private Public_Key publicKey;
    private Chain chain;
    private User user;

    @Before
    public void setUpBlock(){
        name = "testName";
        iban = "NL642335674446";
        publicKey = new Public_Key();
        user = new User(name, iban, publicKey, chain);

    }

    @Test
    public void getContact() throws Exception {

    }

    @Test
    public void getChainOwner() throws Exception {

    }

    @Test
    public void getBlockData() throws Exception {

    }

}