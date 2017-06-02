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

    private Block testBlock;
    private BlockController blockController;
    private final String typeBlock = "BLOCK";
    private final String owner = "owner";
    private final String ownHash = "ownHash";
    private final String previousHashChain = "N/A";
    private final String previousHashSender = "N/A";
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
        testBlock = BlockFactory.getBlock(
                typeBlock,
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
                typeBlock,
                owner,
                blockController.getLatestSeqNumber(owner)+1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );

        assertEquals(testBlock, newBlock);
    }

    /**
     * Tests whether the creation through a block factory works
     * With a normal block
     */
    @Test
    public void testGetRevokeBlock()throws HashException{

        final Block newBlock = BlockFactory.getBlock(
                typeBlock,
                owner,
                blockController.getLatestSeqNumber(owner)+1,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );

        assertEquals(testBlock, newBlock);
    }

    /**
     * Tests whether the creation through a block factory works
     * With an empty type
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetBlockEmpty() throws HashException {
        BlockFactory.getBlock(
                "",
                owner,
                2,
                ownHash,
                previousHashChain,
                previousHashSender,
                publicKey,
                iban,
                trustValue
        );
    }

}
