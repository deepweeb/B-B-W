package nl.tudelft.b_b_w.controller;

import org.encryptor4j.util.FileEncryptor;
import org.encryptor4j.util.TextEncryptor;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;


/**
 * CryptoController class
 * Encrypts and decrypts Strings using the AES-CBC protocol
 */

public class CryptoController {

    /**
     * Class attributes
     *
     * textEncryptor is the text encryptor uses AES encryption in CBC mode
     * fileEncryptor is the file encryptor uses AES encryption in CTR mode
     *
     * with a maximum permitted key length of 256bit.
     */
    private TextEncryptor textEncryptor;
    private FileEncryptor fileEncryptor;
    private static final String ENCRYPTION_EXTENSION = ".enc";

    /**
     * Constructor method
     * Initializes the CryptoController by initializing the textEncryptor with the secret key
     */
    public CryptoController(String password) {
        this.textEncryptor = new TextEncryptor(password);
        this.fileEncryptor = new FileEncryptor(password);
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

    /**
     * encryptFile method
     *
     * @param path given filepath to encrypt
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    final void encryptFile(String path) {
        try {
            fileEncryptor.encrypt(new File(path + KeyWriter.TEMP_EXTENSION),
                    new File(path + ENCRYPTION_EXTENSION));
            new File(path + KeyWriter.TEMP_EXTENSION).delete();
        } catch (GeneralSecurityException| IOException e) {
            throw new RuntimeException("Error - Could not encrypt file: " + path);
        }
    }

    /**
     * decryptFile method
     *
     * @param path given filepath to decrypt
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    final void decryptFile(String path) {
        try {
            fileEncryptor.decrypt(new File(path + ENCRYPTION_EXTENSION), new File(path));
            new File(path + ENCRYPTION_EXTENSION).delete();
        } catch (GeneralSecurityException| IOException e) {
            throw new RuntimeException("Error - Could not decrypt file: " + path);
        }
    }




}
