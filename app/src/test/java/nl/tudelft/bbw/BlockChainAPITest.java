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


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class BlockChainAPITest {

    /**
     * Testing attributes which are used more than once
     */
    private User apiUser;
    private Acquaintance acquaintanceB;
    private List<Block> list;

    /**
     * Initialize BlockChainAPI before every Junit tests
     *
     * @throws HashException               when the hash methods are not available
     * @throws BlockAlreadyExistsException when a block already exists
     */
    @Before
    public final void setUp() throws HashException, BlockAlreadyExistsException {
        // You are apiUser user with name "A" and Iban number "IbanOfA"
        apiUser = BlockChainAPI.initializeAPI("A", "ibanOfA", RuntimeEnvironment.application);
        list = BlockChainAPI.getContactsOf(apiUser);

        // Initialization of acquaintanceB
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        acquaintanceB = new Acquaintance("B", "IbanOfB", ED25519.getPublicKey(privateKey), new ArrayList<List<Block>>());

        // Initialization of the multichain of acquaintanceB with B's genesis block
        List<List<Block>> newUserMultichain = new ArrayList<List<Block>>();
        List<Block> test = new ArrayList<Block>();
        test.add(new Block(acquaintanceB));
        newUserMultichain.add(test);
        acquaintanceB.setMultichain(newUserMultichain);
        acquaintanceB.setPrivateKey(privateKey);
    }

    /**
     * Test if adding an contact function addContact() to the chain works correctly
     *
     * @throws HashException               when the hash methods are not available
     * @throws BlockAlreadyExistsException when a block already exists
     */
    @Test
    public final void addContactTest() throws HashException, BlockAlreadyExistsException,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //add acquaintance multichain right after pairing
        BlockChainAPI.addAcquaintanceMultichain(acquaintanceB);

        //add acquaintance right after you review and accept them
        Block newBlock = BlockChainAPI.addContact(acquaintanceB);

        list.add(newBlock);
        assertEquals(BlockChainAPI.getContactsOf(apiUser), list);
    }


    /**
     * Test if adding the contact(userC) of your contact (acquaintanceB) works properly
     *
     * @throws HashException               when the hash methods are not available
     * @throws BlockAlreadyExistsException when a block already exists
     */
    @Test
    public final void addContactofContactTest() throws HashException, BlockAlreadyExistsException,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        //Initializing userC.
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        User userC = new User("C", "iban3", ED25519.getPublicKey(privateKey));
        Block userCGenesis = new Block(userC);


        //Add userC to the chain of acquaintanceB
        Block keyblock = createKeyBlock(acquaintanceB.getMultichain().get(0).get(0), userCGenesis, BlockType.ADD_KEY);
        List<List<Block>> multichain = new ArrayList<List<Block>>();
        List<Block> test = new ArrayList<Block>();
        test.add(acquaintanceB.getMultichain().get(0).get(0));
        test.add(keyblock);
        multichain.add(test);

        //Add genesis of userC into multichain of acquaintanceB
        List<Block> test2 = new ArrayList<Block>();
        test2.add(userCGenesis);
        multichain.add(test2);
        acquaintanceB.setMultichain(multichain);

        //Add multichain of acquaintanceB into your database right after pairing
        BlockChainAPI.addAcquaintanceMultichain(acquaintanceB);
        //Add  acquaintanceB to apiUser (yourself)
        Block blockB = BlockChainAPI.addContact(acquaintanceB);
        list.add(blockB);

        //query for userC as if you go through your contact list of acquaintanceB
        // (userC is not your contact yet)
        User contactOfcontact = BlockChainAPI.getContactsOf(acquaintanceB).get(1).getContact();

        //add userC
        Block blockC = BlockChainAPI.addContact(contactOfcontact);
        list.add(blockC);

        assertEquals(BlockChainAPI.getContactsOf(apiUser), list);
    }

    /**
     * Test if revoking a contact from your chain with RevokeContact() works properly
     * that getContactsOf(apiUser) should not display acquaintanceB as a contact after revoking
     *
     * @throws HashException               when the hash methods are not available
     * @throws BlockAlreadyExistsException when a block already exists
     */
    @Test
    public final void revokeContactFromChainTest() throws HashException, BlockAlreadyExistsException,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //Add acquaintanceB into you chain
        BlockChainAPI.addAcquaintanceMultichain(acquaintanceB);
        BlockChainAPI.addContact(acquaintanceB);
        // Revoke acquaintanceB from your chain
        BlockChainAPI.revokeContact(acquaintanceB);
        assertEquals(BlockChainAPI.getContactsOf(apiUser), list);
    }

    /**
     * Test if the method isDatabaseEmpty() works properly
     */
    @Test
    public final void databaseEmptyTest() {
        assertFalse(BlockChainAPI.isDatabaseEmpty());
    }

    /**
     * Test if Trust Value of a block is upgraded correctly after an successful transaction
     */
    @Test
    public final void successfulTransactionTest() {
        BlockChainAPI.successfulTransactionTrustUpdate(list.get(0));
        assertTrue(list.get(0).getTrustValue() > TrustValues.INITIALIZED.getValue());
    }

    /**
     * Test if Trust Value of a block is downgraded correctly after an successful transaction
     */
    @Test
    public final void failedTransactionTest() {
        BlockChainAPI.failedTransactionTrustUpdate(list.get(0));
        assertTrue(list.get(0).getTrustValue() < TrustValues.INITIALIZED.getValue());
    }

    /**
     * Test if Trust Value of a block is upgraded correctly after an successful Iban verification
     */
    @Test
    public final void verifyIBANTest() {
        BlockChainAPI.verifyIBANTrustUpdate(list.get(0));
        assertTrue(list.get(0).getTrustValue() > TrustValues.INITIALIZED.getValue());
    }

    /**
     * Test to verify if the Acquaintance object of your self is generated correctly.
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws HashException               when the hash methods are not available
     * @throws BlockAlreadyExistsException when a block already exists
     */
    @Test
    public final void makeAcquaintanceTest() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, BlockAlreadyExistsException, HashException {
        //Add user B into you chain
        BlockChainAPI.addAcquaintanceMultichain(acquaintanceB);
        BlockChainAPI.addContact(acquaintanceB);
        list = BlockChainAPI.getContactsOf(apiUser);

        Acquaintance testAcquaintance = BlockChainAPI.makeAcquaintanceObject();
        //assert if all the attributes are correctly formed into the Acquaintance object
        assertEquals(apiUser.getName(), testAcquaintance.getName());
        assertEquals(apiUser.getIban(), testAcquaintance.getIban());
        assertEquals(apiUser.getPublicKey(), testAcquaintance.getPublicKey());
        assertTrue(testAcquaintance.getMultichain().contains(list));
    }


    /**
     * Method to return a key block to add to a chain
     * This method is made  to create test blocks for the Junit tests
     *
     * @param latest    The latest block of your chain
     * @param contact   The genesis block of your contact who you want to add
     * @param blockType The type of the keyblock. It is usually the ADD_KEY type
     * @return the keyblock which represent your contact to put in your chain
     * @throws HashException
     * @throws BlockAlreadyExistsException
     */
    private Block createKeyBlock(Block latest, Block contact,
                                 BlockType blockType) throws HashException, BlockAlreadyExistsException {
        Hash previousBlockHash = latest.getOwnHash();
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
