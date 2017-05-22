package nl.tudelft.b_b_w.ModelsUnitTest;

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
    private final String owner = "owner";
    private final int sequenceNumber = 0;
    private final String ownHash = "ownHash";
    private final String previousHashChain = "previousHashChain";
    private final String previousHashSender = "previousHashSender";
    private final String publicKey = "publicKey";
    private final String iban = "iban";
    private final boolean isRevoked = false;
    private BlockFactory blockFactory;

    /**
     * This method runs before each test to initialize the test object
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Before
    public void makeNewBlock() throws Exception {
        _block = new Block(owner, sequenceNumber, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
        blockFactory = new BlockFactory();
    }

    /**
     * Tests whether the creation through a block factory works
     * With a normal block
     */
    @Test
    public void testGetBlock(){
        Block newBlock = BlockFactory.getBlock("BLOCK", owner, sequenceNumber, ownHash, previousHashChain, previousHashSender, publicKey,iban);
        assertEquals(_block, newBlock);
    }

    /**
     * Tests whether the creation through a block factory works
     * With a normal block
     */
    @Test
    public void testGetRevokeBlock(){
        Block newBlock = BlockFactory.getBlock("BLOCK", owner, sequenceNumber, ownHash, previousHashChain, previousHashSender, publicKey,iban);
        assertEquals(_block, newBlock);
    }

    /**
     * Tests whether the creation through a block factory works
     * With an empty type
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetBlockEmpty(){
        BlockFactory.getBlock("", owner, sequenceNumber, ownHash, previousHashChain, previousHashSender, publicKey, iban);
    }

    /**
     * Tests whether the creation through a block factory works
     * With a faulty type
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetBlockFaultyString(){
        BlockFactory.getBlock("block", owner, sequenceNumber, ownHash, previousHashChain, previousHashSender, publicKey,iban);
    }

}