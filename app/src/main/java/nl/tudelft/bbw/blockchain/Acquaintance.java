package nl.tudelft.bbw.blockchain;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.util.List;

/**
 * Class for creating an acquaintance,
 * This is someone who you paired with but is not in your contact list yet.
 * This is a data transfer object.
 */

public class Acquaintance extends User {

    /**
     * Properties of a Acquaintance
     */
    private List<List<Block>> multichain;

    /**
     * Constructor for user class
     *
     * @param multichain given multichain
     */
    public Acquaintance(String name, String iban, EdDSAPublicKey publicKey, List<List<Block>> multichain) {
        super(name, iban, publicKey);
        this.multichain = multichain;
    }

    /**
     * getMultichain function
     *
     * @return List<Chain> of the multichain of this.Acquaintance
     */
    public List<List<Block>> getMultichain() {
        return this.multichain;
    }

    public void setMultichain(List<List<Block>> multichain)
    {
        this.multichain = multichain;
    }

}
