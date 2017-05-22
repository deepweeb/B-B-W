package nl.tudelft.b_b_w.ModelsUnitTest;

import org.junit.Before;
import org.junit.Test;
import nl.tudelft.b_b_w.model.Block;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BlockUnitTest {

    private Block _block;
    private final String owner = "owner";
    private final int sequenceNumber = 0;
    private final String ownHash = "ownHash";
    private final String previousHashChain = "previousHashChain";
    private final String previousHashSender = "previousHashSender";
    private final String publicKey = "publicKey";
    private final String iban = "iban";
    private final boolean isRevoked = false;

    /**
     * This method runs before each test to initialize the test object
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Before
    public void makeNewBlock() throws Exception {
        _block = new Block(owner, sequenceNumber, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
    }

    /**
     * Test to check whether the getOwner() method returns the owner name
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
    public void getOwnerTest() throws Exception {
        final String check = "owner";
        assertEquals(check, _block.getOwner());
    }


    /**
     * Test to check whether the getOwnHash() method returns the right hash
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
    public void getOwnHashTest() throws Exception {
        final String check = "ownHash";
        assertEquals(check, _block.getOwnHash());
    }

    /**
     * Test to check whether the getPreviousHashChain() returns the hash of the previous block in the chain
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
    public void getPreviousHashChainTest() throws Exception {
        final String check = "previousHashChain";
        assertEquals(check, _block.getPreviousHashChain());
    }

    /**
     * Test to check whether the getPreviousHashSender() returns the hash of the previous block of the sender
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
    public void getPreviousHashSenderTest() throws Exception {
        final String check = "previousHashSender";
        assertEquals(check, _block.getPreviousHashSender());
    }

    /**
     * Test to check whether the getSequence_number() returns the right sequence number of the block
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
    public void getSequenceNumberTest() throws Exception {
        final int check = 0;
        assertEquals(check, _block.getSequenceNumber());
    }

    /**
     * Test to check whether the getPublic_key() function returns the right public key of the contact of the block
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
    public void getPublicKeyTest() throws Exception {
        final String check = "publicKey";
        assertEquals(check, _block.getPublicKey());
    }


    /**
     * Test to check whether the getIban() function returns the right public key of the contact of the block
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
    public void getIbanTest() throws Exception {
        final String check = "iban";
        assertEquals(check, _block.getIban());
    }

    /**
     * Test to check whether the isRevoked() method returns the right boolean value indicating if the block is revoked or not.
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
    public void isRevokedTest() throws Exception {
        assertFalse(_block.isRevoked());
    }

    /**
     * Test to check whether the equals() method returns the right boolean value indicating if this block is equal to the parameter block.
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
    public void equalsTest() throws Exception {
        Block check = new Block(owner, sequenceNumber, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
        assertTrue(_block.equals(check));
    }

    /**
     * Test to check whether the equals() method returns the right boolean value indicating if this block is equal to the parameter block.
     * Forces a false
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
    public void equalsFalseTest() throws Exception {
        final String _owner = "NOTOWNER";
        Block check = new Block(_owner, sequenceNumber, ownHash, previousHashChain, previousHashSender, publicKey,iban, isRevoked);
        assertFalse(_block.equals(check));
    }

    /**
     * Test whether the toString method is correct
     */
    @Test
    public void toStringTest() {
        String result = "Block{" +
                "owner='" + owner + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", ownHash='" + ownHash + '\'' +
                ", previousHashChain='" + previousHashChain + '\'' +
                ", previousHashSender='" + previousHashSender + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", iban='" + iban + '\'' +
                ", isRevoked=" + isRevoked +
                '}';
        assertEquals(result, _block.toString());
    }

    /**
     * Testing whether the hashcode method yields the same
     * output with the same input
     */
    @Test
    public void testHashCode() {
        Block x = new Block("owner2", sequenceNumber+1, ownHash, previousHashChain, previousHashSender, "pub2",iban, false);
        Block y = new Block("owner2", sequenceNumber+1, ownHash, previousHashChain, previousHashSender, "pub2",iban, false);
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }



}