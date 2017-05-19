package nl.tudelft.b_b_w;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import nl.tudelft.b_b_w.Controllers.BlockController;
import nl.tudelft.b_b_w.Models.Block;
import nl.tudelft.b_b_w.Models.Conversion;
import nl.tudelft.b_b_w.Models.DatabaseHandler;

/**
 * When the user wants to add a block he enters into the AddBlockActivity, which contain
 * some entry fields and a button to confirm the addition.
 */
public class AddBlockActivity extends Activity {
    /**
     * Connection with block database
     */
    private DatabaseHandler handler;

    /**
     * Controller of blocks
     */
    private BlockController blockController;

    /**
     * Owner of the block
     */
    private String ownerName;

    /**
     * Public key that we want to add
     */
    private String publicKey;

    /**
     * On create we request a database connection
     * @param savedInstanceState unused, meant for serialisation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addblock);
        handler = new DatabaseHandler(this);
        blockController = new BlockController(this);
        Bundle extras = getIntent().getExtras();
        ownerName = extras.getString("ownerName");
        publicKey = extras.getString("publicKey");
        Toast.makeText(this, ownerName + ", " + publicKey, Toast.LENGTH_SHORT).show();
    }

    /**
     * Upon adding a block, information is extracted from the GUI and put into the fresh block,
     * which is added to the database.
     * @param view current view (AddBlockActivity)
     */
    public void onAddBlock(View view) {

        try {
            // extract information
            EditText senderHashText = (EditText) findViewById(R.id.addSenderHash);
            EditText senderPublicKeyText = (EditText) findViewById(R.id.addPublicKey);
            String senderHash = senderHashText.getText().toString();
            String senderPublicKey = senderPublicKeyText.getText().toString();


            // create and add the block
            Block previous = handler.getLatestBlock(ownerName);

            Conversion conversion = new Conversion(ownerName, senderPublicKey, previous.getOwnHash(), senderHash);
            String ownHash = conversion.hashKey();
            Block block = new Block(
                    ownerName, // owner of a block
                    999999, // addBlock will overwrite this, TODO remove from constructor
                    ownHash, // our own hash
                    previous.getOwnHash(), //  the hash value of the block before in the chain
                    senderHash, // the hash value of the block before of the sender
                    senderPublicKey, // public key of the owner of the block
                    false // is revoked?
            );
            handler.addBlock(block);

            // confirm by showing a small text message
            Toast.makeText(this, "Block added", Toast.LENGTH_SHORT).show();

            // switch back to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Block added failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
