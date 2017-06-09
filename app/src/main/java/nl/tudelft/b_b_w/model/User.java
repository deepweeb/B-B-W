package nl.tudelft.b_b_w.model;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

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
    private EdDSAPublicKey publicKey;

    /**
     * Constructor for user class
     *
     * @param name given name
     * @param iban given iban
     */
    public User(String name, String iban) {
        this.name = name;
        this.iban = iban;
    }

    /**
     * Constructor for user class
     *
     * @param name given name
     * @param iban given iban
     *
     */
    public User(String name, String iban, EdDSAPublicKey publicKey) {
        this.name = name;
        this.iban = iban;
        this.publicKey = publicKey;
    }

    /**
     * getName function
     *
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * getIban function
     *
     * @return Iban of user
     */
    public String getIban() {
        return iban;
    }

    /**
     * getPublicKey function
     *
     * @return public key of user
     */
    public EdDSAPublicKey getPublicKey() {
        return this.publicKey;
    }

    /**
     * generatePublicKey function
     * Generates a public key
     *
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (!name.equals(user.name)) {
            return false;
        }
        return iban.equals(user.iban);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + iban.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
