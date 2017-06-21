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
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class APITest {

    /**
     * Attributes which are used more than once
     */
    private Acquaintance newUser;
    private List<Block> list;

    /**
     * Initialize API before every test
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Before
    public final void setUp() throws HashException, BlockAlreadyExistsException {
        API.initializeAPI("Jeff", "iban", RuntimeEnvironment.application);
        list = API.getMyContacts();

        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        newUser = new Acquaintance("Nick", "iban2", ED25519.getPublicKey(privateKey), new ArrayList<List<Block>>());

        List<List<Block>> newUserMultichain = new ArrayList<List<Block>>();
        List<Block> test = new ArrayList<Block>();
        test.add(new Block(newUser));
        newUserMultichain.add(test);
        newUser.setMultichain(newUserMultichain);
        newUser.setPrivateKey(privateKey);
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
        API.addAcquaintance(newUser);
        assertNotEquals(API.getMyContacts(), list);
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
        API.addAcquaintance(newUser);
        List<Block> list = API.getMyContacts();
        API.revokeContact(newUser);
        assertNotEquals(API.getMyContacts(), list);
    }

    /**
     * Test if the database isn't empty
     */
    @Test
    public final void databaseEmptyTest() {
        assertFalse(API.isDatabaseEmpty());
    }

    /**
     * Test if trustValue of a block changes after transaction
     */
    @Test
    public final void succesfulTransactionTest() {
        API.successfulTransactionUpdate(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after transaction
     */
    @Test
    public final void failedTransactionTest() {
        API.failedTransactionUpdate(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after verification
     */
    @Test
    public final void verifyIBANTest() {
        API.verifyIBANUpdate(list.get(0));
        API.getMyContacts().get(0).getTrustValue();
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }


    /**
     * Test to verify if the Acquaintance object is generated correctly.
     */
    @Test
    public final void makeAcquaintanceTest() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, BlockAlreadyExistsException, HashException {
        API.addAcquaintance(newUser);
        list = API.getMyContacts();
        Acquaintance testAcquaintance = API.makeAcquaintanceObject();
        assertEquals(API.getMyName(), testAcquaintance.getName());
        assertEquals(API.getMyIban(), testAcquaintance.getIban());
        assertEquals(API.getMyPublicKey(), testAcquaintance.getPublicKey());
        assertTrue(testAcquaintance.getMultichain().contains(list));
    }

}
