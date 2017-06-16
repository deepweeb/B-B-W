package nl.tudelft.bbw.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Class to read the private and/ or public keys from the file
 */
public final class KeyReader {

    /**
     * Class variables
     */
    private static FileInputStream fileInputStream;

    /**
     * Empty constructor method
     * Ensures that the Class cannot be instantiated
     */
    private KeyReader() {
    }

    /**
     * initialize method
     * Initializes the FileInputStream
     *
     * @param filePath given file path
     */
    private static void initialize(String filePath) throws IOException {
        try {
            fileInputStream = new FileInputStream(filePath);
        } catch (IOException e) {
            throw new IOException("Failed to initalize path: " + filePath);
        }
    }

    /**
     * readKey method
     * Reads the key from the given filepath
     *
     * @param path given filepath
     * @return byte array containing the read key
     */
    public static byte[] readKey(String path, String password) throws IOException {
        decryptFile(path, password);
        initialize(path);
        File file = new File(path);
        byte[] encodedKey = new byte[(int) file.length()];
        int read = fileInputStream.read(encodedKey);

        if (read <= 0) {
            throw new IOException("File is empty: " + path);
        }

        fileInputStream.close();
        return encodedKey;
    }

    /**
     * readPrivateKey method
     * Reads the private key from the file
     *
     * @return private key
     */
    public static EdDSAPrivateKey readPrivateKey(String password) throws InvalidKeySpecException, IOException {
        final byte[] encodedPrivateKey = readKey(KeyWriter.PATH_PRIVATE_KEY, password);
        return convertToPrivateKey(encodedPrivateKey);
    }

    /**
     * readPublicKey method
     * Reads the public key from the file
     *
     * @return public key
     */
    public static EdDSAPublicKey readPublicKey(String password) throws InvalidKeySpecException, IOException {
        final byte[] encodedPublicKey = readKey(KeyWriter.PATH_PUBLIC_KEY, password);
        return convertToPublicKey(encodedPublicKey);
    }

    /**
     * StringPublicKey method
     * Reads the public key from the given string
     *
     * @param encodedPublicKey given hex representation of the byte array of the public key
     * @return public key
     */
    public static EdDSAPublicKey stringToPublicKey(String encodedPublicKey) throws InvalidKeySpecException {
        return convertToPublicKey(Utils.hexToBytes(encodedPublicKey));
    }

    /**
     * convertToPrivateKey method
     * Converts the private key byte array to a private key
     *
     * @param encodedPrivateKey given byte array that represents the private key
     * @return EdDSAPrivateKey private key
     * @throws InvalidKeySpecException when the KeySpec is not valid
     */
    private static EdDSAPrivateKey convertToPrivateKey(byte[] encodedPrivateKey) throws InvalidKeySpecException {
        PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec(encodedPrivateKey);
        return new EdDSAPrivateKey(encoded);
    }

    /**
     * convertToPublicKey method
     * Converts the public key byte array to a public key
     *
     * @param encodedPublicKey given byte array that represents the public key
     * @return EdDSAPublicKey public key
     * @throws InvalidKeySpecException when the KeySpec is not valid
     */
    private static EdDSAPublicKey convertToPublicKey(byte[] encodedPublicKey) throws InvalidKeySpecException {
        X509EncodedKeySpec encoded = new X509EncodedKeySpec(encodedPublicKey);
        return new EdDSAPublicKey(encoded);
    }

    /**
     * decryptFile method
     * Decrypts the file using the string representation of the public key as password
     *
     * @param path     given path to file
     * @param password given password
     */
    private static void decryptFile(String path, String password) {
        CryptoController cryptoController = new CryptoController(password);
        cryptoController.decryptFile(path);
    }
}
