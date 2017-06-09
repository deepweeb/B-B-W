package nl.tudelft.b_b_w.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.b_b_w.model.User;

import static org.junit.Assert.*;

/**
 * Test class for CryptoController
 */
public class CryptoControllerTest {

    /**
     * Class attributes
     */
    private CryptoController cryptoController;
    private EdDSAPrivateKey privateKey;
    private EdDSAPublicKey publicKey;
    private static final byte[] TEST_SEED = Utils.hexToBytes("0000000000000000000000000000000000000000000000000000000000000000");
    private final String test = "Test string";

    /**
     * setUp method
     * Initializes class variables
     */
    @Before
    public void setUp() {
        this.privateKey = ED25519.generatePrivateKey(TEST_SEED);
        this.publicKey = ED25519.getPublicKey(this.privateKey);
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
        assertEquals(test, cryptoController.decryptString(cryptoController.encryptString(test)));
    }

}