package nl.tudelft.b_b_w.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Class to read the private and/ or public keys from the file
 */
public class KeyReader {

    /**
     * Class variables
     */
    private FileInputStream fileInputStream;

    /**
     * Constructor method
     */
    public KeyReader() {}

    /**
     * initialize method
     * Initializes the FileInputStream
     *
     * @param filePath given file path
     */
    private void initialize(String filePath) {
        try {
            this.fileInputStream = new FileInputStream(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initalize path: " + filePath);
        }
    }

    /**
     * readKey method
     * Reads the key from the given filepath
     *
     * @param path given filepath
     * @return byte array containing the read key
     */
    final byte[] readKey(String path) {
        try {
            initialize(path);
            File file = new File(path);
            byte[] encodedKey = new byte[(int) file.length()];
            int read = this.fileInputStream.read(encodedKey);
            this.fileInputStream.close();

            if (read <= 0) {
                throw new RuntimeException("File is empty: " + path);
            }

            return encodedKey;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from file: " + path);
        }
    }

    /**
     * readPrivateKey method
     * Reads the private key from the file
     *
     * @return private key
     */
    public final EdDSAPrivateKey readPrivateKey() throws InvalidKeySpecException{
        final String privateKeyPath = "private.key";
        final byte[] encodedPrivateKey = readKey(privateKeyPath);
        return convertToPrivateKey(encodedPrivateKey);
    }

    /**
     * readPublicKey method
     * Reads the public key from the file
     *
     * @return public key
     */
    public final EdDSAPublicKey readPublicKey() throws InvalidKeySpecException{
        final String publicKeyPath = "public.key";
        final byte[] encodedPublicKey = readKey(publicKeyPath);
        return convertToPublicKey(encodedPublicKey);
    }

    /**
     * convertToPrivateKey method
     * Converts the private key byte array to a private key
     *
     * @param encodedPrivateKey given byte array that represents the private key
     * @return EdDSAPrivateKey private key
     * @throws InvalidKeySpecException when the KeySpec is not valid
     */
    private EdDSAPrivateKey convertToPrivateKey(byte[] encodedPrivateKey) throws InvalidKeySpecException {
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
    private EdDSAPublicKey convertToPublicKey(byte[] encodedPublicKey) throws InvalidKeySpecException {
        X509EncodedKeySpec encoded = new X509EncodedKeySpec(encodedPublicKey);
        return new EdDSAPublicKey(encoded);
    }
}
