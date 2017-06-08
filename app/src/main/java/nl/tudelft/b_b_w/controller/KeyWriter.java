package nl.tudelft.b_b_w.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Class to write the private and/ or keys to the file
 */
public class KeyWriter {

    /**
     * Class variables
     */
    private FileOutputStream fileOutputStream;

    /**
     * Constructor method
     */
    public KeyWriter() {}

    /**
     * initialize method
     * Initializes the FileOutputStream
     *
     * @param filePath given file path
     */
    private void initialize(String filePath) {
        try {
            this.fileOutputStream = new FileOutputStream(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initalize path: " + filePath);
        }
    }

    /**
     * writeKey method
     * Writes the key to the given filepath
     *
     * @param path       given filepath
     * @param encodedKey given byte array containing the key
     */
    final void writeKey(String path, byte[] encodedKey) {
        try {
            initialize(path);
            this.fileOutputStream.write(encodedKey);
            this.fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + path);
        }
    }

    /**
     * writePrivateKey method
     * Writes the private key to the given filepath
     *
     * @param privateKey the private key to write away
     */
    final void writePrivateKey(EdDSAPrivateKey privateKey) {
        final String privateKeyPath = "private.key";

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                privateKey.getEncoded());
        writeKey(privateKeyPath, pkcs8EncodedKeySpec.getEncoded());
    }

    /**
     * writePublicKey method
     * Writes the public key to the given filepath
     *
     * @param publicKey the public key to write away
     */
    final void writePublicKey(EdDSAPublicKey publicKey) {
        final String publicKeyPath = "public.key";

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                publicKey.getEncoded());
        writeKey(publicKeyPath, x509EncodedKeySpec.getEncoded());
    }

}
