package nl.tudelft.bbw.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.List;

import nl.tudelft.bbw.BlockChainAPI;
import nl.tudelft.bbw.R;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

public class MainActivity3 extends Activity {

    List<Block> contactList;
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        try {
            BlockChainAPI.initializeAPI("Naqib", "NL22", this);
        } catch (HashException e) {
            e.printStackTrace();
        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        }

        contactList = BlockChainAPI.getMyContacts();

    }

}
