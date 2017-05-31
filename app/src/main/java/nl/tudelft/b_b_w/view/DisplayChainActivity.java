package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.Block;

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


        List<Block> blocks = controller.getBlocks(ownerName);

        TextView view = (TextView) findViewById(R.id.chain);

        String result = "";
        for (Block block : blocks) {
            if (block.isRevoked())
                result = result + "REVOKE ";
            result = result + block.getSequenceNumber() + "\t" + block.getPublicKey() + "\t" + block.getOwnHash() + "\n\n";
        }
        view.setText(result);
    }

}
