package nl.tudelft.bbw.blockchain;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.bbw.controller.ED25519;
import nl.tudelft.bbw.model.TrustValues;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Junit Testing class of the Block class
 */
public class BlockTest {
    /**
     * Properties of the owner of the block
     */
    private User owner;
    private String ownerName;
    private String ownerIban;
    private EdDSAPublicKey ownerPublicKey;


    /**
     * Properties of the contact/ block sender
     */
    private User contact;
    private String contactName;
    private String contactIban;
    private EdDSAPublicKey contactPublicKey;

    /**
     * Properties of a BlockData object
     */
    private BlockData blockData;
    private BlockType blockType;
    private int sequenceNumber;
    private Hash previousHashChain;
    private Hash previousHashSender;
    private double trustValue;

    private Hash testOwnHash;

    /**
     * The block.
     */
    private Block block;
    private Block genesisBlock;

    /**
     * Set up the before testing.
     */
    @Before
    public void setUpBlock() {


        //setting up owner
        ownerName = "BlockOwner1";
        ownerIban = "Owner1Iban";
        //object to generate public key
        EdDSAPrivateKey edDSAPrivateKey1 =
                ED25519.generatePrivateKey(Utils.hexToBytes("0000000000000000000000000000000000000000000000000000000000000000"));
        ownerPublicKey = ED25519.getPublicKey(edDSAPrivateKey1);
        owner = new User(ownerName, ownerIban, ownerPublicKey);

        //Setting up Genesis block
        genesisBlock = new Block(owner);

        //setting up contact
        contactName = "Contact1";
        contactIban = "Contact1Iban";
        //object to generate public key
        EdDSAPrivateKey edDSAPrivateKey2 =
                ED25519.generatePrivateKey(Utils.hexToBytes("6900000000000000000000000000000000000000000000000000000000000005"));
        contactPublicKey = ED25519.getPublicKey(edDSAPrivateKey2);
        contact = new User(contactName, contactIban, contactPublicKey);

        //setting up block data
        blockType = BlockType.ADD_KEY;
        sequenceNumber = genesisBlock.getSequenceNumber()+ 1;
        previousHashChain = genesisBlock.getPreviousHashChain();
        previousHashSender = new Hash("Contact1PreviousHashSender");
        trustValue = TrustValues.INITIALIZED.getValue();
        blockData = new BlockData(blockType, sequenceNumber, previousHashChain, previousHashSender, trustValue);
        testOwnHash = new Hash("2592b8a970eae4b8c20846737de79645ca9498cb74940f3a7e68947efa0cbbb4");

        //setting up block
        block = new Block(owner, contact, blockData);
    }

    @Test
    public void getterTests() {

        //testing block owner getters
        assertEquals(owner, genesisBlock.getBlockOwner());
        assertEquals(owner, block.getBlockOwner());
        assertEquals(ownerName, block.getOwnerName());
        assertEquals(ownerIban, block.getOwnerIban());
        assertEquals(ownerPublicKey, block.getOwnerPublicKey());

        //testing contact of the block getters
        assertEquals(contact, block.getContact());
        assertEquals(contactName, block.getContactName());
        assertEquals(contactIban, block.getContactIban());
        assertEquals(contactPublicKey, block.getContactPublicKey());

        //testing get data getters
        assertEquals(blockData, block.getBlockData());
        assertEquals(blockType, block.getBlockType());
        assertFalse(block.isRevoked());
        assertEquals(sequenceNumber, block.getSequenceNumber());
        assertEquals(previousHashChain, block.getPreviousHashChain());
        assertEquals(previousHashSender, block.getPreviousHashSender());
        assertEquals(trustValue, block.getTrustValue());

        //testing setter for trust value
        block.setTrustValue(10);
        assertEquals(10.0, block.getTrustValue());


        assertEquals(testOwnHash, block.getOwnHash());


        //verify method testing
        assertTrue(block.verifyBlock(block));
        assertFalse(block.verifyBlock(testOwnHash));
        assertFalse(block.verifyBlock(genesisBlock));
        assertFalse(block.verifyBlock(genesisBlock));
    }

    /**
     * equals() and hashCode() method testing.
     */
    @Test
    public void testEqualsSymmetric() {
        Block x = new Block(owner);  // equals and hashCode check name field value
        assertTrue(genesisBlock.equals(x) && x.equals(genesisBlock));
        assertTrue(genesisBlock.hashCode() == x.hashCode());
        assertTrue(x.equals(x));
        assertFalse(x.equals(owner));
    }

}