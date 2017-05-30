package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;

/** The Api class provides read and write access to our data without having to worry about
 * the blockchain and database.
 */
public class Api {
    /** BlockController for access to the blocks */
    private BlockController blockController;

    /**
     * Initialize the API with a context
     * @param context A context object, needed for the database
     */
    public Api(Context context) {
        blockController = new BlockController(context);
    }

    /**
     * Retrieve the keys of a user from the viewpoint of an owner user
     * @param owner the owner of the blockchain to query
     * @param user the user of whom to retrieve non-revoked public keys
     * @return a list of public keys in string form
     */
    public final List<String> getUserKeys(String owner, String user) {
        List<Block> blocks = blockController.getBlocks(owner);
        List<String> keys = new ArrayList<String>();

        // add public key of each block
        for (Block block : blocks) {
            if (block.getSequenceNumber() > 1) {

                if (user.equals(owner)) {
                    // our own keys do not have send hashes
                    if (Objects.equals(block.getPreviousHashSender(), "N/A"))
                        keys.add(block.getPublicKey());
                } else {
                    String blockUserName = blockController.getContactName(block.getPreviousHashSender());
                    String targetUserName = user;
                    if (targetUserName.equals(blockUserName))
                        keys.add(block.getPublicKey());
                }
            }
        }

        return keys;

    }

    /**
     * Add a user-key binding to a chain.
     * @param owner The owner of the chain we want to add to
     * @param user The user who possesses the key
     * @param key The public key we want to add
     * @throws Exception when hash function is not available
     */
    public final void addKey(String owner, String user, String key, String iban) throws Exception {
        // find blocks to connect to
        List<Block> senderBlocks = blockController.getBlocks(user);
        Block genesisSender = senderBlocks.get(0);
        Block latest = blockController.getLatestBlock(owner);

        // create our block
        String prevHashSelf = latest.getOwnHash();
        String prevHashOther = genesisSender.getOwnHash();
        if (owner.equals(user))
            prevHashOther = "N/A";

        // get hash
        ConversionController conversionController = new ConversionController(owner,
                key, prevHashSelf, prevHashOther, iban);
        String hash = conversionController.hashKey();

        Block fresh = BlockFactory.getBlock(
                "BLOCK",
                owner,
                blockController.getLatestSeqNumber(owner) + 1,
                hash,
                prevHashSelf,
                prevHashOther,
                key,
                iban,
                0
        );

        // add to database
        blockController.addBlock(fresh);
    }

    /**
     * Revoke a user-key binding to a chain.
     * @param owner The owner of the chain we want to add to
     * @param user The user who possesses the key
     * @param key The public key we want to add
     * @throws Exception when hash function is not available
     */
    public final void revokeKey(String owner, String user, String key, String iban) throws Exception {
        // find blocks to connect to
        List<Block> senderBlocks = blockController.getBlocks(user);
        Block genesisSender = senderBlocks.get(0);
        Block latest = blockController.getLatestBlock(owner);

        // create our block
        String prevHashSelf = latest.getOwnHash();
        String prevHashOther = genesisSender.getOwnHash();
        if (owner.equals(user))
            prevHashOther = "N/A";

        // get hash
        ConversionController conversionController = new ConversionController(owner,
                key, prevHashSelf, prevHashOther, iban);
        String hash = conversionController.hashKey();

        Block fresh = BlockFactory.getBlock(
                "REVOKE",
                owner,
                blockController.getLatestSeqNumber(owner) + 1,
                hash,
                prevHashSelf,
                prevHashOther,
                key,
                iban,
                0
        );

        // add to database
        blockController.addBlock(fresh);
    }

}
