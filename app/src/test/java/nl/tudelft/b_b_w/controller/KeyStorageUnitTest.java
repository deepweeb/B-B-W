package nl.tudelft.b_b_w.controller;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;

/**
 * Test class for KeyWriter and KeyReader
 */
public class KeyStorageUnitTest {

    /**
     * Initialize rule to catch the thrown exception
     */
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    /**
     * Class attributes
     */
    private EdDSAPublicKey edDSAPublicKey;
    private EdDSAPrivateKey edDSAPrivateKey;

    /**
     * Instantiate the KeyReader and KeyWriter
     */
    @Before
    public void initialize() {
        this.edDSAPrivateKey = ED25519.generatePrivateKey();
        this.edDSAPublicKey = ED25519.getPublicKey(this.edDSAPrivateKey);
    }

    /**
     * Tests whether writing and reading the private key works
     */
    @Test
    public void testPrivateKey() throws IOException {
        KeyWriter.writePrivateKey(this.edDSAPrivateKey);
        try {
            assertEquals(this.edDSAPrivateKey, KeyReader.readPrivateKey());
        } catch (InvalidKeySpecException e) {
            fail();
        }
    }

    /**
     * Tests whether writing and reading the public key works
     */
    @Test
    public void testPublicKey() throws IOException {
        KeyWriter.writePublicKey(this.edDSAPublicKey);
        try {
            assertEquals(this.edDSAPublicKey, KeyReader.readPublicKey());
        } catch (InvalidKeySpecException e) {
            fail();
        }
    }

    /**
     * Test to force an Exception upon reading a key
     */
    @Test
    public void testReadKeyException() throws IOException {
        exception.expect(IOException.class);
        exception.expectMessage("Failed to initalize path: ");
        KeyReader.readKey("");
    }

    /**
     * Test to force an Exception upon reading a key of an empty file
     */
    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void testReadKeyEmpty() throws IOException {
        final String empty = "empty";
        exception.expect(IOException.class);
        exception.expectMessage("File is empty: " + empty);

        new File(empty).createNewFile();
        KeyReader.readKey(empty);
    }

    /**
     * Test to check whether converting a public key to a string and back works
     */
    @Test
    public void testStringPublicKey() {
        try {
            EdDSAPublicKey edDSAPublicKey1 = KeyReader.readPublicKey(
                    KeyWriter.publicKeyToString(edDSAPublicKey));
            assertEquals(edDSAPublicKey, edDSAPublicKey1);
        } catch (InvalidKeySpecException e) {
            fail();
        }
    }

    /**
     * Remove test key files
     */
    @After
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void delete() {
        new File("private.key").delete();
        new File("public.key").delete();
        new File("empty").delete();
    }

}