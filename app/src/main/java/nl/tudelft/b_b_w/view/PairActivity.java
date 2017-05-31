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
import nl.tudelft.b_b_w.model.BlockFactory;

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
    }


    /**
     * This method create the first test subject.We do this in order to simulate a transaction.
     * @param view The view of the program.
     */
    public final void onTestSubject1(View view) {
        final String ibanTestSub1= "IBANTestSubject1";
        ownerName = "TestSubject1";
        blockController = new BlockController(this);
        block1 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH1",
                "N/A",
                "N/A",
                "TestSubject_PUBKEY",
                ibanTestSub1,
                0
        );
        blockController.addBlock(block1);
        block2 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH2",
                "HASH1",
                "HASHfromContact1",
                "Contact1_PUBKEY",
                "IBANContact1",
                0
        );
        blockController.addBlock(block2);
        block3 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH3",
                "HASH2",
                "HASHfromContact2",
                "Contact2_PUBKEY",
                "IBANContact2",
                0
        );
        blockController.addBlock(block3);
        block4 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH4",
                "HASH3",
                "HASHfromContact3",
                "Contact3_PUBKEY",
                "IBANContact3",
                0
        );
        blockController.addBlock(block4);

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
    public final void onTestSubject2(View view) {

        Block block5;
        final String ibanTestSub2= "IBANTestSubject2";

        ownerName = "TestSubject2";
        blockController = new BlockController(this);
        block1 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH1",
                "N/A",
                "N/A",
                "a",
                ibanTestSub2,
                0
        );
        blockController.addBlock(block1);
        block2 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH2",
                "HASH1",
                "HASHfromContact1",
                "b",
                "IBANContact2",
                0
        );
        blockController.addBlock(block2);
        block3 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH3",
                "HASH2",
                "HASHfromContact2",
                "c",
                "IBANContact3",
                0
        );
        blockController.addBlock(block3);
        block4 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH4",
                "HASH3",
                "HASHfromContact3",
                "d",
                "IBANContact4",
                0
        );
        blockController.addBlock(block4);

        block5 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH5",
                "HASH4",
                "HASHfromContact4",
                "e",
                "IBANContact5",
                0
        );
        blockController.addBlock(block5);

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

        final String ibanTestSub3= "IBANTestSubject3";
        ownerName = "TestSubject3";
        blockController = new BlockController(this);
        block1 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH1",
                "N/A",
                "N/A",
                "sub3KeyA",
                ibanTestSub3,
                0
        );
        blockController.addBlock(block1);
        block2 = BlockFactory.getBlock(
                "BLOCK",
                ownerName,
                blockController.getLatestSeqNumber(ownerName) + 1,
                "HASH2",
                "HASH1",
                "HASHfromContact1",
                "sub3KeyB",
                "IBANContact1",
                0
        );
        blockController.addBlock(block2);

        List<Block> list = blockController.getBlocks(ownerName);

        Toast.makeText(this, list.get(0).getPublicKey() + ", " +
                list.get(1).getPublicKey(), Toast.LENGTH_SHORT).show();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userNameTestSubject", ownerName);
        editor.putString("ibanTestSubject", ibanTestSub3);
        editor.apply();

        startActivity(new Intent(this, FriendsPageActivity.class));

    }

}