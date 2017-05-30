package nl.tudelft.b_b_w.model.block;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nl.tudelft.b_b_w.model.HashException;

public class BlockData {
    private BlockType blockType;
    private int sequenceNumber;
    private String owner;
    private String hash;
    private String previousHashChain;
    private String previousHashSender;
    private String publicKey;
    private String iban;
    private int trustValue;

    /**
     * Calculate the SHA-256 hash of this block
     * @return the base-64 encoded hash as a string
     * @throws HashException when the crypto functions are not available
     */
    public String calculateHash() throws HashException {
        try {
            // ConversionController conversionController = new ConversionController();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String text = owner + publicKey + previousHashChain + previousHashSender + iban;
            md.update(text.getBytes("UTF-8"));
            byte[] digest = md.digest();
            String hash = String.format("%064x", new BigInteger(1, digest));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new HashException("SHA-256 not available on this device");
        } catch (UnsupportedEncodingException e) {
            throw new HashException("UTF-8 not available on this device");
        }
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPreviousHashChain() {
        return previousHashChain;
    }

    public void setPreviousHashChain(String previousHashChain) {
        this.previousHashChain = previousHashChain;
    }

    public String getPreviousHashSender() {
        return previousHashSender;
    }

    public void setPreviousHashSender(String previousHashSender) {
        this.previousHashSender = previousHashSender;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public int getTrustValue() {
        return trustValue;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockData blockData = (BlockData) o;

        try {
            return calculateHash().equals(blockData.calculateHash());
        } catch (HashException e) {
            return false;
        }
    }

    public void setTrustValue(int trustValue) {
        this.trustValue = trustValue;
    }
}
