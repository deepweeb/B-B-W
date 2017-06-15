package nl.tudelft.b_b_w.database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.BlockData;
import nl.tudelft.b_b_w.blockchain.BlockType;
import nl.tudelft.b_b_w.blockchain.Hash;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.database.read.GetChainQuery;
import nl.tudelft.b_b_w.database.write.BlockAddQuery;
import nl.tudelft.b_b_w.database.write.UserAddQuery;
import nl.tudelft.b_b_w.model.TrustValues;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for the ChainSizeQuery
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class UserAddQueryTest {
    private User alice;
    final String notAvailable = "N/A";

    @Before
    public void init() {
        alice = new User("Alice", "IBANA", ED25519.getPublicKey(ED25519.generatePrivateKey()));
    }

    /**
     * Add one block
     */
    @Test
    public void testSimpleAdd() throws Exception {
        // create genesis block
        Block genesis = new Block(alice, alice, new BlockData(
                BlockType.GENESIS, 1, new Hash(notAvailable), new Hash(notAvailable), TrustValues.INITIALIZED.getValue()
        ));

        // add one genesis block
        BlockAddQuery query = new BlockAddQuery(genesis);
        Database database = new Database(RuntimeEnvironment.application);
        database.write(query);
        database.write(new UserAddQuery(alice));

        // verify
        GetChainQuery chainQuery = new GetChainQuery(database, alice);
        database.read(chainQuery);
        List<Block> expected = new ArrayList<Block>();
        expected.add(genesis);
        List<Block> actual = chainQuery.getChain();
        assertEquals(expected, actual);
    }

    /**
     * Add three different genesis blocks
     */
    @Test
    public void testMultipleGenesis() throws Exception {
        Database database = new Database(RuntimeEnvironment.application);
        User alice = new User("Alice", "IBANA", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        User bob = new User("Bob", "IBANB", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        User carol = new User("Carol", "IBANC", ED25519.getPublicKey(ED25519.generatePrivateKey()));

        // create genesis blocks
        Block genesisA = new Block(alice, alice, new BlockData(
                BlockType.GENESIS, 1, new Hash(notAvailable), new Hash(notAvailable),
                TrustValues.INITIALIZED.getValue()));
        Block genesisB = new Block(bob, bob, new BlockData(
                BlockType.GENESIS, 1, new Hash(notAvailable), new Hash(notAvailable),
                TrustValues.INITIALIZED.getValue()));
        Block genesisC = new Block(carol, carol, new BlockData(
                BlockType.GENESIS, 1, new Hash(notAvailable), new Hash(notAvailable),
                TrustValues.INITIALIZED.getValue()));

        // add users
        database.write(new UserAddQuery(alice));
        database.write(new UserAddQuery(bob));
        database.write(new UserAddQuery(carol));

        // add one genesis block
        BlockAddQuery queryA = new BlockAddQuery(genesisA);
        BlockAddQuery queryB = new BlockAddQuery(genesisB);
        BlockAddQuery queryC = new BlockAddQuery(genesisC);
        database.write(queryA);
        database.write(queryB);
        database.write(queryC);

        // verify
        GetChainQuery chainQueryA = new GetChainQuery(database, alice);
        GetChainQuery chainQueryB = new GetChainQuery(database, bob);
        GetChainQuery chainQueryC = new GetChainQuery(database, carol);
        database.read(chainQueryA);
        database.read(chainQueryB);
        database.read(chainQueryC);
        List<Block> expectedA = new ArrayList<Block>();
        List<Block> expectedB = new ArrayList<Block>();
        List<Block> expectedC = new ArrayList<Block>();
        expectedA.add(genesisA);
        expectedB.add(genesisB);
        expectedC.add(genesisC);
        List<Block> actualA = chainQueryA.getChain();
        List<Block> actualB = chainQueryB.getChain();
        List<Block> actualC = chainQueryC.getChain();

        // NOTE: three asserts because they belong together
        assertEquals(expectedA, actualA);
        assertEquals(expectedB, actualB);
        assertEquals(expectedC, actualC);
    }
}
