package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.model.User;

import static nl.tudelft.b_b_w.view.MainActivity.PREFS_NAME;

public class TransactionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        User user = new User(settings.getString("userName", ""), settings.getString("iban", ""));

        TextView ownerItemText = (TextView)  findViewById(R.id.OwnerName);
        ownerItemText.setText(user.getName());

        TextView IbanItemText = (TextView)  findViewById(R.id.IbanTransferor);
        IbanItemText.setText(user.getIBAN());
    }


}
