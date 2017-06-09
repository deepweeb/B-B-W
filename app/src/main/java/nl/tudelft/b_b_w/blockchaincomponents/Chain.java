package nl.tudelft.b_b_w.blockchaincomponents;

import android.content.Context;

import java.util.ArrayList;

import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.block.Block;

/**
 * Class for creating a user
 */

public class Chain {

    /**
     * Properties of a user
     */
    private BlockController blockController;
    private User chainOwner;
    private ArrayList<Block> chain;
    private Context context;

    /**
     * Constructor for user class
     *
     * @param chainOwner given name
     */
    public Chain(User chainOwner) {
        blockController = new BlockController(context, chainOwner);
        this.chainOwner = chainOwner;
        queryContacts();

    }

    /**
     * getName function
     *
     * @return name of this.user
     */
    public User getChainOwner() {
        return this.chainOwner;
    }

    /**
     * getName function
     *
     * @return name of this.user
     */
    public ArrayList<Block> getChain() {
        return this.chain;
    }

    /**
     * getName function
     *
     * @return name of this.user
     */
    public ArrayList<Block> queryContacts() {
        try {
            return blockController.getChainOf(this.chainOwner);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * getName function
     *
     * @return name of this.user
     */
    public void remove(User contact) throws HashException {
        try {
            blockController.revokeBlock(contact);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * getName function
     *
     * @return name of this.user
     */
    public ArrayList<Block> queryContacts() {
        try {
            return blockController.getChainOf(this.chainOwner);
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * getName function
     *
     * @return name of this.user
     */
    public void add(Acquaintance acquaintance) throws HashException {
        try {
            blockController.addBlockToChain();
            blockController.addChains(acquaintance);
        } catch (Exception e) {
            throw e;
        }
    }


}
