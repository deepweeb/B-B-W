package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.User;

import static nl.tudelft.b_b_w.view.MainActivity.PREFS_NAME;

/**
 * Pair activity lets you pair with a fixed number of preprogrammed contacts, for demo purposes.
 */
public class PairActivity extends Activity {
    /**
     * Some constants for list indexing.
     */
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;

    /**
     * The block controller.
     */
    private BlockController blockController;

    /**
     * It's own block.
     */
    private Block block1;

    /**
     * Preprogrammed blocks for now, later Bluetooth will be used.
     */
    private Block block2;
    private Block block3;
    private Block block4;


    /**
     * The name of the owner of each block in the chain.
     */
    private String ownerName;


    /**
     * The on create method sets up the activity.
     * @param savedInstanceState brings the variables.
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);
        blockController = new BlockController(this);
    }


    /**
     * This method create the first test subject.We do this in order to simulate a transaction.
     * @param view The view of the program.
     */
    public final void onTestSubject1(View view) throws Exception {

        ownerName = "TestSubject1";
        final String ibanTestSub1= "IBAN1";

        try {
            block1 = blockController.createGenesis(new User(ownerName, ibanTestSub1));
        }
        catch(Exception e)
        {
        Toast.makeText(this, "Sorry, this contact is already added!",
                Toast.LENGTH_SHORT).show();
        return;
        }

        block2 = blockController.createKeyBlock(ownerName, ownerName, "Contact1_PUBKEY", "IBAN1Contact1");

        block3 = blockController.createKeyBlock(ownerName, ownerName, "Contact2_PUBKEY", "IBAN1Contact2");

        block4 = blockController.createKeyBlock(ownerName, ownerName, "Contact3_PUBKEY", "IBAN1Contact3");

        List<Block> list = blockController.getBlocks(ownerName);

        Toast.makeText(this, list.get(0).getPublicKey() + ", " +
                list.get(ONE).getPublicKey() + ", " +
                list.get(TWO).getPublicKey() + ", " +
                list.get(THREE).getPublicKey(), Toast.LENGTH_SHORT).show();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userNameTestSubject", ownerName);
        editor.putString("ibanTestSubject", ibanTestSub1);
        editor.apply();

        startActivity(new Intent(this, FriendsPageActivity.class));
    }

    /**
     * This method creates another test subject(second). It is hardcoded and will be change later on.
     * We do this to simulate a transaction.
     * @param view The view of the program.
     */
    public final void onTestSubject2(View view) throws Exception {

        Block block5;
        ownerName = "TestSubject2";
        final String ibanTestSub2= "IBAN2";

        try {
            block1 = blockController.createGenesis(new User(ownerName, ibanTestSub2));
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Sorry, this contact is already added!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        block2 = blockController.createKeyBlock(ownerName, ownerName, "b", "IBAN2Contact1");
        block3 = blockController.createKeyBlock(ownerName, ownerName, "c", "IBAN2Contact2");
        block2 = blockController.createKeyBlock(ownerName, ownerName, "d", "IBAN2Contact3");
        block2 = blockController.createKeyBlock(ownerName, ownerName, "e", "IBAN2Contact4");

        List<Block> list = blockController.getBlocks(ownerName);

        Toast.makeText(this, list.get(0).getPublicKey() + ", " +
                list.get(ONE).getPublicKey() + ", " +
                list.get(TWO).getPublicKey() + ", " +
                list.get(THREE).getPublicKey() + ", " +
                list.get(FOUR).getPublicKey(), Toast.LENGTH_SHORT).show();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userNameTestSubject", ownerName);
        editor.putString("ibanTestSubject", ibanTestSub2);
        editor.apply();

        startActivity(new Intent(this, FriendsPageActivity.class));
    }

    /**
     * This method creates another test subject (third). It is hardcoded and will be change later on.
     * We do this to simulate a transaction.aa
     * @param view  The view of the program.
     */
    public final void onTestSubject3(View view) {


        ownerName = "TestSubject3";
        final String ibanTestSub3= "IBAN3";

        try {
            block1 = blockController.createGenesis(new User(ownerName, ibanTestSub3));
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Sorry, this contact is already added!",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        List<Block> list = blockController.getBlocks(ownerName);

        Toast.makeText(this, list.get(0).getPublicKey(), Toast.LENGTH_SHORT).show();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userNameTestSubject", ownerName);
        editor.putString("ibanTestSubject", ibanTestSub3);
        editor.apply();

        startActivity(new Intent(this, FriendsPageActivity.class));

    }


}