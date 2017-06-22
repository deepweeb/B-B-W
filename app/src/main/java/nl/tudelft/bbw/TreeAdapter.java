package nl.tudelft.bbw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class TreeAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private String[][] mContents;
    private String[] mTitles;

    public TreeAdapter(Context context, String[] titles, String[][] contents) {
        super();
        if(titles.length != contents.length) {
            throw new IllegalArgumentException("Titles and Contents must be the same size.");
        }

        mContext = context;
        mContents = contents;
        mTitles = titles;
    }
    @Override
    public String getChild(int groupPosition, int childPosition) {
        return mContents[groupPosition][childPosition];
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (true || groupPosition == 0 || childPosition == 0) {
            try {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ExpandableListView child = (ExpandableListView) inflater.inflate(R.layout.tree, null);
                child.setAdapter(new TreeAdapter(mContext,
                        new String[] {"Sub", "Subber"},
                        new String[][] {new String[] {"hoi", "hee"}, new String[] {"he"}}));
                child.setPadding(150,0,0,0);
                return child;
            } catch (Exception e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        TextView row = (TextView)convertView;
        if(row == null) {
            row = new TextView(mContext);
        }
        row.setText(mContents[groupPosition][childPosition]);
        row.setTextSize(30.0f);
        return row;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return mContents[groupPosition].length;
    }
    @Override
    public String[] getGroup(int groupPosition) {
        return mContents[groupPosition];
    }
    @Override
    public int getGroupCount() {
        return mContents.length;
    }
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        TextView row = (TextView)convertView;
        if(row == null) {
            row = new TextView(mContext);
        }
        //row.setTypeface(Typeface.DEFAULT_BOLD);
        row.setText(mTitles[groupPosition]);
        row.setPadding(150,0,0,0);
        row.setTextSize(30.0f);
        return row;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}