package nl.tudelft.b_b_w.blockchain;

import java.util.ArrayList;

import nl.tudelft.b_b_w.model.block.Block;

/**
 * Class for creating a chainList
 */

public class Chain {

    /**
     * Properties of a Chain object
     */
    private User chainOwner;
    private ArrayList<Block> chainList;

    /**
     * Constructor for user class
     *
     * @param chainOwner given name
     */
    public Chain(User chainOwner) {
        this.chainOwner = chainOwner;
    }

    /**
     * getChainOwner() function
     *
     * @return owner User object of the Chain
     */
    public User getChainOwner() {
        return this.chainOwner;
    }

    /**
     * getChainList() function
     *
     * @return ArrayList of Blocks that represent this user's chainList
     */
    public ArrayList<Block> getChainList() {
        return this.chainList;
    }

    /**
     * setChainList() function
     * Set the ArrayList<Block>  of this chainList to the value of the parameter
     */
    public void setChainList(ArrayList<Block> arrayList) {
        this.chainList = arrayList;
    }

}
