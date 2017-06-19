package nl.tudelft.bbw.database.read;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

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

/**
 * Tests for converting the database to a multichain
 * A: genesis, add B, revoke B
 * B: genesis, add C
 * C: genesis
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class DatabaseToMultichainQueryTest {
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
     * Create example users and blocks
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

    /**
     * Convert the test database to a multichain
     */
    @Test
    public void testDatabaseToMultichain() {
        // construct expected blockchain
        List<List<Block>> expectedMultichain = new ArrayList<List<Block>>();
        List<Block> aliceBlocks = new ArrayList<Block>();
        List<Block> bobBlocks = new ArrayList<Block>();
        List<Block> carolBlocks = new ArrayList<Block>();
        aliceBlocks.add(genesisA);
        aliceBlocks.add(aAddsB);
        aliceBlocks.add(aRevokesB);
        bobBlocks.add(genesisB);
        bobBlocks.add(bAddsC);
        carolBlocks.add(genesisC);
        expectedMultichain.add(aliceBlocks);
        expectedMultichain.add(bobBlocks);
        expectedMultichain.add(carolBlocks);

        // perform query
        DatabaseToMultichainQuery query = new DatabaseToMultichainQuery(database);
        database.read(query);
        assertEquals(expectedMultichain, query.getMultichain());
    }
}
