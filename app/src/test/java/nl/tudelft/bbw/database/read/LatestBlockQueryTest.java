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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Tests for the UserExistQuery class
 * A: genesis, add B, revoke B
 * B: genesis, add C
 * C: genesis
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class LatestBlockQueryTest {
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
        aAddsB = new Block(alice, bob, new BlockData(BlockType.ADD_KEY, genesisA.getSequenceNumber()+1,
                genesisA.getOwnHash(), genesisB.getOwnHash(), 0));
        aRevokesB = new Block(alice, bob, new BlockData(BlockType.REVOKE_KEY, aAddsB.getSequenceNumber()+1,
                aAddsB.getOwnHash(), Hash.NOT_AVAILABLE, 0));
        bAddsC = new Block(bob, carol, new BlockData(BlockType.ADD_KEY, genesisB.getSequenceNumber()+1,
                genesisB.getOwnHash(), genesisC.getOwnHash(), 0));

        database.write(new BlockAddQuery(aAddsB));
        database.write(new BlockAddQuery(aRevokesB));
        database.write(new BlockAddQuery(bAddsC));

    }

    /**
     * Test if the latest block of A is correct
     */
    @Test
    public void testLatestA() {
        LatestBlockQuery latestQuery = new LatestBlockQuery(database, alice);
        database.read(latestQuery);
        assertEquals(aRevokesB, latestQuery.getLatestBlock());
    }

    /**
     * Test if the latest block of B is correct
     */
    @Test
    public void testLatestB() {
        LatestBlockQuery latestQuery = new LatestBlockQuery(database, bob);
        database.read(latestQuery);
        assertEquals(bAddsC, latestQuery.getLatestBlock());
    }

    /**
     * Test if the latest block of C (its genesis) is correct
     */
    @Test
    public void testLatestC() {
        LatestBlockQuery latestQuery = new LatestBlockQuery(database, carol);
        database.read(latestQuery);
        assertEquals(genesisC, latestQuery.getLatestBlock());
    }

    /**
     * Test if a non-existing user has null for latest block
     */
    @Test
    public void testLatestNonexistent() {
        User sinterklaas = new User("Sinterklaas", "gratis",
                ED25519.getPublicKey(ED25519.generatePrivateKey()));
        LatestBlockQuery latestQuery = new LatestBlockQuery(database, sinterklaas);
        database.read(latestQuery);
        assertNull(latestQuery.getLatestBlock());
    }
}
