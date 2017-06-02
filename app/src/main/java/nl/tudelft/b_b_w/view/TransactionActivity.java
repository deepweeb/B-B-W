package nl.tudelft.b_b_w.view;


import static nl.tudelft.b_b_w.view.MainActivity.PREFS_NAME;

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

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.controller.BlockController;
import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.User;

/**
 * Page which handles transactions between user and a contact.
 */
public class TransactionActivity extends Activity {

    //Variables which are frequently used
    private BlockController blockController;
    private User user;
    private Spinner dialog;
    private String transactionName;

    /**
     * Default method which executes all tasks on creation
     * @param savedInstanceState The current state, which is kept in memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        user = new User(settings.getString("userName", ""), settings.getString("iban", ""));
        blockController = new BlockController(this);
        addItemsOnSpinner();
        setText();
        onSend();
    }

    /**
     * Method which sets the text in fields.
     */
    public void setText() {
        TextView ownerItemText = (TextView)  findViewById(R.id.OwnerName);
        ownerItemText.setText(user.getName());
        TextView ibanItemText = (TextView)  findViewById(R.id.IbanTransferor);
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
                for (Block block : blockController.getBlocks(user.getName())) {
                    if (blockController.backtrack(block).getOwner().equals(transactionName)) {
                        blockController.successfulTransaction(block);
                        Toast.makeText(TransactionActivity.this, "Send €" + amount+" to "
                                + transactionName + "!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Method which fills in the dropDown with contacts.
     */
    public void addItemsOnSpinner() {
        dialog = (Spinner)findViewById(R.id.spinner1);
        int listSize = blockController.getBlocks(user.getName()).size();
        String[] items = new String[listSize];
        for (int i = 0; i < listSize; i++) {
            items[i] = blockController.backtrack(
                    blockController.getBlocks(user.getName()).get(i)).getOwner();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        dialog.setAdapter(adapter);
    }
}
