package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.controller.ConversionController;
import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;
import nl.tudelft.b_b_w.model.User;

import static nl.tudelft.b_b_w.view.MainActivity.PREFS_NAME;

/**
 * This class displays the Friend Page. Here we want to be able to see the
 * iban number, the name and the rating of the person you paired with. Also
 * you are able to add this person to your contact list.
 */
public class FriendsPageActivity extends Activity {

    /**
     * Block controller
     */
    private BlockController bc;

    /**
     * Block argument to create a block
     */
    private static final String TYPE_BLOCK = "BLOCK";

    /**
     * This is your own user
     */
    private User user;

    /**
     * This is the paired contact
     */
     private User contact;

    /**
     * The contact name
     */
    private String contactName;

    /**
     * The iban nummer of the contact
     */
    private String ibanNumber;

    /**
     * The public key of the contact
     */
    private String publicKey;

    /**
     * Block hash of the paired contact's genesis
     */
    private String contactGenesisBlockHash;

    /**
     * The hash of your latest block
     */
    private String userLatestBlockHash;

    /**
     * Text view of the iban number.
     */
    private TextView textViewIban;

    /**
     * Text view of the contact name.
     */
    private TextView textViewContact;


    /**
     * On create method, here we request a database connection
     * @param savedInstanceState
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        bc = new BlockController(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        contact = new User(settings.getString("userNameTestSubject", ""), settings.getString("ibanTestSubject", ""));
        user = new User(settings.getString("userName", ""), settings.getString("iban", ""));

        contactName = contact.getName();
        ibanNumber = contact.getIBAN();
        publicKey = contact.generatePublicKey();

        userLatestBlockHash = bc.getBlocks(user.getName()).get(bc.getBlocks(user.getName()).size()-1).getOwnHash();
        contactGenesisBlockHash = bc.getBlocks(contactName).get(0).getOwnHash();
        //Displaying the information of the contact whose you are paired with
        textViewIban = (TextView) findViewById(R.id.editIban);
        textViewContact = (TextView) findViewById(R.id.senderName);
        textViewIban.setText(ibanNumber);
        textViewContact.setText(contactName);

    }

       /**
     * When you want to visit the DisplayContactFriendListActivity page.
     * @param view  The view
     */
    public final void onViewContactListPage(View view) {
        Intent intent = new Intent(this, DisplayContactFriendListActivity.class);
        startActivity(intent);
    }

    /**
     * When you want to add this person to your own contact list page.
     * @param view  The view
     */
    public final void onAddThisPersonToContactList(View view) throws Exception {

        ConversionController conversionController = new ConversionController(user.getName(), publicKey,
                userLatestBlockHash, contactGenesisBlockHash, ibanNumber);
        String hash = conversionController.hashKey();
        Block block = BlockFactory.getBlock(
                TYPE_BLOCK,
                user.getName(),
                bc.getLatestSeqNumber(user.getName()) + 1,
                hash,
                userLatestBlockHash,
                contactGenesisBlockHash,
                publicKey,
                ibanNumber,
                0 // trust value still not implemented
        );

        try {bc.addBlock(block);}
        catch(Exception e) {Toast.makeText(this, "Sorry, this contact is already added!",
                Toast.LENGTH_SHORT).show();
            return;}

        Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }


}
