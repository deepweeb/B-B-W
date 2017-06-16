package nl.tudelft.bbw.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Class to write the private and/ or keys to the file
 */
public final class KeyWriter {

    static final String TEMP_EXTENSION = ".bak";
    /**
     * Class variables
     */
    private static FileOutputStream fileOutputStream;
    public static String PATH_PRIVATE_KEY = "private.key";
    public static String PATH_PUBLIC_KEY = "public.key";

    /**
     * Empty constructor method
     * Ensures that the class cannot be instantiated
     */
    private KeyWriter() {
    }

    /**
     * initialize method
     * Initializes the FileOutputStream
     *
     * @param filePath given file path
     */
    private static void initialize(String filePath) throws IOException {
        fileOutputStream = new FileOutputStream(filePath);
    }

    /**
     * writeKey method
     * Writes the key to the given filepath
     *
     * @param path       given filepath
     * @param encodedKey given byte array containing the key
     */
    private static void writeKey(String path, byte[] encodedKey) throws IOException {
        initialize(path + TEMP_EXTENSION);
        fileOutputStream.write(encodedKey);
        fileOutputStream.close();
    }

    /**
     * writePrivateKey method
     * Writes the private key to the given filepath
     *
     * @param privateKey the private key to write away
     */
    public static void writePrivateKey(EdDSAPrivateKey privateKey) throws IOException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        writeKey(PATH_PRIVATE_KEY, pkcs8EncodedKeySpec.getEncoded());
        encryptFile(PATH_PRIVATE_KEY, ED25519.getPublicKey(privateKey));
    }

    /**
     * writePublicKey method
     * Writes the public key to the given filepath
     *
     * @param publicKey the public key to write away
     */
    public static void writePublicKey(EdDSAPublicKey publicKey) throws IOException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        writeKey(PATH_PUBLIC_KEY, x509EncodedKeySpec.getEncoded());
        encryptFile(PATH_PUBLIC_KEY, publicKey);
    }

    /**
     * publicKeyToString method
     * Writes the public key to a string
     *
     * @param publicKey the public key to write away
     * @return Hex representation of the byte array, which represents the public string
     */
    public static String publicKeyToString(EdDSAPublicKey publicKey) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        return Utils.bytesToHex(x509EncodedKeySpec.getEncoded());
    }

    /**
     * encryptFile method
     * Encrypts the file using the string representation of the public key as password
     *
     * @param path      given path to file
     * @param publicKey given public key to use as password
     */
    private static void encryptFile(String path, EdDSAPublicKey publicKey) {
        CryptoController cryptoController = new CryptoController(publicKeyToString(publicKey));
        cryptoController.encryptFile(path);
    }

}
