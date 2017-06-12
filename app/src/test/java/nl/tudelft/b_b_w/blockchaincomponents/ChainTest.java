package nl.tudelft.b_b_w.blockchaincomponents;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.b_b_w.controller.ED25519;

import static junit.framework.Assert.assertEquals;

/**
 * Created by NOSLEEP on 12-Jun-17.
 */
public class ChainTest {

    /**
     * Properties of the owner of the chain
     */
    private User owner;
    private String ownerName;
    private String ownerIban;
    private EdDSAPublicKey ownerPublicKey;

    //chain object to be tested
    private Chain chain;

    @Before
    public void setUpBlock() throws Exception {

        //setting up owner
        ownerName = "BlockOwner1";
        ownerIban = "Owner1Iban";
        //object to generate public key
        EdDSAPrivateKey edDSAPrivateKey1 =
                ED25519.generatePrivateKey(Utils.hexToBytes("0000000000000000000000000000000000000000000000000000000000000000"));
        ownerPublicKey = ED25519.getPublicKey(edDSAPrivateKey1);
        owner = new User(ownerName, ownerIban, ownerPublicKey);

        chain = new Chain(owner);

    }

    @Test
    public void getChainOwner() throws Exception {
        assertEquals(owner, chain.getChainOwner());
    }


}