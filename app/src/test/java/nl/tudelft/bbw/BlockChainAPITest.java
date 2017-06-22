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
import static nl.tudelft.bbw.BlockChainAPI.addAcquaintance;
import static org.junit.Assert.assertNotEquals;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class BlockChainAPITest {

    /**
     * Attributes which are used more than once
     */
    private Acquaintance newUser;
    private List<Block> list;

    /**
     * Initialize BlockChainAPI before every test
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Before
    public final void setUp() throws HashException, BlockAlreadyExistsException {
        BlockChainAPI.initializeAPI("Jeff", "iban", RuntimeEnvironment.application);
        list = BlockChainAPI.getMyContacts();

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
        Block newBlock = BlockChainAPI.addAcquaintance(newUser);
        list.add(newBlock);
       assertEquals(BlockChainAPI.getMyContacts(), list);
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
        addAcquaintance(newUser);
        List<Block> list = BlockChainAPI.getMyContacts();
        BlockChainAPI.revokeContact(newUser);
        assertNotEquals(BlockChainAPI.getMyContacts(), list);
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
        BlockChainAPI.getMyContacts().get(0).getTrustValue();
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }


    /**
     * Test to verify if the Acquaintance object is generated correctly.
     */
    @Test
    public final void makeAcquaintanceTest() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, BlockAlreadyExistsException, HashException {
        addAcquaintance(newUser);
        list = BlockChainAPI.getMyContacts();
        Acquaintance testAcquaintance = BlockChainAPI.makeAcquaintanceObject();
        List<Block> list2 = testAcquaintance.getMultichain().get(0);
        assertEquals(BlockChainAPI.getMyName(), testAcquaintance.getName());
        assertEquals(BlockChainAPI.getMyIban(), testAcquaintance.getIban());
        assertEquals(BlockChainAPI.getMyPublicKey(), testAcquaintance.getPublicKey());
        assertTrue(testAcquaintance.getMultichain().contains(list));
    }

}
