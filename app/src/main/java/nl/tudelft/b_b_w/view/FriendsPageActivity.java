package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.GetDatabaseHandler;

/**
 * This class displays the Friend Page. Here we want to be able to see the
 * iban number, the name and the rating of the person you paired with. Also
 * you are able to add this person to your contact list.
 */
public class FriendsPageActivity extends Activity {

    /**
     * Used to create connection with database
     */
    private GetDatabaseHandler getDatabaseHandler;

    /**
     * block controller
     */
    private BlockController blockController;

    /**
     * the owner of the block
     */
    private String ownerName;

    /**
     * the IBAN nummer of the owner
     */
    private String ibanNumber;

    /**
     * The public key of the block
     */
    private String publicKey;

    /**
     * Text view of the iban number.
     */
    private TextView textViewIban;

    /**
     * Text view of the public key.
     */
    private TextView textViewPubkey;

    /**
     * Text view of the owner name.
     */
    private TextView textViewOwner;


    /**
     * On create method, here we request a database connection
     * @param savedInstanceState
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        // int userID = savedInstanceState.getInt("userID");
        //User user = Api.getUser(userID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);

        Intent myIntent = getIntent();
        String iban = myIntent.getStringExtra("IBAN");
        String pubKey = myIntent.getStringExtra("PUBKEY");
        String ownerName= myIntent.getStringExtra("OWNER");


        textViewIban = (TextView) findViewById(R.id.editIban);
        textViewPubkey = (TextView) findViewById(R.id.editPubKey);
        textViewOwner = (TextView) findViewById(R.id.senderName);


        textViewIban.setText(iban);
        textViewPubkey.setText(pubKey);
        textViewOwner.setText(ownerName);

    }



}
