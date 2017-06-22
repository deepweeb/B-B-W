package nl.tudelft.bbw.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import nl.tudelft.bbw.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TreeNode root = TreeNode.root();

        TreeNode parent = new TreeNode("MyParentNode");
        TreeNode child0 = new TreeNode("ChildNode0");
        TreeNode child1 = new TreeNode("ChildNode1");
        parent.addChildren(child0, child1);
        root.addChild(parent);

        AndroidTreeView tView = new AndroidTreeView(this, root);

        ((ConstraintLayout) findViewById(R.id.container)).addView(tView.getView());

        /*ListView tree = (ListView) findViewById(R.id.listTree);

        final ArrayNodes root = new ArrayNodes(
                new String[] { "Contacts", "Pairing" },
                new INodes[] { null, null }
        );

        tree.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return root.getLength();
            }

            @Override
            public Object getItem(int position) {
                return "Test";
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                TextView view = (TextView) inflater.inflate(R.layout.node, null);
                view.setText(root.getName(position));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                return view;
            }
        });*/
    }
}
