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
     * Constructor for user class
     * User class used by crawler, iban in this user class is equal to the string representation of the read public key
     *
     * @param name given name
     * @param iban given iban
     */
    public User(String name, String iban) {
        this.name = name;
        this.iban = iban;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }

        User user = (User) o;

        if (name != null ? !name.equals(user.name) : user.name != null) {
            return false;
        }
        if (iban != null ? !iban.equals(user.iban) : user.iban != null) {
            return false;
        }
        return publicKey != null ? publicKey.equals(user.publicKey) : user.publicKey == null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (iban != null ? iban.hashCode() : 0);
        result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
        return result;
    }
}
