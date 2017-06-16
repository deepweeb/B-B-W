package nl.tudelft.bbw.controller;

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

    private static final String ENCRYPTION_EXTENSION = ".enc";
    /**
     * Class attributes
     * <p>
     * textEncryptor is the text encryptor which uses AES encryption in CBC mode
     * fileEncryptor is the file encryptor which uses AES encryption in CTR mode
     * <p>
     * with a maximum permitted key length of 256bit.
     */
    private TextEncryptor textEncryptor;
    private FileEncryptor fileEncryptor;

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
     * encrypts the string using the AES encryption in CBC mode
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
     * decrypts the string using the AES encryption in CBC mode
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
     * encrypts the string using the AES encryption in CTR mode
     *
     * @param path given filepath to encrypt
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    final void encryptFile(String path) {
        try {
            File tempFile = new File(path + KeyWriter.TEMP_EXTENSION);
            fileEncryptor.encrypt(tempFile,
                    new File(path + ENCRYPTION_EXTENSION));
            tempFile.delete();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error - Could not encrypt file: " + path);
        }
    }

    /**
     * decryptFile method
     * decrypts the string using the AES encryption in CTR mode
     *
     * @param path given filepath to decrypt
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    final void decryptFile(String path) {
        try {
            File tempFile = new File(path + ENCRYPTION_EXTENSION);
            fileEncryptor.decrypt(tempFile, new File(path));
            tempFile.delete();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error - Could not decrypt file: " + path);
        }
    }


}
