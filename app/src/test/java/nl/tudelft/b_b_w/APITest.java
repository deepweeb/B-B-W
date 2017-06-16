package nl.tudelft.b_b_w;


import android.content.Context;

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
import nl.tudelft.b_b_w.exception.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.exception.HashException;
import nl.tudelft.b_b_w.blockchain.TrustValues;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class APITest {

    /**
     * Attributes which are used more than once
     */
    private User owner;
    private User newUser;
    private Context context;
    private List<Block> list;

    /**
     * Initialize API before every test
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Before
    public final void setUp() throws HashException, BlockAlreadyExistsException {
        owner = new User("Jeff", "iban", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        this.context = RuntimeEnvironment.application;
        list = API.getBlocks(owner, context);
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
        list = API.getBlocks(newUser, context);
        API.addContactToChain(newUser, context, owner);
        assertNotEquals(API.getBlocks(newUser, context), list);
    }

    /**
     * Test if revoking from chain yields a different chain
     *
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void revokeContactFromChainTest() throws HashException, BlockAlreadyExistsException {
        API.revokeContactFromChain(owner, context, owner);
        assertNotEquals(API.getBlocks(owner, context), list);
    }

    /**
     * Test if adding to database yields a different chain
     *
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void addContactToDatabase() throws HashException, BlockAlreadyExistsException {
        list = API.getBlocks(newUser, context);
        API.addContactToDatabase(newUser, context, owner);
        assertNotEquals(API.getBlocks(newUser, context), list);
    }

    /**
     * Test if adding revoke block to database yields a different chain
     *
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void addRevokeContactToDatabase() throws HashException, BlockAlreadyExistsException {
        API.addRevokeContactToDatabase(owner, context, owner);
        assertNotEquals(list, API.getBlocks(owner, context));
    }

    /**
     * Test if the database isn't empty
     *
     */
    @Test
    public final void databaseEmptyTest() {
        assertFalse(API.isDatabaseEmpty(context));
    }

    /**
     * Test if trustValue of a block changes after transaction
     *
     */
    @Test
    public final void succesfulTransactionTest() {
        API.successfulTransaction(owner, context, list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after transaction
     *
     */
    @Test
    public final void failedTransactionTest() {
        API.failedTransaction(owner, context, list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after verification
     *
     */
    @Test
    public final void verifyIBANTest() {
        API.verifyIBAN(owner, context, list.get(0));
        API.getBlocks(owner, context).get(0).getTrustValue();
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }

    /**
     * Test if trustValue of a block changes after revoking
     *
     */
    @Test
    public final void revokedBlockTest() {
        API.revokedBlock(owner, context, list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }
}
