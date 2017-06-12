package nl.tudelft.b_b_w.blockchaincomponents;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.util.ArrayList;

/**
 * Class for creating aan acquaintance,
 * this is someone who you paired with but is not in your contact list yet.
 * This is a data transfer object.
 */

public class Acquaintance extends User{

    /**
     * Properties of a Acquaintance
     */
    private User user;
    private ArrayList<Chain> multichain;

    /**
     * Constructor for user class
     * @param multichain given multichain
     */
    public Acquaintance(String name, String iban, EdDSAPublicKey publicKey, ArrayList<Chain> multichain) {
        super(name, iban, publicKey);
        this.multichain = multichain;
    }

    /**
     * getUser function
     *
     * @return user object of this.Acquaintance
     */
    public User getUser() {
        return this.user;
    }

    /**
     * getMultichain function
     *
     * @return List<Chain> of the multichain of this.Acquaintance
     */
    public ArrayList<Chain> getMultichain() {
        return this.multichain;
    }


}
