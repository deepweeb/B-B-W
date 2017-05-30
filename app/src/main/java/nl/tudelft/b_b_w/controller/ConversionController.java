package nl.tudelft.b_b_w.controller;

import java.security.MessageDigest;

/**
 * Class to convert values into a hashed value. Use BlockData.calculateHash instead.
 */
@Deprecated
public class ConversionController {
    // Variables which we need to create a hashed key
    private String blockOwner;
    private String senderPublicKey;
    private String previousBlockHash;
    private String contactBlockHash;
    private String contactIban;

    /**
     * Instantiating the necessary variables
     * @param _senderPublicKey PublicKey of the block
     * @param _owner Owner of the block
     */
    public ConversionController(String _owner,  String _senderPublicKey, String _previousBlockHash, String _contactBlockHash, String _contactIban) {
        blockOwner = _owner;
        senderPublicKey = _senderPublicKey;
        previousBlockHash = _previousBlockHash;
        contactBlockHash = _contactBlockHash;
        contactIban = _contactIban;
    }

    /**
     * Method for converting the PK, Owner, its previous block's Hash  and its new contact hash to a hashed key
     * with a SHA-256 conversion
     * @return the Hashed Key
     */
    public final String hashKey() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = blockOwner  + senderPublicKey + previousBlockHash + contactBlockHash + contactIban;
        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();
        String hash = String.format("%064x", new java.math.BigInteger(1, digest));
        return hash;
    }
}
