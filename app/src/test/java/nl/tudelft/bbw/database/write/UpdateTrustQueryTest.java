package nl.tudelft.bbw.database.write;

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
import nl.tudelft.bbw.database.read.LatestBlockQuery;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for the UserExistQuery class
 * A: genesis, add B, revoke B
 * B: genesis, add C
 * C: genesis
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class UpdateTrustQueryTest {
    /**
     * Example blocks to test with
     */
    private Block aAddsB;
    private Block aRevokesB;
    private Block bAddsC;

    /**
     * Example genesises
     */
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
                genesisA.getOwnHash(), genesisB.getOwnHash(), 0.0));
        aRevokesB = new Block(alice, bob, new BlockData(BlockType.REVOKE_KEY, 3,
                aAddsB.getOwnHash(), Hash.NOT_AVAILABLE, 0));
        bAddsC = new Block(bob, carol, new BlockData(BlockType.ADD_KEY, 3,
                genesisB.getOwnHash(), genesisC.getOwnHash(), 0.0));

        database.write(new BlockAddQuery(aAddsB));
        database.write(new BlockAddQuery(aRevokesB));
        database.write(new BlockAddQuery(bAddsC));
    }

    /**
     * Update trust once and test if it is updated
     */
    @Test
    public void testSimpleUpdateTrust() {
        Block bAddsCTrust = new Block(bob, carol, new BlockData(BlockType.ADD_KEY, 3,
                genesisB.getOwnHash(), genesisC.getOwnHash(), 10.0));
        database.write(new UpdateTrustQuery(bAddsCTrust));

        // verify
        LatestBlockQuery latest = new LatestBlockQuery(database, bob);
        database.read(latest);
        assertEquals(10.0, latest.getLatestBlock().getTrustValue());
    }

    /**
     * Update trust thrice and test if it is updated
     */
    @Test
    public void testTripleUpdateTrust() {
        Block bAddsCTrust1 = new Block(bob, carol, new BlockData(BlockType.ADD_KEY, 3,
                genesisB.getOwnHash(), genesisC.getOwnHash(), 10.0));
        Block bAddsCTrust2 = new Block(bob, carol, new BlockData(BlockType.ADD_KEY, 3,
                genesisB.getOwnHash(), genesisC.getOwnHash(), 20.0));
        Block bAddsCTrust3 = new Block(bob, carol, new BlockData(BlockType.ADD_KEY, 3,
                genesisB.getOwnHash(), genesisC.getOwnHash(), 30.0));
        database.write(new UpdateTrustQuery(bAddsCTrust1));
        database.write(new UpdateTrustQuery(bAddsCTrust2));
        database.write(new UpdateTrustQuery(bAddsCTrust3));

        // verify
        LatestBlockQuery latest = new LatestBlockQuery(database, bob);
        database.read(latest);
        assertEquals(30.0, latest.getLatestBlock().getTrustValue());
    }
}
