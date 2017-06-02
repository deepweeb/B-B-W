package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.model.User;

public class BlockData {
    private BlockType blockType;
    private int sequenceNumber;
    private String ownerName;
    private String hash;
    private String previousHashChain;
    private String previousHashSender;
    private String publicKey;
    private String iban;
    private int trustValue;

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

    public User getOwner() {
        return new User(ownerName, iban);
    }

    public void setOwner(User owner) {
        this.ownerName = owner.getName();
    }

    public void setIban(User contact) {
        this.iban = contact.getIban();
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

    public int getTrustValue() {
        return trustValue;
    }

    public void setTrustValue(int trustValue) {
        this.trustValue = trustValue;
    }
}
