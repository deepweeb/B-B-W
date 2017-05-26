package nl.tudelft.b_b_w.controller;


import org.encryptor4j.util.TextEncryptor;

import java.security.GeneralSecurityException;

/**
 * CryptoController class
 * Encrypts and decrypts Strings using the AES-CBC protocol
 */

public class CryptoController {

    /**
     * Class attributes
     * @param SECRET_KEY The key of the encryption
     * @param textEncryptor The encryptor which uses AES Encryption in CBC mode with a maximum
     *                      permitted key length of 256bit.
     */
    private final String SECRET_KEY = "k7JKPUDoGhTSo4by";
    private TextEncryptor textEncryptor;

    /**
     * Constructor method
     * Initializes the CryptoController by initializing the textEncryptor with the secret key
     */
    public CryptoController() {
        this.textEncryptor = new TextEncryptor(SECRET_KEY);
    }

    /**
     * encryptString method
     * @param data given data
     * @return encryption of data
     * @throws GeneralSecurityException if string is larger than 256 bit
     */
    public final String encryptString(String data) {
        try {
            return textEncryptor.encrypt(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Error - Could not encode string: " + e);
        }
    }

    /**
     * decryptString method
     * @param data given encrypted data
     * @return decrypted data
     * @throws GeneralSecurityException if string is larger than 256 bit
     */
    public final String decryptString(String data) {
        try {
            return textEncryptor.decrypt(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Error - Could not decode string: " + e);
        }
    }
}
