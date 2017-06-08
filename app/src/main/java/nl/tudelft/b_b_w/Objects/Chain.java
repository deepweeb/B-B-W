package nl.tudelft.b_b_w.Objects;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.model.block.Block;

/**
 * Class for creating a user
 */

public class Chain {

    /**
     * Properties of a user
     */
    private User chainOwner;
    private ArrayList<Block> chain;

    /**
     * Constructor for user class
     *
     * @param chainOwner given name
     */
    public Chain(User chainOwner) {
        this.chainOwner = chainOwner;
        //TODO
        //queryContacts()
        // if return null, make genesis block
        //else, assign resulted values to this.chain

    }

    /**
     * getName function
     *
     * @return name of this.user
     */
    public User getChainOwner() {
        return this.chainOwner;
    }


}
