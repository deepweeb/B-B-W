package nl.tudelft.bbw.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;

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

import nl.tudelft.bbw.BuildConfig;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.BlockData;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.blockchain.Hash;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.database.DatabaseException;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

import static org.junit.Assert.assertEquals;


/**
 * Unit test for the BlockController class
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class BlockControllerUnitTest {

    /**
     * Attributes which are used more than once
     */
    private User owner;
    private BlockController blockController;
    private User userB;
    private Block genesisA;

    /**
     * Initialize BlockController before every test
     * Setting up an owner, and a user
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Before
    public final void setUp() throws HashException, BlockAlreadyExistsException {
        //setting up owner
        String ownerName = "BlockOwner1";
        String ownerIban = "Owner1Iban";
        //object to generate public key
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        owner = new User(ownerName, ownerIban, ED25519.getPublicKey(privateKey));
        owner.setPrivateKey(privateKey);

        blockController = new BlockController(owner, RuntimeEnvironment.application);

        //Setting up Genesis block
        genesisA = blockController.createGenesis(owner);

        EdDSAPublicKey publicKeyB = ED25519.getPublicKey(ED25519.generatePrivateKey());
        userB = new User("B", "ibanB", publicKeyB);
        blockController.createGenesis(userB);
    }

    /**
     * Test adding a block
     *
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void testAddBlock() throws HashException, BlockAlreadyExistsException,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final byte[] message = genesisA.getContactPublicKey().getEncoded();
        final byte[] signature = ED25519.generateSignature(message, owner.getPrivateKey());
        List<Block> list = new ArrayList<>();
        list.add(genesisA);
        list.add(blockController.addBlockToChain(userB, signature, message));
        assertEquals(list, blockController.getBlocks(owner));
    }

    /**
     * Test revoking a block
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void testRevokeBlock() throws HashException, BlockAlreadyExistsException,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final byte[] message = genesisA.getBlockOwner().getPublicKey().getEncoded();
        final byte[] signature = ED25519.generateSignature(message, genesisA.getBlockOwner().getPrivateKey());
        List<Block> list = new ArrayList<>();
        list.add(genesisA);
        blockController.addBlockToChain(userB, signature, message);
        blockController.revokeBlockFromChain(userB);
        assertEquals(list, blockController.getBlocks(owner));
    }

    /**
     * Test for updating the trustValue of a block
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test
    public final void testUpdateBlock() throws HashException, BlockAlreadyExistsException {
        List<Block> list = new ArrayList<>();
        Block updated = new Block(owner, owner, new BlockData(BlockType.GENESIS, 1,
                Hash.NOT_AVAILABLE, Hash.NOT_AVAILABLE, 50));
        blockController.updateTrustOfBlock(updated);
        list.add(updated);
        assertEquals(list, blockController.getBlocks(owner));
    }

    /**
     * Tests adding a duplicate block thrice
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test(expected = DatabaseException.class)
    public final void testAddDupBlocks() throws HashException, BlockAlreadyExistsException {
        Block blockC = blockController.createGenesis(owner);
        blockController.addBlock(blockC);
        blockController.addBlock(blockC);
    }

    /**
     * Tests adding a block when the user hasn't a genesis block yet
     * @throws HashException When creating a block results in an error
     * @throws BlockAlreadyExistsException when adding a block results in an error
     */
    @Test(expected = IllegalArgumentException.class)
    public final void noGenesisTest() throws HashException, BlockAlreadyExistsException {
        User newUser = new User("Jeff", "iban", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        blockController.createKeyBlock(newUser, owner);
    }
}
