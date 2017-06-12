package nl.tudelft.b_b_w.controller;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.security.MessageDigest;

import nl.tudelft.b_b_w.blockchaincomponents.BlockData;
import nl.tudelft.b_b_w.blockchaincomponents.User;

/**
 * Class to convert values into a hashed value. Use BlockData.calculateHash instead.
 */
public class ConversionController {
    // Variables which we need to create a hashed key
    private String blockOwner;
    private String contactName;
    private String contactIban;
    private EdDSAPublicKey contactPublicKey;

    private String previousBlockHash;
    private String contactBlockHash;


    /**
     * Instantiating the necessary variables
     *
     * @param _senderPublicKey Public_Key of the block
     * @param _owner           Owner of the block
     */
    public ConversionController(String blockOwner, User contact, BlockData blockData)
    {
        blockOwner = blockOwner;
        contactName = contact.getName();
        contactIban = contact.getIban();
        contactPublicKey = contact.getPublicKey();

        previousBlockHash = _previousBlockHash;
        contactBlockHash = _contactBlockHash;
        contactIban = _contactIban;
    }

    /**
     * Method for converting the PK, Owner, its previous block's Hash  and its new contact hash to a hashed key
     * with a SHA-256 conversion
     *
     * @return the Hashed Key
     */
    public final String hashKey() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = blockOwner + senderPublicKey + previousBlockHash + contactBlockHash + contactIban;
        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();
        String hash = String.format("%064x", new java.math.BigInteger(1, digest));
        return hash;
    }
}
