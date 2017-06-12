package nl.tudelft.b_b_w.controller;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.security.MessageDigest;

import nl.tudelft.b_b_w.blockchaincomponents.BlockData;
import nl.tudelft.b_b_w.blockchaincomponents.Block_Type;
import nl.tudelft.b_b_w.blockchaincomponents.Hash;
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

    private Block_Type blockType;
    private int sequenceNumber;
    private Hash previousHashChain;
    private Hash previousHashSender;
    private int trustValue;

    /**
     *
     * @param blockOwner
     * @param contact
     * @param blockData
     */
    public ConversionController(String blockOwner, User contact, BlockData blockData)
    {
        this.blockOwner = blockOwner;

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
        String text = blockOwner +
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
