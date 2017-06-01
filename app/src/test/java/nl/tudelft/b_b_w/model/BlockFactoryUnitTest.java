package nl.tudelft.b_b_w.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.block.Block;
import nl.tudelft.b_b_w.model.block.BlockFactory;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,sdk= 21,  manifest = "src/main/AndroidManifest.xml")
public class BlockFactoryUnitTest {

    private Block _block;
    private BlockController blockController;
    private final String TYPE_BLOCK = "BLOCK";
    private final String owner = "owner";
    private final String ownHash = "ownHash";
    private final String previousHashChain = "previousHashChain";
    private final String previousHashSender = "previousHashSender";
    private final String publicKey = "publicKey";
    private final String iban = "iban";
    private final int trustValue = TrustValues.INITIALIZED.getValue();
    /**
     * This method runs before each test to initialize the test object
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Before
    public void makeNewBlock() throws Exception {
        blockController = new BlockController(RuntimeEnvironment.application);
        _block = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                blockController.getLatestSeqNumber(owner)+1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
    }

    /**
     * Tests whether the creation through a block factory works
     * With a normal block
     */
    @Test
    public void testGetBlock() throws HashException {
        final Block newBlock = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                blockController.getLatestSeqNumber(owner)+1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );

        assertEquals(_block, newBlock);
    }

    /**
     * Tests whether the creation through a block factory works
     * With a normal block
     */
    @Test
    public void testGetRevokeBlock()throws HashException{

        final Block newBlock = BlockFactory.getBlock(
                TYPE_BLOCK,
                owner,
                blockController.getLatestSeqNumber(owner)+1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );

        assertEquals(_block, newBlock);
    }

    /**
     * Tests whether the creation through a block factory works
     * With an empty type
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetBlockEmpty()throws HashException{
        BlockFactory.getBlock(
                "",
                owner,
                blockController.getLatestSeqNumber(owner)+1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
    }

    /**
     * Tests whether the creation through a block factory works
     * With a faulty type
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetBlockFaultyString()throws HashException{
        BlockFactory.getBlock(
                "block",
                owner,
                blockController.getLatestSeqNumber(owner)+1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
    }

}
