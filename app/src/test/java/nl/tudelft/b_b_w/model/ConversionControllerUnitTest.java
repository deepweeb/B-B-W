package nl.tudelft.b_b_w.ModelsUnitTest;

import org.junit.Test;
import nl.tudelft.b_b_w.controller.ConversionController;

import static org.junit.Assert.*;
/**
 * ConversionTest
 * Created by Ashay on 01/05/2017.
 */
public class ConversionControllerUnitTest {

    /**
     * Test to check whether the hash function gives the
     * desired output.
     * @throws Exception Catches error when the MessageDigest
     * gets an error.
     */
    @Test
        public void hashCheck() throws Exception {
        ConversionController co = new ConversionController("owner", 4,"2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824","d79d0573a5af1a8c8a77b10319b8dcb3e8370c3020ce3414437dfb7004ab3460","fdwedawawdaw32423r4234f23cf2t3wergrewrvt342qvt34rt2", "NL61A34265311");
        String check = "8737f8aa0911b2541a5d72778fd1167d01b333ca137eecf0c55b98bf5cc5775b";

        assertEquals(check, co.hashKey());
    }
}