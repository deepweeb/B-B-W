package nl.tudelft.bbw.blockchain;

import org.junit.Before;
import org.junit.Test;

<<<<<<< HEAD:app/src/test/java/nl/tudelft/bbw/blockchain/BlockDataTest.java
=======
import nl.tudelft.bbw.model.TrustValues;

>>>>>>> bf1d337508dc2ec241eb12f2ad6e85c78aea115d:app/src/test/java/nl/tudelft/bbw/blockchain/BlockDataTest.java
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
        sequenceNumber = 1;
        previousHashChain = new Hash("ExampleHash1");
        previousHashSender = new Hash("ExampleHash2");
        trustValue = TrustValues.INITIALIZED.getValue();

        blockData = new BlockData(blockType, sequenceNumber, previousHashChain, previousHashSender, trustValue);
    }

    /**
     * getBlockType() getter method testing
     */
    @Test
    public void getBlockTypeTest() {
        assertEquals(blockType, blockData.getBlockType());
    }

    /**
     * getSequenceNumber() getter method testing
     */
    @Test
    public void getSequenceNumberTest() {
        assertEquals(sequenceNumber, blockData.getSequenceNumber());
    }

    /**
     * getPreviousHashChain() getter method testing
     */
    @Test
    public void getPreviousHashChainTest() {
        assertEquals(previousHashChain, blockData.getPreviousHashChain());
    }

    /**
     * getPreviousHashSender() getter method testing
     */
    @Test
    public void getPreviousHashSenderTest() {
        assertEquals(previousHashSender, blockData.getPreviousHashSender());
    }

    /**
     * getTrustValue() getter method testing
     */
    @Test
    public void getTrustValueTest() {
        assertEquals(trustValue, blockData.getTrustValue());
    }

    /**
     * setTrustValue() setter method testing
     */
    @Test
    public void setTrustValueTest() {
        final double testTrustValue = 99;
        blockData.setTrustValue(testTrustValue);
        assertEquals(testTrustValue, blockData.getTrustValue());
    }
}