package nl.tudelft.b_b_w.blockchaincomponents;

import java.util.ArrayList;

import nl.tudelft.b_b_w.model.block.Block;

/**
 * Class for creating a chain
 */

public class Chain {

    /**
     * Properties of a Chain object
     */
    private User chainOwner;
    private ArrayList<Block> chain;

    /**
     * Constructor for user class
     * @param chainOwner given name
     */
    public Chain(User chainOwner) {
        this.chainOwner = chainOwner;
    }

    /**
     * getChainOwner() function
     * @return owner User object of the Chain
     */
    public User getChainOwner() {
        return this.chainOwner;
    }

    /**
     * getChain() function
     * @return ArrayList of Blocks that represent this user's chain
     */
    public ArrayList<Block> getChain() {
        return this.chain;
    }

    /**
     * setChain() function
     * Set the ArrayList<Block>  of this chain to the value of the parameter
     */
    public void setChain(ArrayList<Block> arrayList) {
        this.chain = arrayList;
    }

}
