package nl.tudelft.bbw.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for CryptoController
 */
public class CryptoControllerTest {

    /**
     * Class attributes
     */
    private CryptoController cryptoController;

    /**
     * setUp method
     * Initializes class variables
     */
    @Before
    public void setUp() {
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        EdDSAPublicKey publicKey = ED25519.getPublicKey(privateKey);
        final String name = "name";
        final String iban = "iban";
        this.cryptoController = new CryptoController(KeyWriter.publicKeyToString(publicKey));
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