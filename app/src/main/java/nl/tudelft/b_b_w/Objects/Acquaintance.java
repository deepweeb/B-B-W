package nl.tudelft.b_b_w.Objects;

import java.util.List;

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
    private List<Chain> multichain;

    /**
     * Constructor for user class
     *
     * @param user given user
     * @param multichain given multichain
     */
    public Acquaintance(User user, List<Chain> multichain) {
        this.user = user;
        this.multichain = multichain;
    }

    /**
     * getUser function
     * @return user object of this.Acquaintance
     */
    public User getUser() {
        return this.user;
    }

    /**
     * getMultichain function
     * @return  List<Chain> of the multichain of this.Acquaintance
     */
    public List<Chain> getMultichain() {
        return this.multichain;
    }




}
