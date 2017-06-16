package nl.tudelft.bbw.database.read;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.bbw.BuildConfig;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.BlockData;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.blockchain.Hash;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.ED25519;
import nl.tudelft.bbw.database.Database;
import nl.tudelft.bbw.database.write.BlockAddQuery;
import nl.tudelft.bbw.database.write.UserAddQuery;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for the UserExistQuery class
 * A: genesis, add B, revoke B
 * B: genesis, add C
 * C: genesis
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class BlockExistQueryTest {
    /**
     * Example blocks to test with
     */
    private Block aAddsB;
    private Block aRevokesB;
    private Block bAddsC;

    private Block genesisA;
    private Block genesisB;
    private Block genesisC;

    /**
     * Example users
     */
    private User alice;
    private User bob;
    private User carol;

    /**
     * Test database
     */
    private Database database;

    /**
     * Create example users
     */
    @Before
    public void init() {
        database = new Database(RuntimeEnvironment.application);
        alice = new User("Alice", "ibanA", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        bob = new User("Bob", "ibanB", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        carol = new User("Carol", "ibanC", ED25519.getPublicKey(ED25519.generatePrivateKey()));

        // add users
        database.write(new UserAddQuery(alice));
        database.write(new UserAddQuery(bob));
        database.write(new UserAddQuery(carol));

        // add genesises
        genesisA = new Block(alice);
        genesisB = new Block(bob);
        genesisC = new Block(carol);
        database.write(new BlockAddQuery(genesisA));
        database.write(new BlockAddQuery(genesisB));
        database.write(new BlockAddQuery(genesisC));

        // add specific blocks
        aAddsB = new Block(alice, bob, new BlockData(BlockType.ADD_KEY, 2,
                genesisA.getOwnHash(), genesisB.getOwnHash(), 0));
        aRevokesB = new Block(alice, bob, new BlockData(BlockType.REVOKE_KEY, 3,
                aAddsB.getOwnHash(), Hash.NOT_AVAILABLE, 0));
        bAddsC = new Block(bob, carol, new BlockData(BlockType.ADD_KEY, 3,
                genesisB.getOwnHash(), genesisC.getOwnHash(), 0));

        database.write(new BlockAddQuery(aAddsB));
        database.write(new BlockAddQuery(aRevokesB));
        database.write(new BlockAddQuery(bAddsC));
    }

    @Test
    public void testGenesisExists() {
        BlockExistQuery query = new BlockExistQuery(new Block(alice));
        database.read(query);
        assertTrue(query.blockExists());
    }

    @Test
    public void testRevokeInexists() {
        Block bRevokesC = new Block(bob, carol, new BlockData(BlockType.REVOKE_KEY, 3,
                genesisB.getOwnHash(), genesisC.getOwnHash(), 0));
        BlockExistQuery query = new BlockExistQuery(bRevokesC);
        database.read(query);
        assertFalse(query.blockExists());
    }

    @Test
    public void testGenesisInexists() {
        User luat = new User("Luat", "ibanLuat", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        BlockExistQuery query = new BlockExistQuery(new Block(luat));
        database.read(query);
        assertFalse(query.blockExists());
    }

    // B can add A
    @Test
    public void testBAddsA() {
        Block bAddsA = new Block(bob, alice, new BlockData(BlockType.ADD_KEY, 3,
                genesisC.getOwnHash(), Hash.NOT_AVAILABLE, 0));

        // query
        BlockExistQuery query = new BlockExistQuery(bAddsA);
        database.read(query);
        assertFalse(query.blockExists());
    }

    // B cannot add C
    @Test
    public void testBAddsC() {
        Block bAddsC2 = new Block(bob, carol, new   BlockData(BlockType.ADD_KEY, 3,
                genesisC.getOwnHash(), Hash.NOT_AVAILABLE, 0));

        // query
        BlockExistQuery query = new BlockExistQuery(bAddsC2);
        database.read(query);
        assertTrue(query.blockExists());
    }
}
