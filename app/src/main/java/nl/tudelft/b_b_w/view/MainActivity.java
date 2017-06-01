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
import android.widget.Toast;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.User;

/**
 * This is the page you will see when you enter the app.
 */
public class MainActivity extends Activity {
    private BlockController blockController;
    public static final String PREFS_NAME = "MyPrefsFile";


    /** The user of this app, containing it's information */
    private User user;

    /**
     * This method sets up the page.
     * @param savedInstanceState    passes in the old variables.
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blockController = new BlockController(this);
        // add genesis if we don't have any blocks
        if (user == null && blockController.isDatabaseEmpty()) {
            user = getUser();
        } else {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            user = new User(settings.getString("userName", ""), settings.getString("iban", ""));
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
                user = new User(nameBox.getText().toString(), ibanBox.getText().toString());
                addGenesis();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("userName", user.getName());
                editor.putString("iban", user.getIBAN());
                editor.apply();
            }
        });
        builder.show();
        return user;
    }

    /**
     * Add a genesis block so that the database is not empty.
     * The genesis block has as previous chain hash "GENESIS" since it has no previous,
     * and as sender hash "N/A" as is usual with blocks without sender.
     */
    private void addGenesis() {
        try {
            blockController.createGenesis(user);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
   }


    /**
     * When you want to visit the PairActivity page.
     * @param view  The view
     */
    public final void onPairPage(View view) {
        Intent intent = new Intent(this, PairActivity.class);
        startActivity(intent);
    }


    /**
     * When you want to visit the ContactsPageActivity.
     * @param view The view
     */
    public final void onContactsPage(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
}
