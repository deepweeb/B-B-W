package nl.tudelft.b_b_w.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.User;

/** The Api class provides read and write access to our data without having to worry about
 * the blockchain and database.
 */
public class Api {
    private static Context context;
    private static BlockController blockController;
    private static ArrayList<User> users = new ArrayList<>();

    /**
     * Initialize the API with a context
     * @param context A context object, needed for the database
     */
    public static void init(Context context) {
        Api.context = context;
        Api.blockController = new BlockController(context);
    }

    /**
     * Add a new user to our list of users.
     * @param name name of the user
     * @param iban iban of the user
     * @return the freshly created user
     */
    public static User createUser(String name, String iban) {
        User user = new User(users.size(), name, iban);
        users.add(user);
        return user;
    }

    /**
     * Retrieve the keys of a user from the viewpoint of an owner user
     * @param owner the owner of the blockchain to query
     * @param user the user of whom to retrieve non-revoked public keys
     * @return a list of public keys in string form
     */
    public static List<String> getUserKeys(User owner, User user) {
        List<Block> blocks = blockController.getBlocks(owner.getName());
        List<String> keys = new ArrayList<String>();

        // add public key of each block
        for (Block block : blocks)
            if (blockController.getContactName(block.getOwnHash()).equals(user.getName()))
                keys.add(block.getPublicKey());

        return keys;

    }

}
