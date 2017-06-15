package nl.tudelft.b_b_w.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Class to write the private and/ or keys to the file
 */
public final class KeyWriter {

    /**
     * Class variables
     */
    private static FileOutputStream fileOutputStream;

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
        initialize(path);
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
        final String privateKeyPath = "private.key";

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        writeKey(privateKeyPath, pkcs8EncodedKeySpec.getEncoded());
    }

    /**
     * writePublicKey method
     * Writes the public key to the given filepath
     *
     * @param publicKey the public key to write away
     */
    public static void writePublicKey(EdDSAPublicKey publicKey) throws IOException {
        final String publicKeyPath = "public.key";

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        writeKey(publicKeyPath, x509EncodedKeySpec.getEncoded());
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
     * writeToKeyStore method
     * Writes the private key to the android keystore
     *
     * @param privateKey given private key
     * @param owner      given owner of the private key
     * @throws KeyStoreException        if there occurs an error in the keystore
     * @throws IOException              if there occurs an error in the input stream
     * @throws CertificateException     if there occurs an error in the creation of the certificate
     * @throws NoSuchAlgorithmException If the given algorithm is invalid, this case 'X.509'
     */
    public static void writeToKeyStore(EdDSAPrivateKey privateKey, String owner) throws KeyStoreException,
            IOException, CertificateException, NoSuchAlgorithmException {
        Certificate certificate = generateCertificate(privateKey);
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        keyStore.setKeyEntry(owner, privateKey, null, new Certificate[]{certificate});
    }

    private static X509Certificate generateCertificate(EdDSAPrivateKey privateKey)
            throws CertificateException {

        CertificateFactory fac = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream in = new ByteArrayInputStream(privateKey.getEncoded());
        return (X509Certificate) fac.generateCertificate(in);

    }

}
