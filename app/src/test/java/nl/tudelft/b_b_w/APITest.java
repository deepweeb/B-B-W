package nl.tudelft.b_b_w;


import static junit.framework.Assert.assertFalse;

import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.TrustValues;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.exception.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.exception.HashException;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class APITest {

    /**
     * Attributes which are used more than once
     */
    private User owner;
    private User newUser;
    private List<Block> list;

    /**
     * Initialize API before every test
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Before
    public final void setUp() throws HashException, BlockAlreadyExistsException {
        owner = new User("Jeff", "iban", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        API.initializeAPI(owner, RuntimeEnvironment.application);
        list = API.getBlocks(owner);
        newUser = new User("Nick", "iban2", ED25519.getPublicKey(ED25519.generatePrivateKey()));
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
        API.initializeAPI(newUser, RuntimeEnvironment.application);
        final byte[] signature = ED25519.generateSignature(new byte[]{}, owner.getPrivateKey());
        list = API.getBlocks(newUser);
        API.addContactToChain(owner, signature, new byte[] {});
        assertNotEquals(API.getBlocks(newUser), list);
    }

    /**
     * Test if revoking from chain yields a different chain
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void revokeContactFromChainTest()
            throws HashException, BlockAlreadyExistsException {
        API.revokeContactFromChain(owner);
        assertNotEquals(API.getBlocks(owner), list);
    }

    /**
     * Test if adding to database yields a different chain
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void addContactToDatabase() throws HashException, BlockAlreadyExistsException {
        API.initializeAPI(newUser, RuntimeEnvironment.application);
        list = API.getBlocks(newUser);
        API.addContactToDatabase(newUser, owner);
        assertNotEquals(API.getBlocks(newUser), list);
    }

    /**
     * Test if adding revoke block to database yields a different chain
     *
     * @throws HashException               When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void addRevokeContactToDatabase()
            throws HashException, BlockAlreadyExistsException {
        API.addRevokeContactToDatabase(owner, owner);
        assertNotEquals(list, API.getBlocks(owner));
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
        API.successfulTransaction(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after transaction
     */
    @Test
    public final void failedTransactionTest() {
        API.failedTransaction(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after verification
     */
    @Test
    public final void verifyIBANTest() {
        API.verifyIBAN(list.get(0));
        API.getBlocks(owner).get(0).getTrustValue();
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after revoking
     */
    @Test
    public final void revokedBlockTest() {
        API.revokedBlock(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }
}
