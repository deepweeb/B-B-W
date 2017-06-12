package nl.tudelft.b_b_w.blockchain;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

/**
 * Class for creating a user object
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
     * @param name given name
     * @param iban given iban
     * @param publicKey given publicKey object
     */
    public User(String name, String iban, EdDSAPublicKey publicKey) {
        this.name = name;
        this.iban = iban;
        this.publicKey = publicKey;
    }

    /**
     * getName function
     * @return name of this.user
     */
    public String getName() {
        return this.name;
    }

    /**
     * getIban function
     * @return iban number of this.user
     */
    public String getIban() {
        return this.iban;
    }

    /**
     * publicKey function
     * @return Public_Key object of this.user
     */
    public EdDSAPublicKey getPublicKey() {
        return this.publicKey;
    }

}
