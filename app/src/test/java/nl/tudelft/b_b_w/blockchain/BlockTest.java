package nl.tudelft.b_b_w.blockchain;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.b_b_w.blockchaincontroller.ConversionController;
import nl.tudelft.b_b_w.controller.ED25519;

import static junit.framework.Assert.assertEquals;

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

    /**
     * The block.
     */
    private Block block;

    /**
     * Set up the before testing.
     */
    @Before
    public void setUpBlock() throws Exception {

        //setting up owner
        ownerName = "BlockOwner1";
        ownerIban = "Owner1Iban";
        //object to generate public key
        EdDSAPrivateKey edDSAPrivateKey1 =
                ED25519.generatePrivateKey(Utils.hexToBytes("0000000000000000000000000000000000000000000000000000000000000000"));
        ownerPublicKey = ED25519.getPublicKey(edDSAPrivateKey1);
        owner = new User(ownerName, ownerIban, ownerPublicKey);

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
        sequenceNumber = 1;
        previousHashChain = new Hash("Contact1PreviousHashChain");
        previousHashSender = new Hash("Contact1PreviousHashSender");
        trustValue = 0;
        blockData = new BlockData(blockType, sequenceNumber, previousHashChain, previousHashSender, trustValue);

        //setting up block
        block = new Block(owner, contact, blockData);
    }

    @Test
    public void getterTests() throws Exception {

        //testing block owner getters
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
        assertEquals(sequenceNumber, block.getSequenceNumber());
        assertEquals(previousHashChain, block.getPreviousHashChain());
        assertEquals(previousHashSender, block.getPreviousHashSender());
        assertEquals(trustValue, block.getTrustValue());
    }

    @Test
    public void getOwnHash() throws Exception {
        ConversionController conversionController = new ConversionController(owner, contact, blockData);
        Hash testHash = conversionController.hashKey();
        assertEquals(testHash, block.getOwnHash());
    }
}