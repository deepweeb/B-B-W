package nl.tudelft.bbw.blockchain;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
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
    private EdDSAPrivateKey privateKey;

    /**
     * Constructor for user class
     *
     * @param name      given name
     * @param iban      given iban
     * @param publicKey given publicKey object
     */
    public User(String name, String iban, EdDSAPublicKey publicKey) {
        this.name = name;
        this.iban = iban;
        this.publicKey = publicKey;
    }

    /**
     * getName function
     *
     * @return name of this.user
     */
    public String getName() {
        return this.name;
    }

    /**
     * getIban function
     *
     * @return iban number of this.user
     */
    public String getIban() {
        return this.iban;
    }

    /**
     * publicKey function
     *
     * @return EdDSAPublicKey object of this.user
     */
    public EdDSAPublicKey getPublicKey() {
        return this.publicKey;
    }

    /**
     * getPrivateKey function
     *
     * @return EdDSAPrivateKey object of this.user
     */
    public EdDSAPrivateKey getPrivateKey() {
        return this.privateKey;
    }

    /**
     * setPrivateKey function
     *
     * @param edDSAPrivateKey given private key to set it to
     */
    public void setPrivateKey(EdDSAPrivateKey edDSAPrivateKey) {
        this.privateKey = edDSAPrivateKey;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return name;
    }

}
