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
     * Class attributes
     */
    private KeyReader keyReader;
    private KeyWriter keyWriter;
    private EdDSAPublicKey edDSAPublicKey;
    private EdDSAPrivateKey edDSAPrivateKey;

    /**
     * Initialize rule to catch the thrown exception
     */
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Instantiate the KeyReader and KeyWriter
     */
    @Before
    public void initialize() {
        this.keyReader = new KeyReader();
        this.keyWriter = new KeyWriter();
        this.edDSAPrivateKey = ED25519.generatePrivateKey();
        this.edDSAPublicKey = ED25519.getPublicKey(this.edDSAPrivateKey);
    }

    /**
     * Tests whether writing and reading the private key works
     */
    @Test
    public void testPrivateKey() throws IOException {
        keyWriter.writePrivateKey(this.edDSAPrivateKey);
        try {
            assertEquals(this.edDSAPrivateKey, keyReader.readPrivateKey());
        } catch (InvalidKeySpecException e) {
            fail();
        }
    }

    /**
     * Tests whether writing and reading the public key works
     */
    @Test
    public void testPublicKey() throws IOException {
        keyWriter.writePublicKey(this.edDSAPublicKey);
        try {
            assertEquals(this.edDSAPublicKey, keyReader.readPublicKey());
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
        keyReader.readKey("");
    }

    /**
     * Test to force an Exception upon reading a key of an empty file
     */
    @Test
    public void testReadKeyEmpty() throws IOException {
        final String empty = "empty";
        exception.expect(IOException.class);
        exception.expectMessage("File is empty: " + empty);

        new File(empty).createNewFile();
        keyReader.readKey(empty);
    }

    /**
     * Test to check whether converting a public key to a string and back works
     */
    @Test
    public void testStringPublicKey() {
        try {
            EdDSAPublicKey edDSAPublicKey1 = keyReader.readPublicKey(
                    keyWriter.publicKeyToString(edDSAPublicKey));
            assertEquals(edDSAPublicKey, edDSAPublicKey1);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Remove test key files
     */
    @After
    public void delete() {
        new File("private.key").delete();
        new File("public.key").delete();
        new File("empty").delete();
    }

}