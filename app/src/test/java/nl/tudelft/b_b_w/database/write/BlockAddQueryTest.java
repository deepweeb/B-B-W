package nl.tudelft.b_b_w.database.write;

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
import nl.tudelft.b_b_w.database.Database;
import nl.tudelft.b_b_w.database.read.GetChainQuery;
import nl.tudelft.b_b_w.model.TrustValues;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for the ChainSizeQuery
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class BlockAddQueryTest {
    /**
     * Add one block
     */
    @Test
    public void testSimpleAdd() throws Exception {
        Database database = new Database(RuntimeEnvironment.application);
        User alice = new User("Alice", "IBANA", ED25519.getPublicKey(ED25519.generatePrivateKey()));

        // create genesis block
        Block genesis = new Block(alice, alice, new BlockData(
                BlockType.GENESIS, 1, Hash.NOT_AVAILABLE, Hash.NOT_AVAILABLE,
                TrustValues.INITIALIZED.getValue()));

        // add user to database
        UserAddQuery addUser = new UserAddQuery(alice);
        database.write(addUser);

        // add one genesis block
        BlockAddQuery query = new BlockAddQuery(genesis);
        database.write(query);

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

        // example users
        User alice = new User("Alice", "IBANA", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        User bob = new User("Bob", "IBANB", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        User carol = new User("Carol", "IBANC", ED25519.getPublicKey(ED25519.generatePrivateKey()));

        // add users
        database.write(new UserAddQuery(alice));
        database.write(new UserAddQuery(bob));
        database.write(new UserAddQuery(carol));

        // create genesis block
        Block genesisA = new Block(alice, alice, new BlockData(
                BlockType.GENESIS, 1, Hash.NOT_AVAILABLE, Hash.NOT_AVAILABLE,
                TrustValues.INITIALIZED.getValue()));
        Block genesisB = new Block(bob, bob, new BlockData(
                BlockType.GENESIS, 1, Hash.NOT_AVAILABLE, Hash.NOT_AVAILABLE,
                TrustValues.INITIALIZED.getValue()));
        Block genesisC = new Block(carol, carol, new BlockData(
                BlockType.GENESIS, 1, Hash.NOT_AVAILABLE, Hash.NOT_AVAILABLE,
                TrustValues.INITIALIZED.getValue()));

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
