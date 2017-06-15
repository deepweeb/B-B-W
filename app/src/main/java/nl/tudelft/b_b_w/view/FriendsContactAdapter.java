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
import android.widget.Toast;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.controller.API;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.model.HashException;
import nl.tudelft.b_b_w.blockchain.User;

public class FriendsContactAdapter extends BaseAdapter implements ListAdapter {

    private final String retrievingHashError = "Hash error while retrieving blocks";
    //Variables which we use for getting the block information
    private API mAPI;
    private Context context;
    private String ownerName;
    private User user;
    //Images for displaying trust
    private Integer[] images = {R.drawable.pic5,
            R.drawable.pic4,
            R.drawable.pic3,
            R.drawable.pic2,
            R.drawable.pic1};

    /**
     * Default constructor to initiate the Adapter
     *
     * @param API     API which is passed on
     * @param context Context which is passed on
     */
    public FriendsContactAdapter(API API, String ownerName, User user, Context context) {
        this.context = context;
        this.ownerName = ownerName;
        this.user = user;
        this.mAPI = API;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem(int position) {
        return mAPI.getBlocks(user).get(position);
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
        return mAPI.getBlocks(user).size();
    }

    /**
     * Method to get the right image number
     *
     * @param trust The trust value
     * @return Image number
     */
    private int getImageNo(int trust) {
        return images[calculateImageIndex(trust)];
    }

    /**
     * Method to calculate the right index number of the array
     *
     * @param trust the trust value
     * @return the index
     */
    private int calculateImageIndex(int trust) {
        final int trustInterval = 20;
        double result = trust / trustInterval - 0.5;
        if (result < 0) {
            result = 0;
        }
        return (int) result;
    }

    /**
     * Method to create a popup to confirm your revoke
     *
     * @param position Current position of the view
     * @return The listener
     */
    private View.OnClickListener createDialog(final int position) throws HashException {
        final Block block = mAPI.getBlocks(user).get(position);
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to add "
                        + block.getBlockOwner()
                        + " IBAN?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            final byte[] message = block.getContactPublicKey().getEncoded();
                            final byte[] signature = ED25519.generateSignature(
                                    message, user.getPrivateKey());
                            mAPI.addContactToChain(block.getContact(), signature, message);
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
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.simple_list_item_2, parent, false);
        }
        try {
            TextView nameItemText = (TextView) view.findViewById(R.id.list_item_name2);
            nameItemText.setText(blockController.getContactName(blockController.getBlocks(ownerName).get(position).getOwnHash()));
            TextView ibanItemText = (TextView) view.findViewById(R.id.list_item_iban2);
            ibanItemText.setText(blockController.getBlocks(ownerName).get(position).getOwner().getIban());
            ImageView pic = (ImageView) view.findViewById(R.id.trust_image2);
            pic.setImageResource(
                    getImageNo((int) Math.round(blockController.getBlocks(ownerName).get(position).getTrustValue())));
            Button addButton = (Button) view.findViewById(R.id.add_btn);
            addButton.setOnClickListener(createDialog(position));
            return view;
        } catch (HashException e) {
            Toast.makeText(context, retrievingHashError, Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
