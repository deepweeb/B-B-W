package nl.tudelft.b_b_w.Models;

import java.security.MessageDigest;

/**
 * Class to convert values into a hashed value
 * Created by Ashay on 01/05/2017.
 */
public class Conversion {
    // Variables which we need to create a hashed key
    private String keyOwner;
    private String senderPublicKey;
    private String previousBlockHash;
    private String contactBlockHash;

    /**
     * Instantiating the necessary variables
     * @param senderPublicKey PublicKey of the block
     * @param owner Owner of the block
     */
    public Conversion(String owner, String senderPublicKey, String previousBlockHash, String contactBlockHash) {
        keyOwner = owner;
        this.senderPublicKey = senderPublicKey;
        this.previousBlockHash = previousBlockHash;
        this.contactBlockHash = contactBlockHash;
    }

    /**
     * Method for converting the PK, Owner, its previous block's Hash  and its new contact hash to a hashed key
     * with a SHA-256 conversion
     * @return the Hashed Key
     */
    public String hashKey() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = keyOwner + senderPublicKey + previousBlockHash + contactBlockHash;
        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();
        String hash = String.format("%064x", new java.math.BigInteger(1, digest));
        return hash;
    }
}
