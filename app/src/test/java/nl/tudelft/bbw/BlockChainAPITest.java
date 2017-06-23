package nl.tudelft.bbw;


import net.i2p.crypto.eddsa.EdDSAPrivateKey;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.bbw.blockchain.Acquaintance;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.BlockData;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.blockchain.Hash;
import nl.tudelft.bbw.blockchain.TrustValues;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.ED25519;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class BlockChainAPITest {

    /**
     * Attributes which are used more than once
     */
    private User APIUser;
    private Acquaintance userB;
    private List<Block> list;

    /**
     * Initialize BlockChainAPI before every test
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Before
    public final void setUp() throws HashException, BlockAlreadyExistsException {
        // You are user with name A
        APIUser = BlockChainAPI.initializeAPI("A", "iban", RuntimeEnvironment.application);
        list = BlockChainAPI.getContactsOf(APIUser);

        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        userB = new Acquaintance("B", "iban2", ED25519.getPublicKey(privateKey), new ArrayList<List<Block>>());

        List<List<Block>> newUserMultichain = new ArrayList<List<Block>>();
        List<Block> test = new ArrayList<Block>();
        test.add(new Block(userB));
        newUserMultichain.add(test);
        userB.setMultichain(newUserMultichain);
        userB.setPrivateKey(privateKey);
    }

    /**
     * Test if adding to chain yields a different chain
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void addContactToChainTest() throws HashException, BlockAlreadyExistsException,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //add acquaintance multichain right after pairing
        BlockChainAPI.addAcquaintanceMultichain(userB);

        //add acquaintance right after you review and accept them
        Block newBlock = BlockChainAPI.addContact(userB);

        list.add(newBlock);
       assertEquals(BlockChainAPI.getContactsOf(APIUser), list);
    }


    /**
     * Test if adding the contact of your contact works properly
     * You are A, you want to add a contact of B whose name is C
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void addContactofContactToChainTest() throws HashException, BlockAlreadyExistsException,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        //Initializing C.
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        User userC = new User("C", "iban3", ED25519.getPublicKey(privateKey));
        Block userCGenesis = new Block(userC);


        //Add C to the chain of B
        Block keyblock = createKeyBlock(userB.getMultichain().get(0).get(0), userCGenesis, BlockType.ADD_KEY);
        List<List<Block>> multichain = new ArrayList<List<Block>>();
        List<Block> test = new ArrayList<Block>();
        test.add(userB.getMultichain().get(0).get(0));
        test.add(keyblock);
        multichain.add(test);

        //Add genesis of C into multichain of B
        List<Block> test2 = new ArrayList<Block>();
        test2.add(userCGenesis);
        multichain.add(test2);
        userB.setMultichain(multichain);

        //Add multichain of B into your database right after pairing
        BlockChainAPI.addAcquaintanceMultichain(userB);
        //Add contact B to A (yourself)
       Block blockB = BlockChainAPI.addContact(userB);
        list.add(blockB);

        //query for User C as if you go through your contact list of B (C is not your contact yet)
        User contactOfcontact = BlockChainAPI.getContactsOf(userB).get(1).getContact();

        //add contact C
        Block blockC = BlockChainAPI.addContact(contactOfcontact);
        list.add(blockC);

        assertEquals(BlockChainAPI.getContactsOf(APIUser), list);
    }

    /**
     * Test if revoking from chain yields a different chain
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void revokeContactFromChainTest()
            throws HashException, BlockAlreadyExistsException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //Add user B into you chain
        BlockChainAPI.addAcquaintanceMultichain(userB);
        BlockChainAPI.addContact(userB);

        List<Block> list = BlockChainAPI.getContactsOf(APIUser);
        BlockChainAPI.revokeContact(userB);
        assertNotEquals(BlockChainAPI.getContactsOf(APIUser), list);
    }

    /**
     * Test if the database isn't empty
     */
    @Test
    public final void databaseEmptyTest() {
        assertFalse(BlockChainAPI.isDatabaseEmpty());
    }

    /**
     * Test if trustValue of a block changes after transaction
     */
    @Test
    public final void succesfulTransactionTest() {
        BlockChainAPI.successfulTransactionTrustUpdate(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after transaction
     */
    @Test
    public final void failedTransactionTest() {
        BlockChainAPI.failedTransactionTrustUpdate(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after verification
     */
    @Test
    public final void verifyIBANTest() {
        BlockChainAPI.verifyIBANTrustUpdate(list.get(0));
        BlockChainAPI.getContactsOf(APIUser).get(0).getTrustValue();
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }


    /**
     * Test to verify if the Acquaintance object is generated correctly.
     */
    @Test
    public final void makeAcquaintanceTest() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, BlockAlreadyExistsException, HashException {
        //Add user B into you chain
        BlockChainAPI.addAcquaintanceMultichain(userB);
        BlockChainAPI.addContact(userB);

        list = BlockChainAPI.getContactsOf(APIUser);
        Acquaintance testAcquaintance = BlockChainAPI.makeAcquaintanceObject();
        List<Block> list2 = testAcquaintance.getMultichain().get(0);
        assertEquals(APIUser.getName(), testAcquaintance.getName());
        assertEquals(APIUser.getIban(), testAcquaintance.getIban());
        assertEquals(APIUser.getPublicKey(), testAcquaintance.getPublicKey());
        assertTrue(testAcquaintance.getMultichain().contains(list));
    }


    /**
     * Method to return a key block to add to a chain
     * This method is made  to create test blocks for the tests
     *
     * @param contact
     * @param blockType
     * @return
     * @throws HashException
     * @throws BlockAlreadyExistsException
     */
    private Block createKeyBlock(Block latest, Block contact,
                              BlockType blockType) throws HashException, BlockAlreadyExistsException {
        Hash previousBlockHash = latest.getOwnHash();
        // always link to genesis of contact blocks
        Hash contactBlockHash;
        contactBlockHash = contact.getOwnHash();
        int seqNumber = latest.getSequenceNumber() + 1;

        BlockData blockData = new BlockData(blockType, seqNumber, previousBlockHash,
                contactBlockHash, TrustValues.INITIALIZED.getValue()
        );
        final Block block = new Block(latest.getBlockOwner(), contact.getBlockOwner(), blockData);
        return block;
    }
}
