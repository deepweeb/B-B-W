package nl.tudelft.b_b_w.view;

import static nl.tudelft.b_b_w.view.MainActivity.PREFS_NAME;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.API;
import nl.tudelft.b_b_w.model.User;
import nl.tudelft.b_b_w.model.block.Block;


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
    private API mAPI;

    /**
     * Its own block.
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
    private User owner;


    /**
     * The on create method sets up the activity.
     *
     * @param savedInstanceState brings the variables.
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);
        mAPI = new API(owner, this);
    }


    /**
     * This method create the first test subject.We do this in order to simulate a transaction.
     *
     * @param view The view of the program.
     */
    public final void onTestSubject1(View view) throws Exception {
        owner = new User("TestSubject1", "IBAN1");

        try {
            mAPI.makeGenesis(owner);
        } catch (Exception e) {
            Toast.makeText(this, "Sorry, this contact is already added!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        User subject1contact1 = new User("Subject1Contact1", "IBAN1Contact1");
        User subject1contact2 = new User("Subject1Contact2", "IBAN1Contact2");
        User subject1contact3 = new User("Subject1Contact3", "IBAN1Contact3");
        mAPI.makeGenesis(subject1contact1);
//        mAPI.addBlockToChain(owner, subject1contact1, "Contact1_PUBKEY");

        mAPI.makeGenesis(subject1contact2);
//        mAPI.createKeyBlock(owner, subject1contact2, "Contact2_PUBKEY");

        mAPI.makeGenesis(subject1contact3);
//        mAPI.createKeyBlock(owner, subject1contact3, "Contact3_PUBKEY");

        List<Block> list = mAPI.getBlocks(owner);

        Toast.makeText(this, list.get(0).getPublicKey() + ", "
                + list.get(ONE).getPublicKey() + ", "
                + list.get(TWO).getPublicKey() + ", "
                + list.get(THREE).getPublicKey(), Toast.LENGTH_SHORT).show();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userNameTestSubject", owner.getName());
        editor.putString("ibanTestSubject", owner.getIban());
        editor.apply();

        startActivity(new Intent(this, FriendsPageActivity.class));
    }

    /**
     * This method creates another test subject(second). It is hardcoded and will be change later on.
     * We do this to simulate a transaction.
     *
     * @param view The view of the program.
     */
    public final void onTestSubject2(View view) throws Exception {

        Block block5;
        owner = new User("TestSubject2", "IBAN2");

        try {
            mAPI.makeGenesis(owner);
        } catch (Exception e) {
            Toast.makeText(this, "Sorry, this contact is already added!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        User subject2contact1 = new User("Subject2Contact1", "IBAN2Contact1");
        User subject2contact2 = new User("Subject2Contact2", "IBAN2Contact2");
        User subject2contact3 = new User("Subject2Contact3", "IBAN2Contact3");
        User subject2contact4 = new User("Subject2Contact4", "IBAN2Contact4");

        mAPI.makeGenesis(subject2contact1);
//        mAPI.addBlockToChain(owner, subject2contact1, "b");

        mAPI.makeGenesis(subject2contact2);
//        mAPI.createKeyBlock(owner, subject2contact2, "c");

        mAPI.makeGenesis(subject2contact3);
//        mAPI.createKeyBlock(owner, subject2contact3, "d");

        mAPI.makeGenesis(subject2contact4);
//        mAPI.createKeyBlock(owner, subject2contact4, "e");

        List<Block> list = mAPI.getBlocks(owner);

        Toast.makeText(this, list.get(0).getPublicKey() + ", "
                + list.get(ONE).getPublicKey() + ", "
                + list.get(TWO).getPublicKey() + ", "
                + list.get(THREE).getPublicKey() + ", "
                + list.get(FOUR).getPublicKey(), Toast.LENGTH_SHORT).show();

        Toast.makeText(this, list.get(0).getPublicKey() + ", "
                + list.get(ONE).getPublicKey() + ", "
                + list.get(TWO).getPublicKey() + ", "
                + list.get(THREE).getPublicKey() + ", "
                + list.get(FOUR).getPublicKey(), Toast.LENGTH_SHORT).show();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userNameTestSubject", owner.getName());
        editor.putString("ibanTestSubject", owner.getIban());

        editor.apply();

        startActivity(new Intent(this, FriendsPageActivity.class));
    }

    /**
     * This method creates another test subject (third). It is hardcoded and will be change later on.
     * We do this to simulate a transaction.
     *
     * @param view The view of the program.
     */
    public final void onTestSubject3(View view) throws Exception {
        owner = new User("TestSubject3", "IBAN3");
        mAPI.makeGenesis(owner);


        List<Block> list = mAPI.getBlocks(owner);

        Toast.makeText(this, list.get(0).getPublicKey(), Toast.LENGTH_SHORT).show();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userNameTestSubject", owner.getName());
        editor.putString("ibanTestSubject", owner.getIban());
        editor.apply();

        Toast.makeText(this, list.get(0).getPublicKey(), Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, FriendsPageActivity.class));
    }
}
