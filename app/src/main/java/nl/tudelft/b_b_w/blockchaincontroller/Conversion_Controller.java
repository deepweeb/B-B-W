package nl.tudelft.b_b_w.blockchaincontroller;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.security.MessageDigest;

import nl.tudelft.b_b_w.blockchaincomponents.BlockData;
import nl.tudelft.b_b_w.blockchaincomponents.Block_Type;
import nl.tudelft.b_b_w.blockchaincomponents.Hash;
import nl.tudelft.b_b_w.blockchaincomponents.User;

/**
 * Class to convert values into a hashed value. Use BlockData.calculateHash instead.
 */
public class Conversion_Controller {
    // Variables which we need to create a hashed key

    private String ownerName;
    private String ownerIban;
    private EdDSAPublicKey ownerPublicKey;

    private String contactName;
    private String contactIban;
    private EdDSAPublicKey contactPublicKey;

    private Block_Type blockType;
    private int sequenceNumber;
    private Hash previousHashChain;
    private Hash previousHashSender;
    private int trustValue;

    /**
     *
     * @param owner
     * @param contact
     * @param blockData
     */
    public Conversion_Controller(User owner, User contact, BlockData blockData)
    {
        this.ownerName = owner.getName();
        this.ownerIban = owner.getIban();
        this.ownerPublicKey = owner.getPublicKey();

        this.contactName = contact.getName();
        this.contactIban = contact.getIban();
        this.contactPublicKey = contact.getPublicKey();

        this.blockType = blockData.getBlockType();
        this.sequenceNumber = blockData.getSequenceNumber();
        this.previousHashChain = blockData.getPreviousHashChain();
        this.previousHashSender = blockData.getPreviousHashSender();
        this.trustValue = blockData.getTrustValue();

    }

    /**
     * Method for converting the PK, Owner, its previous block's Hash  and its new contact hash to a hashed key
     * with a SHA-256 conversion
     *
     * @return the Hashed Key
     */
    public final Hash hashKey() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text =  ownerName +
                ownerIban +
                ownerPublicKey.toString() +
                contactName +
                contactIban +
                contactPublicKey.toString() +
                blockType.name() +
                String.valueOf(sequenceNumber) +
                previousHashChain.toString() +
                previousHashSender.toString() +
                String.valueOf(trustValue);
        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return new Hash(String.format("%064x", new java.math.BigInteger(1, digest)));
    }
}
