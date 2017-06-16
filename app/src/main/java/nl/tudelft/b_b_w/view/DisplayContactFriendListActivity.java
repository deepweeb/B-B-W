package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import java.util.List;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.model.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.model.HashException;

/**
 * When the user wants to see a list of friends of the contact he just paired he enters into the
 * DisplayContactFriendListActivity, which contain some entry fields.
 */
public class DisplayContactFriendListActivity extends Activity {

    /**
     * On create we request a database connection
     *
     * @param savedInstanceState unused, meant for serialisation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_contacts);
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        final String ownerName = settings.getString("userNameTestSubject", "");
        final String userName = settings.getString("userName", "");
        final String iban = settings.getString("iban", "");
        EdDSAPrivateKey edDSAPrivateKey1 =
                ED25519.generatePrivateKey(Utils.hexToBytes(
                        "0000000000000000000000000000000000000000000000000000000000000000"));
        EdDSAPublicKey ownerPublicKey = ED25519.getPublicKey(edDSAPrivateKey1);
        User user = new User(userName, iban, ownerPublicKey);
        setTitle(ownerName + "'s contact list");
        API API = null;
        try {
            API = new API(user, this);
        } catch (HashException e) {
            e.printStackTrace();
        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        }
        List<Block> list = null;
        try {
            list = API.getBlocks(user);
        } catch (HashException e) {
            e.printStackTrace();
        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        }
        setUpGraph(list);
        FriendsContactAdapter adapter = new FriendsContactAdapter(API, ownerName, user, this);
        ListView lView = (ListView) findViewById(R.id.contacts2);
        lView.setAdapter(adapter);
    }

    /**
     * Setting up the graph
     *
     * @param blocks The blocks where the values for the graph are extracted from
     */
    public void setUpGraph(List<Block> blocks) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(blocks.size());
        DataPoint[] points = new DataPoint[blocks.size()];
        for (int i = 0; i < blocks.size(); i++) {
            points[i] = new DataPoint(i, blocks.get(i).getTrustValue());
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        graph.addSeries(series);
    }
}

