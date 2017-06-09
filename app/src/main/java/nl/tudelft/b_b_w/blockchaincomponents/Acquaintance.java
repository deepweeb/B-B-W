package nl.tudelft.b_b_w.blockchaincomponents;

import java.util.ArrayList;

/**
 * Class for creating aan acquaintance,
 * this is someone who you paired with but is not in your contact list yet.
 * This is a data transfer object.
 */

public class Acquaintance {

    /**
     * Properties of a Acquaintance
     */
    private User user;
    private ArrayList<Chain> multichain;

    /**
     * Constructor for user class
     *
     * @param user       given user
     * @param multichain given multichain
     */
    public Acquaintance(User user, ArrayList<Chain> multichain) {
        this.user = user;
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
