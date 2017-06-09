package nl.tudelft.b_b_w.blockchaincomponents;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * This test class tests the functionality of the object.
 */
public class BlockTest {


    /**
     * The contact user attributes.
     */
    private String nameContact;
    private String ibanContact;
    private Public_Key publicKeyContact;
    private User contact;
    private String hash;

    /**
     * The block owner attributes.
     */
    private String nameOwner;
    private String ibanOwner;
    private Public_Key publicKeyOwner;
    private User blockOwner;

    /**
     * The block data.
     */
    private BlockData blockData;

    /**
     * The block.
     */
    private Block block;

    /**
     * Set up the before testing.
     */
    @Before
    public void setUpBlock() {
        nameContact = "Naqib";
        ibanContact = "NL642335674446";
        publicKeyContact = new Public_Key();
        contact = new User(nameContact, ibanContact, publicKeyContact);
        nameOwner = "blockOwnie";

        block = new Block(contact, nameOwner, blockData);


    }

    /**
     * Test whether the getContact method returns the correct attribute.
     *
     * @throws Exception
     */
    @Test
    public void testGetContact() throws Exception {
        assertEquals(contact, block.getContact());
    }

    /**
     * Test whether the getBlockOwner method returns the correct attribute.
     *
     * @throws Exception
     */
    @Test
    public void testGetBlockOwner() throws Exception {
        assertEquals(nameOwner, block.getBlockOwner());
    }

    /**
     * Test whether the getBlockData method returns the correct attribute.
     *
     * @throws Exception
     */
    @Test
    public void testGetBlockData() throws Exception {
        assertEquals(blockData, block.getBlockData());
    }


//    @Test
//    public void testGetPreviousHashSender() {
//        String prevHashSender = "Hassan";
//        block.getBlockData().setPreviousHashSender(prevHashSender);
//        assertEquals(prevHashSender, block.getPreviousHashSender());
//    }





}