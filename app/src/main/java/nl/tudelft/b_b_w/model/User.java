package nl.tudelft.b_b_w.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for creating a user
 */

public class User {

    /**
     * Properties of a user
     */
    private String name;
    private String iban;

    /**
     * Constructor for user class
     * @param _name given name
     * @param _iban given iban
     */
    public User(String _name, String _iban) {
        this.name = _name;
        this.iban = _iban;
    }

    /**
     * getName function
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * getIban function
     * @return Iban of user
     */
    public String getIban() {
        return iban;
    }

    /**
     * generatePublicKey function
     * Generates a public key
     * @return generated publicKey
     */
    public String generatePublicKey() {
        //TODO: Generate public key using ED25519 protocol
        //Generate SHA256 hash until this protocol is implemented
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String text = name + iban;
        try {
            messageDigest.update(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] digest = messageDigest.digest();
        return String.format("%064x", new java.math.BigInteger(1, digest));
    }
}
