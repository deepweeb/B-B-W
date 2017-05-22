package nl.tudelft.b_b_w.controller;

import java.security.MessageDigest;

/**
 * Class to convert values into a hashed value
 * Created by Ashay on 01/05/2017.
 */
public class ConversionController {
    // Variables which we need to create a hashed key
    private String blockOwner;
    private int     seqNumber;
    private String senderPublicKey;
    private String previousBlockHash;
    private String contactBlockHash;
    private String contactIban;

    /**
     * Instantiating the necessary variables
     * @param senderPublicKey PublicKey of the block
     * @param owner Owner of the block
     */
    public ConversionController(String owner, int sn, String senderPublicKey, String previousBlockHash, String contactBlockHash, String contactIban) {
        this.blockOwner = owner;
        this.seqNumber =sn;
        this.senderPublicKey = senderPublicKey;
        this.previousBlockHash = previousBlockHash;
        this.contactBlockHash = contactBlockHash;
        this.contactIban = contactIban;
    }

    /**
     * Method for converting the PK, Owner, its previous block's Hash  and its new contact hash to a hashed key
     * with a SHA-256 conversion
     * @return the Hashed Key
     */
    public String hashKey() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = blockOwner + String.valueOf(seqNumber) + senderPublicKey + previousBlockHash + contactBlockHash + contactIban;
        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();
        String hash = String.format("%064x", new java.math.BigInteger(1, digest));
        return hash;
    }
}
