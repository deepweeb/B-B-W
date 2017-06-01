package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.block.Block;

public class DisplayChainActivity extends Activity {
    private BlockController controller;
    private String ownerName;
    private String publicKey;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaychain);

        controller = new BlockController(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ownerName = extras.getString("ownerName");
            publicKey = extras.getString("publicKey");
        }

        /*ListView view = (ListView) findViewById(R.id.chain);

        List<Block> blocks = handler.getAllBlocks(ownerName);
        ArrayList<Block> array = new ArrayList<Block>(blocks);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_displaychain);//, array);
        //new ArrayAdapter<>()
        view.setAdapter(adapter);

        Genesis:eigen pubkey
        Genesis: sender 1 key*/

        try {
            List<Block> blocks = controller.getBlocks(ownerName);

            TextView view = (TextView) findViewById(R.id.chain);

            String result = "";
            for (Block block : blocks) {
                if (block.isRevoked())
                    result = result + "REVOKE ";
                result = result + block.getSequenceNumber() + "\t" + block.getPublicKey() + "\t" + block.getOwnHash() + "\n\n";
            }

            view.setText(result);
        } catch (HashException e) {
            Toast.makeText(this, "Hash error while retrieving blocks", Toast.LENGTH_LONG).show();
        }
    }
}
