package nl.tudelft.b_b_w.model.block;

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

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    private boolean revoked;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public void setTrustValue(int trustValue) {
        this.trustValue = trustValue;
    }

}
