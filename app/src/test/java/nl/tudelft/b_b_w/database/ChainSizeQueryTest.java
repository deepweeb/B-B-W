package nl.tudelft.b_b_w.database;

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
import nl.tudelft.b_b_w.blockchain.Hash;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.database.read.ChainSizeQuery;
import nl.tudelft.b_b_w.database.write.BlockAddQuery;
import nl.tudelft.b_b_w.database.write.UserAddQuery;
import nl.tudelft.b_b_w.model.TrustValues;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for the ChainSizeQuery
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class ChainSizeQueryTest {
    final String notAvailable = "N/A";

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
        Block genesis = new Block(alice, alice, new BlockData(
                BlockType.GENESIS, 1, new Hash(notAvailable), new Hash(notAvailable),
                TrustValues.INITIALIZED.getValue()));

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
     * Test chain size with three users in a chain
     */
    @Test
    public void testMultipleGenesis() throws Exception {
        // add genesis
        Block genesisA = new Block(alice, alice, new BlockData(
                BlockType.GENESIS, 1, new Hash(notAvailable), new Hash(notAvailable),
                TrustValues.INITIALIZED.getValue()));
        Block genesisB = new Block(bob, bob, new BlockData(
                BlockType.GENESIS, 1, new Hash(notAvailable), new Hash(notAvailable),
                TrustValues.INITIALIZED.getValue()));
        Block genesisC = new Block(carol, carol, new BlockData(
                BlockType.GENESIS, 1, new Hash(notAvailable), new Hash(notAvailable),
                TrustValues.INITIALIZED.getValue()));

        // add them
        database.write(new BlockAddQuery(genesisA));
        database.write(new BlockAddQuery(genesisB));
        database.write(new BlockAddQuery(genesisC));

        // verify all chains have size 1
        ChainSizeQuery sizeA = new ChainSizeQuery(alice);
        ChainSizeQuery sizeB = new ChainSizeQuery(bob);
        ChainSizeQuery sizeC = new ChainSizeQuery(carol);
        database.read(sizeA);
        database.read(sizeB);
        database.read(sizeC);
        assertEquals(1, sizeA.getSize());
        assertEquals(1, sizeB.getSize());
        assertEquals(1, sizeC.getSize());
    }
}
