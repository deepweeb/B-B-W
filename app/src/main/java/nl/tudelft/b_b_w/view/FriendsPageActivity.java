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
import nl.tudelft.b_b_w.model.GetDatabaseHandler;
import nl.tudelft.b_b_w.model.User;

import static android.icu.lang.UProperty.BLOCK;
import static nl.tudelft.b_b_w.view.MainActivity.PREFS_NAME;

/**
 * This class displays the Friend Page. Here we want to be able to see the
 * iban number, the name and the rating of the person you paired with. Also
 * you are able to add this person to your contact list.
 */
public class FriendsPageActivity extends Activity {

    /**
     * block controller
     */
    private BlockController blockController;

    User contact;

    /**
     * the contact name
     */
    private String contactName;

    /**
     * the IBAN nummer of the contact
     */
    private String ibanNumber;

    /**
     * the public key of the contact
     */
    private String contactPublicKey;

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
        blockController = new BlockController(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        contact = new User(settings.getString("userNameTestSubject", ""), settings.getString("ibanTestSubject", ""));

        contactName = contact.getName();
        ibanNumber = contact.getIBAN();
        publicKey = contact.generatePublicKey();

        //Displaying the information of the contact whose you are paired with
        textViewIban = (TextView) findViewById(R.id.editIban);
        textViewContact = (TextView) findViewById(R.id.senderName);
        textViewIban.setText(contactName);
        textViewContact.setText(ibanNumber);

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
    public final void onAddThisPersonToContactList(View view) {

        String chainHash = NA;
        String senderHash = NA;
        String publicKey = user.generatePublicKey();
        String iban = user.getIBAN();
        ConversionController conversionController = new ConversionController(user.getName(), publicKey,
                chainHash, senderHash, iban);
        String hash = conversionController.hashKey();
        Block block = BlockFactory.getBlock(
                BLOCK,
                user.getName(),
                getLatestSeqNumber(user.getName()) + 1,
                hash,
                chainHash,
                senderHash,
                publicKey,
                iban,
                0
        );
        blockController.addBlock(block);

        Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }


}
