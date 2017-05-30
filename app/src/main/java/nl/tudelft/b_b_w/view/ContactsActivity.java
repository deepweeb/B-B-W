package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.Block;

/**
 * When the user wants to add a block he enters into the ContactsActivity, which contain
 * some entry fields and a button to confirm the addition.
 */
public class ContactsActivity extends Activity {

    //Owner of the blockchain
    private String ownerName;

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
        Bundle extras = getIntent().getExtras();
        ownerName = extras.getString("ownerName");
        // get contacts
        BlockController blcController = new BlockController(this);
        setUpGraph(blcController.getBlocks(ownerName));
        ContactAdapter adapter = new ContactAdapter(blcController, ownerName, this);
        ListView lView = (ListView)findViewById(R.id.contacts);
        lView.setAdapter(adapter);
    }

    /**
     * Setting up the graph
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

