package nl.tudelft.b_b_w;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import nl.tudelft.b_b_w.Controllers.BlockController;
import nl.tudelft.b_b_w.Models.Block;
import nl.tudelft.b_b_w.Models.DatabaseHandler;

public class MainActivity extends Activity {
    private DatabaseHandler handler;
    private BlockController blockController;
    private String ownerName;
    private String publicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new DatabaseHandler(this);
        blockController = new BlockController(this);
        ownerName = "GENESIS";
        publicKey = "demokey";

        //test method
        int n = handler.lastSeqNumberOfChain(ownerName);
        Toast.makeText(this, "#" + n, Toast.LENGTH_LONG).show();

        // add genesis if we don't have any blocks
        if (handler.lastSeqNumberOfChain(ownerName) == 0)
            addGenesis();
    }

    /**
     * Add a genesis block so that the database is not empty
     */
    private void addGenesis() {
        try {
            if (handler.getAllBlocks(ownerName).isEmpty()) {
                Block block = new Block(ownerName, 1, "ownhash", "previoushash", "senderhash", "senderpubkey", false);
                blockController.addBlock(block);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Callback for when user clicks on 'add block'. Switch to AddBlockActivity.
     * @param view current view, which is always MainActivity
     */
    public void onAddBlock(View view) {
        Intent intent = new Intent(this, AddBlockActivity.class);
        intent.putExtra("ownerName", ownerName);
        intent.putExtra("publicKey", publicKey);
        startActivity(intent);
    }


    /**
     * Callback for when user clicks on 'delete block'. Switch to RevokeBlockActivity.
     * @param view current view, which is always MainActivity
     */
    public void onRevokeBlock(View view) {
        Intent intent = new Intent(this, RevokeBlockActivity.class);
        intent.putExtra("ownerName", ownerName);
        intent.putExtra("publicKey", publicKey);
        startActivity(intent);
    }

    public void onDisplayChain(View view) {
        Intent intent = new Intent(this, DisplayChainActivity.class);
        intent.putExtra("ownerName", ownerName);
        intent.putExtra("publicKey", publicKey);
        startActivity(intent);
    }
}
