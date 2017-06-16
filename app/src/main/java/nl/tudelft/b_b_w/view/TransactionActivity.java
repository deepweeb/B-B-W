package nl.tudelft.b_b_w.view;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import nl.tudelft.b_b_w.API;
import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.model.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.model.HashException;

import static nl.tudelft.b_b_w.view.MainActivity.PREFS_NAME;

/**
 * Page which handles transactions between user and a contact.
 */
public class TransactionActivity extends Activity {

    //Variables which are frequently used
    private API mAPI;
    private User user;
    private Spinner dialog;
    private String transactionName;

    /**
     * Default method which executes all tasks on creation
     *
     * @param savedInstanceState The current state, which is kept in memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        EdDSAPrivateKey edDSAPrivateKeyOwner1 =
                ED25519.generatePrivateKey(Utils.hexToBytes(
                        "1000000000000000000000000000000000000000000000000000000000000001"));
        EdDSAPublicKey ownerPublicKey1 = ED25519.getPublicKey(edDSAPrivateKeyOwner1);
        user = new User(settings.getString("userName", ""), settings.getString("iban", ""), ownerPublicKey1);

        try {
            mAPI = new API(user, this);
            addItemsOnSpinner();
        } catch (HashException e) {
            e.printStackTrace();
        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        }
        setText();
        onSend();
    }

    /**
     * Method which sets the text in fields.
     */
    public void setText() {
        TextView ownerItemText = (TextView) findViewById(R.id.OwnerName);
        ownerItemText.setText(user.getName());
        TextView ibanItemText = (TextView) findViewById(R.id.IbanTransferor);
        ibanItemText.setText(user.getIban());
    }

    /**
     * Method which handles the transaction when the user clicks send.
     */
    public void onSend() {
        Button buttonSend = (Button) findViewById(R.id.SendButton);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionName = String.valueOf(dialog.getSelectedItem());
                EditText amountText = (EditText) findViewById(R.id.editText11);
                final int amount = Integer.parseInt(amountText.getText().toString());
                for (Block block : mAPI.getBlocks(user)) {
                    if (block.getBlockOwner().getName().equals(transactionName)) {
                        mAPI.successfulTransaction(block);
                        Toast.makeText(TransactionActivity.this, "Send €" + amount + " to "
                                + transactionName + "!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Method which fills in the dropDown with contacts.
     */
    public void addItemsOnSpinner() throws HashException {
        dialog = (Spinner) findViewById(R.id.spinner1);
        int listSize = mAPI.getBlocks(user).size();
        String[] items = new String[listSize];
        for (int i = 0; i < listSize; i++) {
            items[i] = mAPI.getBlocks(user).get(i).getBlockOwner().getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        dialog.setAdapter(adapter);
    }
}
