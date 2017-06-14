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
import static junit.framework.Assert.assertNull;

/**
 * Test the GetChainQuery
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class GetChainQueryTest {
    /**
     * Example database
     */
    private Database database;

    /**
     * Example users
     */
    private User alice;
    private User bob;

    /**
     * Initialize the example variables
     */
    @Before
    public void init() {
        database = new Database(RuntimeEnvironment.application);
        alice = new User("Alice", "ibanA", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        bob = new User("Bob", "ibanB", ED25519.getPublicKey(ED25519.generatePrivateKey()));
    }

    /**
     * Non-existing user returns null as chain
     */
    @Test
    public void testEmptyDatabase() {
        GetChainQuery query = new GetChainQuery(database, alice);
        database.read(query);
        assertNull(query.getChain());
    }

    /**
     * Check if the query really filters blocks
     */
    @Test
    public void testEmptyChain() {
        // write chain
        Block genesis = new Block(alice, alice, new BlockData(
                BlockType.GENESIS, 1, new Hash("N/A"), new Hash("N/A"),
                TrustValues.INITIALIZED.getValue()));
        database.write(new BlockAddQuery(genesis));

        // read empty chain
        GetChainQuery query = new GetChainQuery(database, bob);
        database.read(query);
        assertNull(query.getChain());
    }

    /**
     * Chain consisting of only one genesis block
     */
    @Test
    public void testSimpleChain() {
        // write chain
        Block genesis = new Block(alice, alice, new BlockData(
                BlockType.GENESIS, 1, new Hash("N/A"), new Hash("N/A"),
                TrustValues.INITIALIZED.getValue()));

        database.write(new UserAddQuery(alice));
        database.write(new BlockAddQuery(genesis));

        // read chain
        GetChainQuery query = new GetChainQuery(database, alice);
        database.read(query);
        List<Block> expected = new ArrayList<Block>();
        expected.add(genesis);
        assertEquals(expected, query.getChain());
    }
}
