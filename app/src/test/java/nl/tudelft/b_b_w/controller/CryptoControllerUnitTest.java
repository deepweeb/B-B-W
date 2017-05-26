package nl.tudelft.b_b_w.controller;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
public class CryptoControllerUnitTest {

    private CryptoController cryptoController;

    /**
     * initialize method
     * Initialized the CryptoController
     */
    @Before
    public void initialize() {
        this.cryptoController = new CryptoController();
    }

    /**
     * EncryptDecryptTest
     * Checks whether the encryption and decryption method work
     */
    @Test
    public void EncryptDecryptTest() throws Exception {
        String random = "0CYk67Pt08m7DE9Bumbo";
        assertEquals(random, this.cryptoController.decryptString(this.cryptoController.encryptString(random)));
    }
}