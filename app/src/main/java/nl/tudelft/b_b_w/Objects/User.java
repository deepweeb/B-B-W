package nl.tudelft.b_b_w.Objects;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.c;

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
     *
     * @param name given name
     * @param iban given iban
     */
    public User(String name, String iban, Public_Key publicKey) {
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
     * @return Iban of this.user
     */
    public String getIban() {
        return this.iban;
    }

    /**
     * publicKey function
     *
     * @return Public_Key of this.user
     */
    public Public_Key getPublicKey() {
        return this.publicKey;
    }


    /**
     * This function checks whether two User objects are equal to each other.
     * @param o
     *          an object
     * @return
     *          true in case they are equals, otherwise false.
     */
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
}
