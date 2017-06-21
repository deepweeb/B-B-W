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
import nl.tudelft.bbw.blockchain.TrustValues;
import nl.tudelft.bbw.controller.ED25519;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;


@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, sdk = 21)
public class InteractionDesignAPI {

    /**
     * START
     */


    /**
     * Add your name and iban number
     */
    final String yourName = "Pietje Puck";
    final String yourIban = "NL45RABO94754726";

    /**
     * Add you friend name and iban number so you can pair and add him to the contact list later on
     */
    final String yourFriendName = "Johan de Boer";
    final String yourFriendIban = "NL26INGB934875943";


    /**
     * Make the API ready to do transactions.
     * @throws HashException
     * @throws BlockAlreadyExistsException
     */
    @Before
    public final void setUp() throws HashException, BlockAlreadyExistsException {
        API.initializeAPI(yourName, yourIban, RuntimeEnvironment.application);
    }


    /**
     * Get my name
     */
    @Test
    public final void getMyNameTest()
    {
        assertEquals(yourName, API.getMyName());
    }

    /**
     * Get my iban number
     */
    @Test
    public final void getMyIbanTest()
    {
        assertEquals(yourIban, API.getMyIban());
    }

    /**
     * Add a new person to the database.
     * @throws HashException
     * @throws BlockAlreadyExistsException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    @Test
    public final void addAcquaintanceTest() throws HashException, BlockAlreadyExistsException,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        /**
         *  In real life situation, an Acquaintance object is generated by the other person's
         *  application and given to you, rather than being generated here for the demonstration purpose.
         */
        Acquaintance pairedPerson = generateAcquaintanceForDemo(yourFriendName, yourFriendIban);

        List<Block> list = API.getMyContacts();
        print(API.getMyContacts());

        API.addAcquaintance(pairedPerson);
        print(API.getMyContacts());

        assertNotEquals(API.getMyContacts(), list);
    }

    /**
     * Remove a contact from the database.
     * @throws HashException
     * @throws BlockAlreadyExistsException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    @Test
    public final void revokeContactTest()
            throws HashException, BlockAlreadyExistsException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Acquaintance pairedPerson = generateAcquaintanceForDemo(yourFriendName, yourFriendIban);

        API.addAcquaintance(pairedPerson);
        List<Block> list = API.getMyContacts();
        print(API.getMyContacts());

        API.revokeContact(pairedPerson);
        print(API.getMyContacts());

        assertNotEquals(API.getMyContacts(), list);
    }


    /**
     * Check if the database is not empty.
     */
    @Test
    public final void databaseEmptyTest() {
        print(API.getMyContacts());
        assertFalse(API.isDatabaseEmpty());
    }

    /**
     * Verify if trust level increases after transaction.
     * @throws SignatureException
     * @throws HashException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BlockAlreadyExistsException
     */
    @Test
    public final void verifyIBANTest() throws SignatureException, HashException, NoSuchAlgorithmException, InvalidKeyException, BlockAlreadyExistsException {
        Acquaintance pairedPerson = generateAcquaintanceForDemo(yourFriendName, yourFriendIban);

        print(API.getMyContacts());

        API.addAcquaintance(pairedPerson);
        print(API.getMyContacts());

        List<Block> list = API.getMyContacts();

        API.verifyIBANUpdate(list.get(1));
        print(API.getMyContacts());

        assertNotEquals(list.get(1).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if a transaction was succesfull
     * @throws SignatureException
     * @throws HashException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BlockAlreadyExistsException
     */
    @Test
    public final void succesfulTransactionTest() throws SignatureException, HashException, NoSuchAlgorithmException, InvalidKeyException, BlockAlreadyExistsException {

        Acquaintance pairedPerson = generateAcquaintanceForDemo(yourFriendName, yourFriendIban);
        print(API.getMyContacts());

        API.addAcquaintance(pairedPerson);
        print(API.getMyContacts());

        List<Block> list = API.getMyContacts();

        API.successfulTransactionUpdate(list.get(1));
        print(API.getMyContacts());

        assertNotEquals(list.get(1).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Verify if if a transaction failed the trust is downgraded.
     * @throws SignatureException
     * @throws HashException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BlockAlreadyExistsException
     */
    @Test
    public final void failedTransactionTest() throws SignatureException, HashException, NoSuchAlgorithmException, InvalidKeyException, BlockAlreadyExistsException {

        Acquaintance pairedPerson = generateAcquaintanceForDemo(yourFriendName, yourFriendIban);

        print(API.getMyContacts());

        API.addAcquaintance(pairedPerson);
        print(API.getMyContacts());

        List<Block> list = API.getMyContacts();

        API.failedTransactionUpdate(list.get(1));
        print(API.getMyContacts());

        assertNotEquals(list.get(1).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Verify if the acquaintance object is setup correctly.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws BlockAlreadyExistsException
     * @throws HashException
     */
    @Test
    public final void makeAcquaintanceTest() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, BlockAlreadyExistsException, HashException {

        print(API.getMyContacts());

        Acquaintance pairedPerson = generateAcquaintanceForDemo(yourFriendName, yourFriendIban);
        API.addAcquaintance(pairedPerson);

        List<Block> list = API.getMyContacts();
        print(API.getMyContacts());

        Acquaintance myAcquaintanceObject = API.makeAcquaintanceObject();

        assertEquals(API.getMyName(), myAcquaintanceObject.getName());
        assertEquals(API.getMyIban(), myAcquaintanceObject.getIban());
        assertEquals(API.getMyPublicKey(), myAcquaintanceObject.getPublicKey());

        assertTrue(myAcquaintanceObject.getMultichain().contains(list));
    }


    /**
     * FINISH
     */



    private final Acquaintance generateAcquaintanceForDemo(String name, String iban) {

        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        Acquaintance newUser = new Acquaintance(name, iban, ED25519.getPublicKey(ED25519.generatePrivateKey()), new ArrayList<List<Block>>());
        ArrayList<List<Block>> newUserMultichain = new ArrayList<List<Block>>();
        ArrayList<Block> test = new ArrayList<Block>();
        test.add(new Block(newUser));
        newUserMultichain.add(test);
        newUser.setMultichain(newUserMultichain);
        newUser.setPrivateKey(privateKey);
        return newUser;
    }
    private final void print(List<Block> list) {
        for(Block e: list)
        {
            System.out.println(e.toString2());
        }
        System.out.println();
    }


}
