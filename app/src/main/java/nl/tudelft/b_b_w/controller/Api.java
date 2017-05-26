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
    private Context context;
    private BlockController blockController;

    /**
     * Initialize the API with a context
     * @param context A context object, needed for the database
     */
    public Api(Context context) {
        this.context = context;
        blockController = new BlockController(context);
    }

    /**
     * Retrieve the keys of a user from the viewpoint of an owner user
     * @param owner the owner of the blockchain to query
     * @param user the user of whom to retrieve non-revoked public keys
     * @return a list of public keys in string form
     */
    public List<String> getUserKeys(String owner, String user) {
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
     */
    public void addKey(String owner, String user, String key) {
        // find blocks to connect to
        List<Block> senderBlocks = blockController.getBlocks(user);
        Block genesisSender = senderBlocks.get(0);
        Block latest = blockController.getLatestBlock(owner);

        // create our block
        String hash = "hash@" + user + "@" + key;
        String prevHashSelf = latest.getOwnHash();
        String prevHashOther = genesisSender.getOwnHash();
        if (owner == user)
            prevHashOther = "N/A";

        Block fresh = BlockFactory.getBlock("BLOCK", owner, hash, prevHashSelf, prevHashOther, key, "NL81...", 0);

        // add to database
        blockController.addBlock(fresh);
    }

    /**
     * Revoke a user-key binding and append the revokal to the chain.
     * @param owner The owner of the chain we want to revoke from
     * @param user The user who possessed the key
     * @param key The public key we want to revoke
     */
    public void revokeKey(String owner, String user, String key) {
        // find blocks to connect to
        List<Block> senderBlocks = blockController.getBlocks(user);
        Block genesisSender = senderBlocks.get(0);
        Block latest = blockController.getLatestBlock(owner);

        // create our block
        String hash = "hash@" + user + "@" + key;
        String prevHashSelf = latest.getOwnHash();
        String prevHashOther = genesisSender.getOwnHash();
        if (owner == user)
            prevHashOther = "N/A";

        // create revoke block
        Block fresh = BlockFactory.getBlock("REVOKE", user, hash, prevHashSelf, prevHashOther, key, "NL81...", 0);

        // add to database
        blockController.addBlock(fresh);
    }

}
