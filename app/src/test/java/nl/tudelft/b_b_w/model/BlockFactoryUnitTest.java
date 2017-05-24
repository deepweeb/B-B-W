package nl.tudelft.b_b_w.model;

import org.junit.Before;
import org.junit.Test;
import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BlockFactoryUnitTest {

    private Block _block;
    private final String TYPE_BLOCK = "BLOCK";
    private final String TYPE_REVOKE = "REVOKE";
    private final String owner = "owner";
    private final String ownHash = "ownHash";
    private final String previousHashChain = "previousHashChain";
    private final String previousHashSender = "previousHashSender";
    private final String publicKey = "publicKey";
    private final String iban = "iban";
    private final int trustValue = 0;
    private final boolean isRevoked = false;
    private BlockFactory blockFactory;

    /**
     * This method runs before each test to initialize the test object
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Before
    public void makeNewBlock() throws Exception {

        _block = BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);

        blockFactory = new BlockFactory();
    }

    /**
     * Tests whether the creation through a block factory works
     * With a normal block
     */
    @Test
    public void testGetBlock(){

        final Block newBlock = BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);

        assertEquals(_block, newBlock);
    }

    /**
     * Tests whether the creation through a block factory works
     * With a normal block
     */
    @Test
    public void testGetRevokeBlock(){

        final Block newBlock = BlockFactory.getBlock(TYPE_BLOCK, owner, ownHash,
                previousHashChain, previousHashSender, publicKey, iban, trustValue);

        assertEquals(_block, newBlock);
    }

    /**
     * Tests whether the creation through a block factory works
     * With an empty type
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetBlockEmpty(){
        BlockFactory.getBlock("", owner, ownHash, previousHashChain, previousHashSender, publicKey,
                iban, trustValue);
    }

    /**
     * Tests whether the creation through a block factory works
     * With a faulty type
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetBlockFaultyString(){
        BlockFactory.getBlock("block", owner,  ownHash, previousHashChain, previousHashSender,
                publicKey, iban, trustValue);
    }

}
