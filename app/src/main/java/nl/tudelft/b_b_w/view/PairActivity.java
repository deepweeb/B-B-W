package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;

import java.util.List;

import nl.tudelft.b_b_w.API;
import nl.tudelft.b_b_w.R;
import nl.tudelft.b_b_w.blockchain.Block;
import nl.tudelft.b_b_w.blockchain.User;
import nl.tudelft.b_b_w.controller.ED25519;
import nl.tudelft.b_b_w.model.BlockAlreadyExistsException;
import nl.tudelft.b_b_w.model.HashException;

import static nl.tudelft.b_b_w.view.MainActivity.PREFS_NAME;


/**
 * Pair activity lets you pair with a fixed number of preprogrammed contacts, for demo purposes.
 */
public class PairActivity extends Activity {
    /**
     * Some constants for list indexing.
     */
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;


    /**
     * The block controller.
     */
    private API mAPI;


    /**
     * The name of the owner of each block in the chain.
     */
    private User owner;


    /**
     * The on create method sets up the activity.
     *
     * @param savedInstanceState brings the variables.
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);
        try {
            mAPI = new API(owner, this);
        } catch (HashException e) {
            e.printStackTrace();
        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method create the first test subject.We do this in order to simulate a transaction.
     *
     * @param view The view of the program.
     */
    public final void onTestSubject1(View view) throws Exception {
        EdDSAPrivateKey edDSAPrivateKeyOwner =
                ED25519.generatePrivateKey(Utils.hexToBytes(
                        "0000000000000000000000000000000000000000000000000000000000000000"));
        EdDSAPublicKey ownerPublicKey = ED25519.getPublicKey(edDSAPrivateKeyOwner);
        owner = new User("TestSubject1", "IBAN1", ownerPublicKey);


        EdDSAPrivateKey edDSAPrivateKeyOwner1 =
                ED25519.generatePrivateKey(Utils.hexToBytes(
                        "1000000000000000000000000000000000000000000000000000000000000001"));
        EdDSAPublicKey ownerPublicKey1 = ED25519.getPublicKey(edDSAPrivateKeyOwner1);
        User subject1contact1 = new User("Subject1Contact1", "IBAN1Contact1", ownerPublicKey1);

        EdDSAPrivateKey edDSAPrivateKeyOwner2 =
                ED25519.generatePrivateKey(Utils.hexToBytes(
                        "1100000000000000000000000000000000000000000000000000000000000011"));
        EdDSAPublicKey ownerPublicKey2 = ED25519.getPublicKey(edDSAPrivateKeyOwner2);
        User subject1contact2 = new User("Subject1Contact2", "IBAN1Contact2", ownerPublicKey2);

        EdDSAPrivateKey edDSAPrivateKeyOwner3 =
                ED25519.generatePrivateKey(Utils.hexToBytes(
                        "1110000000000000000000000000000000000000000000000000000000000111"));
        EdDSAPublicKey ownerPublicKey3 = ED25519.getPublicKey(edDSAPrivateKeyOwner3);
        User subject1contact3 = new User("Subject1Contact3", "IBAN1Contact3", ownerPublicKey3);

        mAPI.addContactToChain(subject1contact1);
        mAPI.addContactToChain(subject1contact2);
        mAPI.addContactToChain(subject1contact3);


        List<Block> list = mAPI.getBlocks(owner);

        Toast.makeText(this, list.get(0).getContactPublicKey() + ", "
                + list.get(ONE).getContactPublicKey() + ", "
                + list.get(TWO).getContactPublicKey() + ", "
                + list.get(THREE).getContactPublicKey(), Toast.LENGTH_SHORT).show();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userNameTestSubject", owner.getName());
        editor.putString("ibanTestSubject", owner.getIban());
        editor.apply();

        startActivity(new Intent(this, FriendsPageActivity.class));
    }

}