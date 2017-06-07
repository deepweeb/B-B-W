package nl.tudelft.b_b_w.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.charset.Charset;
import java.security.SignatureException;

import static junit.framework.Assert.assertTrue;

/**
 * Test class for ED25519
 */
public class ED25519UnitTest {

    /**
     * Class attributes
     */
    private static final byte[] TEST_SEED = Utils.hexToBytes("0000000000000000000000000000000000000000000000000000000000000000");
    private static final byte[] TEST_PRIVATE_KEY = Utils.hexToBytes("3b6a27bcceb6a42d62a3a8d02a6f0d73653215771de243a63ac048a18b59da29");
    private static final byte[] TEST_PUBLIC_KEY = Utils.hexToBytes("302a300506032b65700321003b6a27bcceb6a42d62a3a8d02a6f0d73653215771de243a63ac048a18b59da29");
    private static final byte[] TEST_MESSAGE = "This is a secret message".getBytes(Charset.forName("UTF-8"));
    private static final byte[] TEST_MESSAGE_SIGNATURE = Utils.hexToBytes("94825896c7075c31bcb81f06dba2bdcd9dcf16e79288d4b9f87c248215c8468d475f429f3de3b4a2cf67fe17077ae19686020364d6d4fa7a0174bab4a123ba0f");

    /**
     * Initialize rule to catch the thrown exception
     */
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Test to check whether generating a private key works
     */
    @Test
    public void testGeneratePrivateKey() {
        EdDSAPrivateKey edDSAPrivateKey = ED25519.generatePrivateKey(TEST_SEED);
        Assert.assertArrayEquals(TEST_PRIVATE_KEY, edDSAPrivateKey.getAbyte());
    }

    /**
     * Test to check whether generating a private key works with no parameters
     */
    @Test
    public void testGeneratePrivateKeyNoParameters() {
        Assert.assertNotEquals(ED25519.generatePrivateKey(), ED25519.generatePrivateKey(TEST_SEED));
    }

    /**
     * Test to check whether getting a public key from a private key works
     */
    @Test
    public void testGetPublicKey() {
        EdDSAPrivateKey edDSAPrivateKey = ED25519.generatePrivateKey(TEST_SEED);
        EdDSAPublicKey edDSAPublicKey = ED25519.getPublicKey(edDSAPrivateKey);
        Assert.assertArrayEquals(TEST_PUBLIC_KEY, edDSAPublicKey.getEncoded());
    }

    /**
     * Test to check whether generating a signature from a message and private key works
     */
    @Test
    public void testGenerateSignature() {
        EdDSAPrivateKey edDSAPrivateKey = ED25519.generatePrivateKey(TEST_SEED);
        Assert.assertArrayEquals(TEST_MESSAGE_SIGNATURE, ED25519.generateSignature(TEST_MESSAGE, edDSAPrivateKey));
    }

    /**
     * Test to check whether generating a signature from a message and private key works
     * Forces a SignatureException
     */
    @Test
    public void testGenerateSignatureException() throws SignatureException {
        EdDSAPrivateKey edDSAPrivateKey = ED25519.generatePrivateKey(TEST_SEED);
        exception.expect(SignatureException.class);
        exception.expectMessage("signature length is wrong");
        ED25519.generateSignature(new byte[]{0}, edDSAPrivateKey);
    }

    /**
     * Test to check whether verifying a signature works
     */
    @Test
    public void testVerifySignature() {
        EdDSAPrivateKey edDSAPrivateKey = ED25519.generatePrivateKey(TEST_SEED);
        EdDSAPublicKey edDSAPublicKey = ED25519.getPublicKey(edDSAPrivateKey);
        final byte[] generatedSignature = ED25519.generateSignature(TEST_MESSAGE, edDSAPrivateKey);
        assertTrue(ED25519.verifySignature(generatedSignature, TEST_MESSAGE, edDSAPublicKey));
    }

    /**
     * Test to check whether verifying a signature works
     * Forces a SignatureException
     */
    @Test
    public void testVerifySignatureException() throws SignatureException {
        EdDSAPrivateKey edDSAPrivateKey = ED25519.generatePrivateKey(TEST_SEED);
        EdDSAPublicKey edDSAPublicKey = ED25519.getPublicKey(edDSAPrivateKey);
        exception.expect(SignatureException.class);
        exception.expectMessage("signature length is wrong");
        ED25519.verifySignature(new byte[]{0}, TEST_MESSAGE, edDSAPublicKey);
    }

}