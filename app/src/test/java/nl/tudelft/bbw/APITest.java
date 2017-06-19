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

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class APITest {

    /**
     * Attributes which are used more than once
     */
    private Acquaintance owner;
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
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        owner = new Acquaintance("Jeff", "iban", ED25519.getPublicKey(privateKey), new ArrayList<Chain>());
        owner.setPrivateKey(privateKey);
        API.initializeAPI(owner, RuntimeEnvironment.application);
        list = API.getBlocks(owner);

        privateKey = ED25519.generatePrivateKey();
        newUser = new Acquaintance("Nick", "iban2", ED25519.getPublicKey(privateKey), new ArrayList<Chain>());
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
        API.initializeAPI(newUser, RuntimeEnvironment.application);
        final byte[] message = owner.getPublicKey().getEncoded();
        final byte[] signature = ED25519.generateSignature(message, newUser.getPrivateKey());
        list = API.getBlocks(newUser);
        API.addAcquaintanceToChain(owner, signature, message);
        assertNotEquals(API.getBlocks(newUser), list);
        API.debug();
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

/*    *//**
     * Test if trustValue of a block changes after revoking
     *//*
    @Test
    public final void revokedBlockTest() {
        API.revokedBlock(list.get(0));
        assertNotEquals(list.get(0).getTrustValue(), TrustValues.INITIALIZED);
    }*/
}
