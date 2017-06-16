package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.model.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.model.HashException;

/**
 * This is the page you will see when you enter the app.
 */
public class MainActivity extends Activity {
    public static final String PREFS_NAME = "MyPrefsFile";
    private API mAPI;
    /**
     * The user of this app, containing it's information
     */
    private User user;

    /**
     * This method sets up the page.
     *
     * @param savedInstanceState passes in the old variables.
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            mAPI = new API(user, this);
        } catch (HashException e) {
            e.printStackTrace();
        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        }
        // add genesis if we don't have any blocks
        if (user == null && mAPI.isDatabaseEmpty()) {
            user = getUser();
        } else {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            EdDSAPrivateKey edDSAPrivateKey1 =
                    ED25519.generatePrivateKey(Utils.hexToBytes(
                            "0000000000000000000000000000000000000000000000000000000000000000"));
            EdDSAPublicKey ownerPublicKey = ED25519.getPublicKey(edDSAPrivateKey1);
            user = new User(settings.getString("userName", ""), settings.getString("iban", ""), ownerPublicKey);
        }
    }

    /**
     * Show a simple display asking the user what its name is and returns it.
     * This dialog is modal so the rest of the application will pause.
     * The username will be stored in the field ownerName..
     */
    public User getUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome!");
        builder.setMessage("Fill in your information");
        final EditText nameBox = new EditText(this);
        final EditText ibanBox = new EditText(this);
        nameBox.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        nameBox.setHint("Name");
        ibanBox.setInputType(InputType.TYPE_CLASS_TEXT);
        ibanBox.setHint("IBAN");
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(nameBox);
        ll.addView(ibanBox);
        builder.setView(ll);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EdDSAPrivateKey edDSAPrivateKey1 =
                        ED25519.generatePrivateKey(Utils.hexToBytes(
                                "0000000000000000000000000000000000000000000000000000000000000000"));
                EdDSAPublicKey ownerPublicKey = ED25519.getPublicKey(edDSAPrivateKey1);
                user = new User(nameBox.getText().toString(), ibanBox.getText().toString(), ownerPublicKey);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("userName", user.getName());
                editor.putString("iban", user.getIban());
                editor.apply();
            }
        });
        builder.show();
        return user;
    }


    /**
     * When you want to visit the PairActivity page.
     *
     * @param view The view
     */
    public final void onPairPage(View view) {
        Intent intent = new Intent(this, PairActivity.class);
        startActivity(intent);
    }


    /**
     * When you want to visit the ContactsPageActivity.
     *
     * @param view The view
     */
    public final void onContactsPage(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
}
