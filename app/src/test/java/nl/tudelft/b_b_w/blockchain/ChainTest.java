package nl.tudelft.b_b_w.blockchain;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import nl.tudelft.b_b_w.controller.ED25519;

import static junit.framework.Assert.assertEquals;

/**
 * Test class for the Chain class
 */
public class ChainTest {

    /**
     * Properties of the owner of the chain
     */
    private User owner;
    private String ownerName;
    private String ownerIban;
    private EdDSAPublicKey ownerPublicKey;
    private ArrayList<Block> testChain;

    //chain object to be tested
    private Chain chain;

    /**
     * Setting up before testing
     */
    @Before
    public void setUpChain() {
        //setting up owner
        ownerName = "BlockOwner1";
        ownerIban = "Owner1Iban";
        //object to generate public key
        EdDSAPrivateKey edDSAPrivateKey1 =
                ED25519.generatePrivateKey(Utils.hexToBytes("0000000000000000000000000000000000000000000000000000000000000000"));
        ownerPublicKey = ED25519.getPublicKey(edDSAPrivateKey1);
        owner = new User(ownerName, ownerIban, ownerPublicKey);
        testChain = new ArrayList<Block>();
        chain = new Chain(owner);

    }

    /**
     * getChainOwner() getter method testing
     */
    @Test
    public void getChainOwner() {
        assertEquals(owner, chain.getChainOwner());
    }

    /**
     * getChainList() getter method testing
     */
    @Test
    public void getChainList() {
        assertEquals(testChain, chain.getChainList());
    }

    /**
     * getChainList() setter method testing
     */
    @Test
    public void setChainList() {
        testChain.add(new Block(owner));
        chain.setChainList(testChain);
        assertEquals(testChain, chain.getChainList());
    }

}