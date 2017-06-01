package nl.tudelft.b_b_w.model;

/**
 * Class for creating a user
 */

public class User {

    /**
     * Properties of a user
     */
    private String name;
    private String IBAN;

    /**
     * Constructor for user class
     * @param _name given name
     * @param _IBAN given iban
     */
    public User(String _name, String _IBAN) {
        this.name = _name;
        this.IBAN = _IBAN;
    }

    /**
     * getName function
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * getIBAN function
     * @return IBAN of user
     */
    public String getIBAN() {
        return IBAN;
    }

    /**
     * generatePublicKey function
     * Generates a public key
     * @return
     */
    public String generatePublicKey() {
        //TODO: Generate public key using ED25519 protocol
        final String PUBLIC_KEY = "PUBLIC_KEY";
        return PUBLIC_KEY;
    }
}
