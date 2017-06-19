package nl.tudelft.bbw.database.write;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import nl.tudelft.bbw.BuildConfig;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.BlockData;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.blockchain.Hash;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.ED25519;
import nl.tudelft.bbw.database.Database;
import nl.tudelft.bbw.database.read.GetChainQuery;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Tests for the RemoveChainQuery
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class RemoveChainQueryTest {
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
     * Create example database, users, and blocks
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
     * Remove Alice's chain and verify that it is truly gone
     */
    @Test
    public void testRemoveAlice() {
        RemoveChainQuery query = new RemoveChainQuery(alice);
        database.write(query);

        // verify
        GetChainQuery verifyQuery = new GetChainQuery(database, alice);
        database.read(verifyQuery);
        assertNull(verifyQuery.getChain());
    }

    /**
     * Remove Carol's chain and verify that it is truly gone
     */
    @Test
    public void testRemoveCarol() {
        RemoveChainQuery query = new RemoveChainQuery(carol);
        database.write(query);

        // verify
        GetChainQuery verifyQuery = new GetChainQuery(database, carol);
        database.read(verifyQuery);
        assertNull(verifyQuery.getChain());
    }

    /**
     * Remove Bob's chain and verify that Alice's chain is still intact
     */
    @Test
    public void testOtherChainIntact() {
        // original Alice query
        GetChainQuery originalQuery = new GetChainQuery(database, alice);
        database.read(originalQuery);
        List<Block> original = originalQuery.getChain();

        // remove Bob's chain
        RemoveChainQuery query = new RemoveChainQuery(bob);
        database.write(query);

        // verify
        GetChainQuery verifyQuery = new GetChainQuery(database, alice);
        database.read(verifyQuery);
        List<Block> verify = verifyQuery.getChain();
        assertEquals(original, verify);
    }
}
