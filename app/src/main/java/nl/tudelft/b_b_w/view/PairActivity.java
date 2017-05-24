package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;

import static android.R.attr.id;

/**
 * This activity
 */
public class PairActivity extends Activity {
    /**
     * The block controller.
     */
    private BlockController blockController;

    /**
     * It's own block.
     */
    private Block block1;

    /**
     * a block.
     */
    private Block block2;

    /**
     * a block.
     */
    private Block block3;

    /**
     * a block.
     */
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);
    }


    /**
     * This method create the first test subject.We do this in order to simulate a transaction.
     * @param view The view of the program.
     */
    public void onTestSubject1(View view) {
        ownerName = "TestSubject1";
        blockController = new BlockController(this);
        block1 = BlockFactory.getBlock("BLOCK", ownerName, "HASH1","N/A","N/A","TestSubject_PUBKEY","IBANTestSubject1",0);
        block2 = BlockFactory.getBlock("BLOCK", ownerName, "HASH2","HASH1","HASHfromContact1","Contact1_PUBKEY","IBANContact1",0);
        block3 = BlockFactory.getBlock("BLOCK", ownerName, "HASH3","HASH2","HASHfromContact2","Contact2_PUBKEY","IBANContact2",0);
        block4 = BlockFactory.getBlock("BLOCK", ownerName, "HASH4","HASH3","HASHfromContact3","Contact3_PUBKEY","IBANContact3",0);

        blockController.addBlock(block1);
        blockController.addBlock(block2);
        blockController.addBlock(block3);
        blockController.addBlock(block4);

        List<Block> list = blockController.getBlocks(ownerName);

        Toast.makeText(this, list.get(0).getPublicKey() + ", " +
                list.get(1).getPublicKey() + ", " +
                list.get(2).getPublicKey() + ", " +
                list.get(3).getPublicKey(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, FriendsPageActivity.class);
        intent.getExtras().putInt("userID", id);
        startActivity(intent);
    }

    /**
     * This method creates another test subject(second). It is hardcoded and will be change later on.
     * We do this to simulate a transaction.
     * @param view The view of the program.
     */
    public void onTestSubject2(View view) {

        Block block5;

        ownerName = "TestSubject2";
        blockController = new BlockController(this);
        block1 = BlockFactory.getBlock("BLOCK", ownerName, "HASH1","N/A","N/A","a","IBANTestSubject1",0);
        block2 = BlockFactory.getBlock("BLOCK", ownerName, "HASH2","HASH1","HASHfromContact1","b","IBANContact1",0);
        block3 = BlockFactory.getBlock("BLOCK", ownerName, "HASH3","HASH2","HASHfromContact2","c","IBANContact2",0);
        block4 = BlockFactory.getBlock("BLOCK", ownerName, "HASH4","HASH3","HASHfromContact3","d","IBANContact3",0);
        block5 = BlockFactory.getBlock("BLOCK", ownerName, "HASH5","Hash4","HASHfromContact4","e","IBANContact4",0);


        blockController.addBlock(block1);
        blockController.addBlock(block2);
        blockController.addBlock(block3);
        blockController.addBlock(block4);
        blockController.addBlock(block5);

        List<Block> list = blockController.getBlocks(ownerName);

        Toast.makeText(this, list.get(0).getPublicKey() + ", " +
                list.get(1).getPublicKey() + ", " +
                list.get(2).getPublicKey() + ", " +
                list.get(3).getPublicKey() + ", " +
                list.get(4).getPublicKey(), Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, FriendsPageActivity.class));
    }

    /**
     * This method creates another test subject (third). It is hardcoded and will be change later on.
     * We do this to simulate a transaction.
     * @param view  The view of the program.
     */
    public void onTestSubject3(View view) {
        ownerName = "TestSubject2";
        blockController = new BlockController(this);
        block1 = BlockFactory.getBlock("BLOCK", ownerName, "HASH1","N/A","N/A","sub3KeyA","IBANTestSubject1",0);
        block2 = BlockFactory.getBlock("BLOCK", ownerName, "HASH2","HASH1","HASHfromContact1","sub3KeyB","IBANContact1",0);


        blockController.addBlock(block1);
        blockController.addBlock(block2);


        List<Block> list = blockController.getBlocks(ownerName);

        Toast.makeText(this, list.get(0).getPublicKey() + ", " +
                list.get(1).getPublicKey() + ", " +
                list.get(2).getPublicKey(), Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, FriendsPageActivity.class));

    }

}
