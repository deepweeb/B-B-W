package nl.tudelft.b_b_w;


import static junit.framework.Assert.assertFalse;

import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.model.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.TrustValues;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class APITest {

    /**
     * Attributes which are used more than once
     */
    private User owner;
    private User newUser;
    private API api;
    private List<Block> list;

    /**
     * Initialize API before every test
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Before
    public final void setUp() throws HashException, BlockAlreadyExistsException {
        owner = new User("Jeff", "iban", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        api = new API(owner, RuntimeEnvironment.application);
        list = api.getBlocks(owner);
        newUser = new User("Nick", "iban2", ED25519.getPublicKey(ED25519.generatePrivateKey()));
    }

    /**
     * Test if adding to chain yields a different chain
     *
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void addContactToChainTest() throws HashException, BlockAlreadyExistsException {
        API newAPI = new API(newUser, RuntimeEnvironment.application);
        list = newAPI.getBlocks(newUser);
        newAPI.addContactToChain(owner);
        assertNotEquals(newAPI.getBlocks(newUser), list);
    }

    /**
     * Test if revoking from chain yields a different chain
     *
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void revokeContactFromChainTest() throws HashException, BlockAlreadyExistsException {
        api.revokeContactFromChain(owner);
        assertNotEquals(api.getBlocks(owner), list);
    }

    /**
     * Test if adding to database yields a different chain
     *
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void addContactToDatabase() throws HashException, BlockAlreadyExistsException {
        API newAPI = new API(newUser, RuntimeEnvironment.application);
        list = newAPI.getBlocks(newUser);
        newAPI.addContactToDatabase(newUser, owner);
        assertNotEquals(newAPI.getBlocks(newUser), list);
    }

    /**
     * Test if adding revoke block to database yields a different chain
     *
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void addRevokeContactToDatabase() throws HashException, BlockAlreadyExistsException {
        api.addRevokeContactToDatabase(owner, owner);
        assertNotEquals(list, api.getBlocks(owner));
    }

    /**
     * Test if the database isn't empty
     *
     */
    @Test
    public final void databaseEmptyTest() {
        assertFalse(api.isDatabaseEmpty());
    }

    /**
     * Test if trustValue of a block changes after transaction
     *
     */
    @Test
    public final void succesfulTransactionTest() {
        api.successfulTransaction(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after transaction
     *
     */
    @Test
    public final void failedTransactionTest() {
        api.failedTransaction(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after verification
     *
     */
    @Test
    public final void verifyIBANTest() {
        api.verifyIBAN(list.get(0));
        api.getBlocks(owner).get(0).getTrustValue();
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after revoking
     *
     */
    @Test
    public final void revokedBlockTest() {
        api.revokedBlock(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }
}
