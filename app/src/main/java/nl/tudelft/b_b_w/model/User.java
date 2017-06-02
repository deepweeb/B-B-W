package nl.tudelft.b_b_w.model;

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
        //generate random number until this protocol is implemented
        return String.valueOf((int)(Math.random() * 50 + 1));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof User) {
            User other = (User) object;
            return other.getIBAN().equals(getIBAN()) &&
                    other.getName().equals(getName());
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
}
}
