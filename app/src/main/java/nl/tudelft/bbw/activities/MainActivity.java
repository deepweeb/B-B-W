package nl.tudelft.bbw.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import nl.tudelft.bbw.R;
import nl.tudelft.bbw.TreeAdapter;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ExpandableListView tree = (ExpandableListView) findViewById(R.id.tree);

        tree.setAdapter(new TreeAdapter(this.getBaseContext(),
                new String[] {"Alice", "Bob", "Carol"},
                new String[][]{
                        new String[]{"A1", "A2"},
                        new String[]{"B1", "B2"},
                        new String[]{"C1", "C2"},
                }
        ));

    }
}
