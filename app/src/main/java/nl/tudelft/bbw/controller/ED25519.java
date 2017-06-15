package nl.tudelft.bbw.controller;

import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Class to generate a public and private key pair using the ED25519 protocol
 */
public final class ED25519 {

    /**
     * The amount of bytes the seed should have
     */
    private static final int SEED_LENGTH = 32;
    /**
     * Specification of the protocol
     */
    private static EdDSAParameterSpec specification = EdDSANamedCurveTable.getByName("Ed25519");


    /**
     * Private constructor
     * Ensures that the class cannot be instantiated
     */
    private ED25519() {

    }

    /**
     * Generates a new private key
     *
     * @return EdDSAPrivateKey object, which represents the private key
     */
    public static EdDSAPrivateKey generatePrivateKey() {
        final byte[] seed = new SecureRandom().generateSeed(SEED_LENGTH);
        return generatePrivateKey(seed);
    }

    /**
     * Generates a new private key
     *
     * @param seed Given seed byte array
     * @return EdDSAPrivateKey object, which represents the private key
     */
    public static EdDSAPrivateKey generatePrivateKey(byte[] seed) {
        EdDSAPrivateKeySpec privateKeySpec = new EdDSAPrivateKeySpec(seed, specification);
        return new EdDSAPrivateKey(privateKeySpec);
    }

    /**
     * Gets the public key of the given private key
     *
     * @param privateKey the private key of which to get the public key from
     * @return EdDSAPublicKey object, which represents the public key
     */
    public static EdDSAPublicKey getPublicKey(EdDSAPrivateKey privateKey) {
        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(privateKey.getAbyte(), specification);
        return new EdDSAPublicKey(pubKey);
    }

    /**
     * Generates a new signature
     *
     * @param message    message to encrypt
     * @param privateKey private key to use in the encryption of the message
     * @return the generated signature
     */
    public static byte[] generateSignature(byte[] message, EdDSAPrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Signature signature = new EdDSAEngine(MessageDigest.getInstance(specification.getHashAlgorithm()));
        signature.initSign(privateKey);
        signature.update(message);
        return signature.sign();
    }

    /**
     * Verifies the given signature
     *
     * @param signatureBytes the given signature byte array
     * @param message        the given message
     * @param publicKey      the public key to decrypt the signature from
     * @return boolean whether the decrypted signature is the same as the message
     */
    public static boolean verifySignature(byte[] signatureBytes, byte[] message, EdDSAPublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Signature signature = new EdDSAEngine(MessageDigest.getInstance(specification.getHashAlgorithm()));
        signature.initVerify(publicKey);
        signature.update(message);
        return signature.verify(signatureBytes);
    }

}
