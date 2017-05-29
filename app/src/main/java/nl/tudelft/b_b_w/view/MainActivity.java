package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.controller.ConversionController;
import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;

/**
 * This is the page you will see when you enter the app.
 */
public class MainActivity extends Activity {
    private BlockController blockController;

    /** The name of this app user */
    private String ownerName;

    /**
     * This method sets up the page.
     * @param savedInstanceState    passes in the old variables.
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blockController = new BlockController(this);

        if (blockController.isDatabaseEmpty()) {
            askUserName();
        }
    }

    /**
     * Show a simple display asking the user what its name is and returns it.
     * This dialog is modal so the rest of the application will pause.
     * The username will be stored in the field ownerName..
     */
    private void askUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What is your name?");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ownerName = input.getText().toString();
                addGenesis();
            }
        });

        builder.show();

    }

    /**
     * Add a genesis block so that the database is not empty.
     * The genesis block has as previous chain hash "GENESIS" since it has no previous,
     * and as sender hash "N/A" as is usual with blocks without sender.
     */
    private void addGenesis() {

        ConversionController cvc = new ConversionController(ownerName, "pubkey", "N/A", "N/A", "Iban");
        try {
            Block block = BlockFactory.getBlock(
                    "BLOCK",
                    ownerName,
                    cvc.hashKey(),
                    "N/A",
                    "N/A",
                    "pubkey",
                    "Iban",
                    0
            );
            blockController.addBlock(block);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method clears the database since a limited amount of data can be viewed.
     * @param view  The view of the program.
     */
    public final void onClearDatabase(View view) {
        blockController.clearAllBlocks();
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
     * When you want to visit the FriendsPageActivity page.
     * @param view The view
     */
    public final void onFriendPage(View view) {
        Intent intent = new Intent(this, FriendsPageActivity.class);
        startActivity(intent);
    }

    /**
     * When you want to visit the ContactsPageActivity.
     * @param view The view
     */
    public final void onContactsPage(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        intent.putExtra("ownerName", ownerName);
        startActivity(intent);
    }
}
