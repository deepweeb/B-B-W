package nl.tudelft.b_b_w.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.model.User;

public class FriendsContactAdapter extends BaseAdapter implements ListAdapter {

    //Variables which we use for getting the block information
    private BlockController blockController;
    private Context context;
    private String ownerName;
    private User user;
    //Images for displaying trust
    private Integer images[] = {R.drawable.pic5,
            R.drawable.pic4,
            R.drawable.pic3,
            R.drawable.pic2,
            R.drawable.pic1};

    /**
     * Default constructor to initiate the Adapter
     * @param bc BlockController which is passed on
     * @param context Context which is passed on
     */
    public FriendsContactAdapter(BlockController bc, String ownerName, User user, Context context) {
        this.context = context;
        this.ownerName = ownerName;
        this.user = user;
        this.blockController = bc;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public Object getItem(int position) {
        try {
            return blockController.getBlocks(ownerName).get(position);
        } catch (HashException e) {
            e.printStackTrace();
        }
    return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        try {
            return blockController.getBlocks(ownerName).size();
        } catch (HashException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * Method to get the right image number
     * @param trust The trust value
     * @return Image number
     */
    private int getImageNo(int trust) {
        return images[calculateImageIndex(trust)];
    }

    /**
     * Method to calculate the right index number of the array
     * @param trust the trust value
     * @return the index
     */
    private int calculateImageIndex(int trust) {
        final int trustInterval = 20;
        double result = trust/ trustInterval - 0.5;
        if (result < 0) result = 0;
        return (int) result;
    }

    /**
     * Method to create a popup to confirm your revoke
     * @param position Current position of the view
     * @return The listener
     */
    private View.OnClickListener createDialog(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                try {
                    builder.setMessage("Are you sure you want to add "
                            + blockController.backtrack(blockController.getBlocks(ownerName).get(position)).getOwner()
                            + " IBAN?");
                } catch (HashException e) {
                    e.printStackTrace();
                }
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            blockController.createKeyBlock(user,
                                    blockController.backtrack(
                                            blockController.getBlocks(ownerName).get(position)).getOwner(),
                                    blockController.getBlocks(ownerName).get(
                                            position).getPublicKey());
                        } catch (Exception e) {
                            //do nothing.
                        }
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
    }

    /**
     * The getview method is becoming too large, we should considering refactoring it in the future.
     * {@inheritDoc}
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.simple_list_item_2, null);
        }
        TextView nameItemText = (TextView)view.findViewById(R.id.list_item_name2);
        try {
            nameItemText.setText(blockController.getContactName(blockController.getBlocks(ownerName).get(position).getOwnHash()));
        } catch (HashException e) {
            e.printStackTrace();
        }
        TextView ibanItemText = (TextView)view.findViewById(R.id.list_item_iban2);
        try {
            ibanItemText.setText(blockController.getBlocks(ownerName).get(position).getOwner().getIBAN());
        } catch (HashException e) {
            e.printStackTrace();
        }
        ImageView pic = (ImageView)view.findViewById(R.id.trust_image2);
        try {
            pic.setImageResource(
                    getImageNo(blockController.getBlocks(ownerName).get(position).getTrustValue()));
        } catch (HashException e) {
            e.printStackTrace();
        }
        Button addButton = (Button)view.findViewById(R.id.add_btn);
        addButton.setOnClickListener(createDialog(position));
        return view;
    }

}
