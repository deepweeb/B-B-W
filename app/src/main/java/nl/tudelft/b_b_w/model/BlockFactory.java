package nl.tudelft.b_b_w.model;

/**
 * BlockFactory class
 * Outputs a block
 */
public class BlockFactory {

    private final static boolean TYPE_REVOKE = true;

    /**
     * getBlock method
     *
     * @param type                type of block
     * @param _owner              owner of block
     * @param _ownHash            Hash value of the block
     * @param _previousHashChain  Previous hash value of the block in the blockchain
     * @param _previousHashSender Previous hash value of the sender of the block
     * @param _publicKey          Public key of the sender
     * @param _iban               IBAN number of a contact
     * @return Returns a new block
     * @throws IllegalArgumentException when the input string type is invalid
     */
    public static Block getBlock(String type, String _owner, String _ownHash, String _previousHashChain, String _previousHashSender, String _publicKey, String _iban, int _trustValue) throws IllegalArgumentException {
        switch (type) {
            case "BLOCK":
                return new Block(_owner, _ownHash, _previousHashChain, _previousHashSender, _publicKey, _iban, _trustValue, !TYPE_REVOKE);
            case "REVOKE":
                return new Block(_owner, _ownHash, _previousHashChain, _previousHashSender, _publicKey, _iban, _trustValue, TYPE_REVOKE);
            default:
                throw new IllegalArgumentException("Invalid type of block: " + type);
        }
    }

    public static Block getBlock(User owner, User user, String previousHashChain,
                                 String previousHashSender, String publicKey,
                                 boolean revoke) {
        return new Block(owner.getName(), "ownHash", previousHashChain,
                previousHashSender, publicKey, user.getIban(), 0, revoke);
    }
}
