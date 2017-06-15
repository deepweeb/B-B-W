package nl.tudelft.b_b_w.controller;


import static junit.framework.Assert.assertFalse;

import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.User;
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
     */
    @Before
    public final void setUp() throws Exception {
        owner = new User("Jeff", "iban", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        api = new API(owner, RuntimeEnvironment.application);
        list = api.getBlocks(owner);
        newUser = new User("Nick", "iban2", ED25519.getPublicKey(ED25519.generatePrivateKey()));
    }

    /**
     * Test if adding to chain yields a different chain
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void addContactToChainTest() throws Exception {
        API newAPI = new API(newUser, RuntimeEnvironment.application);
        list = newAPI.getBlocks(newUser);
        newAPI.addContactToChain(owner);
        assertNotEquals(newAPI.getBlocks(newUser), list);
    }

    /**
     * Test if revoking from chain yields a different chain
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void revokeContactFromChainTest() throws Exception {
        api.revokeContactFromChain(owner);
        assertNotEquals(api.getBlocks(owner), list);
    }

    /**
     * Test if adding to database yields a different chain
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void addContactToDatabase() throws Exception {
        API newAPI = new API(newUser, RuntimeEnvironment.application);
        list = newAPI.getBlocks(newUser);
        newAPI.addContactToDatabase(newUser, owner);
        assertNotEquals(newAPI.getBlocks(newUser), list);
    }

    /**
     * Test if adding revoke block to database yields a different chain
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void addRevokeContactToDatabase() throws Exception {
        api.addRevokeContactToDatabase(owner, owner);
        assertNotEquals(list, api.getBlocks(owner));
    }

    /**
     * Test if the database isn't empty
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void databaseEmptyTest() {
        assertFalse(api.isDatabaseEmpty());
    }

    /**
     * Test if trustValue of a block changes after transaction
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void succesfulTransactionTest() throws Exception {
        api.successfulTransaction(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after transaction
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void failedTransactionTest() throws Exception {
        api.failedTransaction(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after verification
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void verifyIBANTest() throws Exception {
        api.verifyIBAN(list.get(0));
        double val = api.getBlocks(owner).get(0).getTrustValue();
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after revoking
     *
     * @throws Exception RuntimeException
     */
    @Test
    public final void revokedBlockTest() throws Exception {
        api.revokedBlock(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }
}
