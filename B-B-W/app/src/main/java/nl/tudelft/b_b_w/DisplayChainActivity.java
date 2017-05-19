package nl.tudelft.b_b_w;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import nl.tudelft.b_b_w.Models.Block;
import nl.tudelft.b_b_w.Models.DatabaseHandler;

public class DisplayChainActivity extends Activity {
    private DatabaseHandler handler;
    private String ownerName;
    private String publicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaychain);

        handler = new DatabaseHandler(this);
        Bundle extras = getIntent().getExtras();
        ownerName = extras.getString("ownerName");
        publicKey = extras.getString("publicKey");

        /*ListView view = (ListView) findViewById(R.id.chain);

        List<Block> blocks = handler.getAllBlocks(ownerName);
        ArrayList<Block> array = new ArrayList<Block>(blocks);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_displaychain);//, array);
        //new ArrayAdapter<>()
        view.setAdapter(adapter);

        Genesis:eigen pubkey
        Genesis: sender 1 key*/

        List<Block> blocks = handler.getAllBlocks(ownerName);

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
