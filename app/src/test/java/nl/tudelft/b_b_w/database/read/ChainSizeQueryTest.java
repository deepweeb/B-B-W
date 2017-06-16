package nl.tudelft.b_b_w.database.read;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.BlockData;
import nl.tudelft.b_b_w.blockchain.BlockType;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.database.Database;
import nl.tudelft.b_b_w.database.write.BlockAddQuery;
import nl.tudelft.b_b_w.database.write.UserAddQuery;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for the ChainSizeQuery
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class ChainSizeQueryTest {
    /**
     * Example users to test with
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
        alice = new User("Adam", "ibanA", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        bob = new User("Bob", "ibanB", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        carol = new User("Carol", "ibanC", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        database = new Database(RuntimeEnvironment.application);
    }

    /**
     * Check if empty chain returns zero
     */
    @Test
    public void testEmptyChainSize() {
        ChainSizeQuery query = new ChainSizeQuery(alice);
        database.read(query);
        int chainsize = query.getSize();
        assertEquals(0, chainsize);
    }

    /**
     * Check if empty chain returns zero
     */
    @Test
    public void testSimpleChain() throws Exception {
        // add block
        Block genesis = new Block(alice);

        // add user
        database.write(new UserAddQuery(alice));

        BlockAddQuery addQuery = new BlockAddQuery(genesis);
        database.write(addQuery);

        // size should be one
        ChainSizeQuery sizeQuery = new ChainSizeQuery(alice);
        database.read(sizeQuery);
        int chainsize = sizeQuery.getSize();
        assertEquals(1, chainsize);
    }

    /**
     * Test a large chain size
     */
    @Test
    public void testLargeChain() {
        Block genesisA = new Block(alice);
        Block genesisB = new Block(bob);
        Block genesisC = new Block(carol);
        Block aAddB = new Block(alice, bob, new BlockData(BlockType.ADD_KEY, 2,
                genesisA.getOwnHash(), genesisB.getOwnHash(), 0));
        Block aAddC = new Block(alice, carol, new BlockData(BlockType.ADD_KEY, 3,
                aAddB.getOwnHash(), genesisC.getOwnHash(), 0));

        database.write(new BlockAddQuery(genesisA));
        database.write(new BlockAddQuery(genesisB));
        database.write(new BlockAddQuery(genesisC));
        database.write(new BlockAddQuery(aAddB));
        database.write(new BlockAddQuery(aAddC));

        // size should be three
        ChainSizeQuery sizeQuery = new ChainSizeQuery(alice);
        database.read(sizeQuery);
        int chainsize = sizeQuery.getSize();
        assertEquals(3, chainsize);
    }
}
