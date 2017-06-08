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

    /**
     * getIban function
     *
     * @return Iban of this.user
     */
    public ArrayList<Block> queryContacts() {
        return null;
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
     * getChain function
     * @return Chain of this.user
     */
    public Chain getChain() {
        return this.chain;
    }

    /**
     * addContact function
     * this function adds an acquaintance into the chain of this.user
     */
    public void addContact(Acquaintance contact) throws Exception {
        this.chain.add(contact);
    }

    /**
     * getChain function
     * this function revoke an contact of the chain of this.user
     */
    public void revokeContact(Chain contact)
    {
        this.chain.remove(contact);
    }

    /**
     * getChain function
     *
     * @return Chain of user
     */
    public void queryContact()
    {
        this.chain.queryContact();
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

        Chain user = (Chain) o;

        if (!name.equals(user.name)) {
            return false;
        }
        return iban.equals(user.iban);
    }
}
