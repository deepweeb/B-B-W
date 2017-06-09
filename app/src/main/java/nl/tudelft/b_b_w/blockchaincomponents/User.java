package nl.tudelft.b_b_w.blockchaincomponents;

/**
 * Class for creating a user
 */
public class User {

    /**
     * Properties of a user
     */
    private String name;
    private String iban;
    private Public_Key publicKey;

    /**
     * Constructor for user class
     * @param name given name
     * @param iban given iban
     * @param publicKey given publicKey object
     */
    public User(String name, String iban, Public_Key publicKey) {
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
    public Public_Key getPublicKey() {
        return this.publicKey;
    }

}
