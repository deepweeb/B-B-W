package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.GetDatabaseHandler;
import nl.tudelft.b_b_w.model.User;

import static nl.tudelft.b_b_w.view.MainActivity.PREFS_NAME;

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

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        User user = new User(settings.getString("userNameTestSubject", ""), settings.getString("ibanTestSubject", ""));

        textViewIban = (TextView) findViewById(R.id.editIban);
        textViewOwner = (TextView) findViewById(R.id.senderName);

        textViewIban.setText(user.getIBAN());
        textViewOwner.setText(user.getName());

    }



}
