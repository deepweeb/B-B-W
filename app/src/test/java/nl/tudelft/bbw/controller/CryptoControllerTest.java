package nl.tudelft.bbw.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.bbw.model.User;

import static org.junit.Assert.assertEquals;

/**
 * Test class for CryptoController
 */
public class CryptoControllerTest {

    /**
     * Class attributes
     */
    private CryptoController cryptoController;
    private static final byte[] TEST_SEED = Utils.hexToBytes("0000000000000000000000000000000000000000000000000000000000000000");

    /**
     * setUp method
     * Initializes class variables
     */
    @Before
    public void setUp() {
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey(TEST_SEED);
        EdDSAPublicKey publicKey = ED25519.getPublicKey(privateKey);
        final String name = "name";
        final String iban = "iban";
        this.cryptoController = new CryptoController(new User(name, iban, publicKey));
    }

    /**
     * endecryptString method
     * Checks whether the encryption and decryption of a string work
     */
    @Test
    public void endecryptString() {
        final String test = "Test string";
        assertEquals(test, cryptoController.decryptString(cryptoController.encryptString(test)));
    }

}