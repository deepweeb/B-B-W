package nl.tudelft.b_b_w.model;

/**
 * Created by jasper on 17/05/2017.
 */

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
     * @param _sequenceNumber     sequenceNumber of block
     * @param _ownHash            Hash value of the block
     * @param _previousHashChain  Previous hash value of the block in the blockchain
     * @param _previousHashSender Previous hash value of the sender of the block
     * @param _publicKey          Public key of the sender
     * @param _iban               IBAN number of a contact
     * @return Returns a new block
     * @throws IllegalArgumentException when the input string type is invalid
     */
    public static Block getBlock(String type, String _owner, int _sequenceNumber, String _ownHash, String _previousHashChain, String _previousHashSender, String _publicKey, String _iban) throws IllegalArgumentException {
        switch (type) {
            case "BLOCK":
                return new Block(_owner, _sequenceNumber, _ownHash, _previousHashChain, _previousHashSender, _publicKey, _iban, !TYPE_REVOKE);
            case "REVOKE":
                return new Block(_owner, _sequenceNumber, _ownHash, _previousHashChain, _previousHashSender, _publicKey, _iban, TYPE_REVOKE);
            default:
                throw new IllegalArgumentException("Invalid type of block: " + type);
        }
    }
}
