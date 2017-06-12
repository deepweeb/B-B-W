package nl.tudelft.b_b_w.blockchaincomponents;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.b_b_w.controller.ED25519;

/**
 * Created by NOSLEEP on 08-Jun-17.
 */
public class AcquaintanceTest {

    /**
     * The user attributes
     */
    private String name;
    private String iban;
    private EdDSAPublicKey publicKey;
    private User user;
    private Acquaintance acquaintance;

    /**
     * Setting up the test object
     */
    @Before
    public void setUpUser(){
        name = "testName";
        iban = "NL642335674446";
        EdDSAPrivateKey edDSAPrivateKey = ED25519.generatePrivateKey(Utils.hexToBytes("0000000000000000000000000000000000000000000000000000000000000000"));
        publicKey = ED25519.getPublicKey(edDSAPrivateKey);
        user = new User(name, iban, publicKey);
        private User user;
    }

    @Test
    public void getUser() throws Exception {

    }

    @Test
    public void getMultichain() throws Exception {

    }

}