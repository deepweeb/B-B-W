package nl.tudelft.b_b_w.controller;

import org.encryptor4j.util.TextEncryptor;

import java.security.GeneralSecurityException;

import nl.tudelft.b_b_w.blockchain.User;

/**
 * CryptoController class
 * Encrypts and decrypts Strings using the AES-CBC protocol
 */

public class CryptoController {

    /**
     * Class attributes
     *
     * @param textEncryptor The encryptor which uses AES Encryption in CBC mode with a maximum
     * permitted key length of 256bit.
     */
    private TextEncryptor textEncryptor;

    /**
     * Constructor method
     * Initializes the CryptoController by initializing the textEncryptor with the secret key
     */
    public CryptoController(User user) {
        this.textEncryptor = new TextEncryptor(KeyWriter.publicKeyToString(user.getPublicKey()));
    }

    /**
     * encryptString method
     *
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
     *
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
