package nl.tudelft.bbw.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import nl.tudelft.bbw.R;
import nl.tudelft.bbw.controller.BlockController;
import nl.tudelft.bbw.model.HashException;
import nl.tudelft.bbw.model.block.Block;

/**
 * When the user wants to add a block he enters into the ContactsActivity, which contain
 * some entry fields and a button to confirm the addition.
 */
public class ContactsActivity extends Activity {

    /**
     * On create we request a database connection
     *
     * @param savedInstanceState unused, meant for serialisation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitle("Contacts");
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        final String ownerName = settings.getString("userName", "");
        BlockController blockController = new BlockController(this);
        try {
            setUpGraph(blockController.getBlocks(ownerName));
        } catch (HashException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        ContactAdapter adapter = new ContactAdapter(blockController, ownerName, this);
        ListView lView = (ListView) findViewById(R.id.contacts);
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

    /**
     * Button to go to the page where transactions are done
     *
     * @param view The view of the app.
     */
    public final void onTransaction(View view) {
        Intent intent = new Intent(this, TransactionActivity.class);
        startActivity(intent);
    }

}
