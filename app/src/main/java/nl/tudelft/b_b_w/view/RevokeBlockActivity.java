package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.controller.ConversionController;
import nl.tudelft.b_b_w.model.block.Block;
import nl.tudelft.b_b_w.model.block.BlockFactory;


public class RevokeBlockActivity extends Activity {


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
     *
     * @param savedInstanceState unused, meant for serialisation
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revokeblock);
        blockController = new BlockController(this);
        Bundle extras = getIntent().getExtras();
        ownerName = extras.getString("ownerName");
        publicKey = extras.getString("publicKey");
        Toast.makeText(this, ownerName + ", " + publicKey, Toast.LENGTH_SHORT).show();
    }

    /**
     * Upon revoking a block, information is extracted from the GUI and put into the fresh revoke block
     * which is added to the database.
     *
     * @param view current view (RevokeBlockActivity)
     */
    public final void onRevokeBlock(View view) {
        try {
            // extract information
            EditText senderHashText = (EditText) findViewById(R.id.revokeSenderHash);
            EditText senderPublicKeyText = (EditText) findViewById(R.id.revokePublicKey);
            String senderHash = senderHashText.getText().toString();
            String senderPublicKey = senderPublicKeyText.getText().toString();

            //URGENCY: THIS NEED TO BE A SEPARATE INPUT FIELD FOR IBAN NUMBER
            String senderIban = senderPublicKeyText.getText().toString();

            // create and add the block
            final Block previous = blockController.getLatestBlock(ownerName);
            final int blockSeqNumber = previous.getSequenceNumber()+ 1;
            ConversionController conversionController = new ConversionController(ownerName, senderPublicKey, previous.getOwnHash(), senderHash,senderIban);
            String ownHash = conversionController.hashKey();

            Block block =  BlockFactory.getBlock(
                    "REVOKE", // is revoked?
                    ownerName,// owner of the block
                    blockController.getLatestSeqNumber(ownerName) + 1,
                    ownHash, // our own hash
                    previous.getOwnHash(), //  the hash value of the block before in the chain
                    senderHash, // the hash value of the block before of the sender
                    senderPublicKey, // public key of the owner of the block
                    senderIban, //iban of the sender
                    0 // initial trust value
            );


            blockController.addBlock(block);

            // confirm by showing a small text message
            Toast.makeText(this, "Revoke block added", Toast.LENGTH_SHORT).show();

            // switch back to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Revoke block failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
