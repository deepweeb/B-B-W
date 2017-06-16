package nl.tudelft.bbw.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import nl.tudelft.bbw.R;
import nl.tudelft.bbw.controller.BlockController;
import nl.tudelft.bbw.model.HashException;
import nl.tudelft.bbw.model.User;
import nl.tudelft.bbw.model.block.Block;

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
        try {
            final String userName = settings.getString("userName", "");
            final String iban = settings.getString("iban", "");
            User user = new User(userName, iban);
            setTitle(ownerName + "'s contact list");
            BlockController blockController = new BlockController(this);
            List<Block> list = blockController.getBlocks(ownerName);
            setUpGraph(list);
            FriendsContactAdapter adapter = new FriendsContactAdapter(blockController, ownerName, user, this);
            ListView lView = (ListView) findViewById(R.id.contacts2);
            lView.setAdapter(adapter);
        } catch (HashException e) {
            Toast.makeText(this, "Hash error while retrieving blocks", Toast.LENGTH_LONG).show();
        }
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

