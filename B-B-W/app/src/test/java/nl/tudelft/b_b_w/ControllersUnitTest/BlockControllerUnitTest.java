package nl.tudelft.b_b_w.ControllersUnitTest;

import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.Controllers.BlockController;
import nl.tudelft.b_b_w.Models.Block;
import nl.tudelft.b_b_w.Models.BlockFactory;

import static org.junit.Assert.assertEquals;

//import android.test.mock.MockContext;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk= 21,  manifest = "src/main/AndroidManifest.xml")
public class BlockControllerUnitTest {

    /**
     * Attributes
     */
    private BlockController bc;
    private final String owner = "owner";
    private final int sequenceNumber = 1;
    private final String ownHash = "ownHash";
    private final String previousHashChain = "previousHashChain";
    private final String previousHashSender = "previousHashSender";
    private final String publicKey = "publicKey";
    private Block _block;
    private final String TYPE_BLOCK = "BLOCK";
    private final String TYPE_REVOKE = "REVOKE";

    /**
     * Initialize BlockController before every test
     * And initialize a dummy block _block
     */
    @Before
    public void setUp() {
        this.bc = new BlockController(RuntimeEnvironment.application);
        this._block = BlockFactory.getBlock(TYPE_BLOCK, owner, sequenceNumber, ownHash,
                previousHashChain, previousHashSender, publicKey);
    }

    /**
     * Tests adding a block
     * @throws Exception RuntimeException
     */
    @Test
    public void testAddBlock() throws Exception {
        bc.addBlock(_block);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        assertEquals(bc.getBlocks(owner), list);
    }

    /**
     * Tests adding two blocks
     * @throws Exception RuntimeException
     */
    @Test
    public void testAddBlock2() throws Exception {
        String newOwner = owner+"2";
        Block newBlock = BlockFactory.getBlock(TYPE_BLOCK, newOwner, sequenceNumber, ownHash,
                previousHashChain, previousHashSender, publicKey);
        bc.addBlock(_block);
        bc.addBlock(newBlock);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        list.add(newBlock);

        List<Block> newList = bc.getBlocks(owner);
        newList.addAll(bc.getBlocks(newOwner));

        assertEquals(newList, list);
}

    /**
     * Tests adding a duplicate block
     */
    @Test(expected=RuntimeException.class)
    public void testAddDupBlocks() {
        bc.addBlock(_block);
        bc.addBlock(_block);
    }

    /**
     * Tests adding an already revoked block
     */
    @Test(expected=RuntimeException.class)
    public void alreadyRevoked() {
        Block newBlock = BlockFactory.getBlock(TYPE_REVOKE, owner, sequenceNumber, ownHash,
                previousHashChain, previousHashSender, publicKey);
        bc.addBlock(newBlock);
        bc.addBlock(_block);
    }

    /**
     * Tests filtering duplicates out of a list
     */
    @Test
    public void testEmptyList() {
        bc.addBlock(_block);
        Block newBlock = BlockFactory.getBlock(TYPE_BLOCK, owner, sequenceNumber+1, ownHash,
                previousHashChain, previousHashSender, publicKey);
        bc.revokeBlock(newBlock);
        List<Block> list = new ArrayList<>();
        assertEquals(list, bc.getBlocks(owner));
    }

    /**
     * Test removeBlock if the specified revoked block has no match
     */
    @Test
    public void testRemoveWithNoMatch() throws Resources.NotFoundException{
        bc.addBlock(_block);
        Block blc2 = BlockFactory.getBlock(TYPE_BLOCK, owner+"2", sequenceNumber+1, ownHash,
                previousHashChain, previousHashSender, publicKey+"2");
        bc.revokeBlock(blc2);
        List<Block> list = new ArrayList<>();
        list.add(_block);
        assertEquals(list, bc.getBlocks(owner));
    }

}