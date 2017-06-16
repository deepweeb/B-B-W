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

import nl.tudelft.bbw.blockchain.TrustValues;

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
                BlockType.GENESIS, 1, Hash.NOT_AVAILABLE, Hash.NOT_AVAILABLE,
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
                BlockType.GENESIS, 1, Hash.NOT_AVAILABLE, Hash.NOT_AVAILABLE,
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
