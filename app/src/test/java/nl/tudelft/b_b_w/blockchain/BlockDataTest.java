package nl.tudelft.b_b_w.blockchain;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * This class tests the functions of the BlockData object.
 */
public class BlockDataTest {

    /**
     * The BlockData attributes
     */
    private BlockData blockData;
    private BlockType blockType;
    private int sequenceNumber;
    private Hash previousHashChain;
    private Hash previousHashSender;
    private double trustValue;

    /**
     * Setting up the test object
     */
    @Before
    public void setUpBlockData(){

        blockType = BlockType.ADD_KEY;
        sequenceNumber = 5;
        previousHashChain = new Hash("ExampleHash1");
        previousHashSender = new Hash("ExampleHash2");
        trustValue = 1;

        blockData = new BlockData(blockType, sequenceNumber, previousHashChain, previousHashSender, trustValue);
    }

    /**
     * getBlockType() getter method testing
     * @throws Exception
     */
    @Test
    public void getBlockTypeTest() throws Exception {
        assertEquals(blockType, blockData.getBlockType());
    }

    /**
     * getSequenceNumber() getter method testing
     * @throws Exception
     */
    @Test
    public void getSequenceNumberTest() throws Exception {
        assertEquals(sequenceNumber, blockData.getSequenceNumber());
    }

    /**
     * getPreviousHashChain() getter method testing
     * @throws Exception
     */
    @Test
    public void getPreviousHashChainTest() throws Exception {
        assertEquals(previousHashChain, blockData.getPreviousHashChain());
    }

    /**
     * getPreviousHashSender() getter method testing
     * @throws Exception
     */
    @Test
    public void getPreviousHashSenderTest() throws Exception {
        assertEquals(previousHashSender, blockData.getPreviousHashSender());
    }

    /**
     * getTrustValue() getter method testing
     * @throws Exception
     */
    @Test
    public void getTrustValueTest() throws Exception {
        assertEquals(trustValue, blockData.getTrustValue());
    }


}