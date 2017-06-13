package nl.tudelft.b_b_w.blockchain;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.util.ArrayList;

/**
 * Class for creating an acquaintance,
 * This is someone who you paired with but is not in your contact list yet.
 * This is a data transfer object.
 */

public class Acquaintance extends User{

    /**
     * Properties of a Acquaintance
     */
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
     * getMultichain function
     * @return List<Chain> of the multichain of this.Acquaintance
     */
    public ArrayList<Chain> getMultichain() {
        return this.multichain;
    }

}
