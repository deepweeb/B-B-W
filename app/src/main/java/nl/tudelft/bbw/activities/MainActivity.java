package nl.tudelft.bbw.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.List;

import nl.tudelft.bbw.BlockChainAPI;
import nl.tudelft.bbw.R;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        try {
            BlockChainAPI.initializeAPI("Naqib", "Nl22RABO222231123", this);
        } catch (HashException e) {
            e.printStackTrace();
        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        }

        addContactForTesting();


        updateView();

        /*ListView tree = (ListView) findViewById(R.id.listTree);

        final ArrayNodes root = new ArrayNodes(
                new String[] { "Contacts", "Pairing" },
                new INodes[] { null, null }
        );

        tree.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return root.getLength();
            }

            @Override
            public Object getItem(int position) {
                return "Test";
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                TextView view = (TextView) inflater.inflate(R.layout.node, null);
                view.setText(root.getName(position));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                return view;
            }
        });*/
    }

    public void updateView(){


        TreeNode root = TreeNode.root();

        TreeNode contacts = new TreeNode("> My Contacts");
        TreeNode pairing = new TreeNode("> Bluetooth Pairing");


        List<Block> myContacts = BlockChainAPI.getMyContacts();

        for(Block block : myContacts){
            TreeNode contactName = new TreeNode("\t> " + block.getContactName());

            TreeNode iban = new TreeNode("\t\t\t\t IBAN: " + block.getContactIban());
            TreeNode trust = new TreeNode("\t\t\t\t Trust Value: " + block.getTrustValue());
            TreeNode publicKey = new TreeNode("\t\t\t\t Public Key: " +block.getContactPublicKey());
            contactName.addChildren(iban, trust, publicKey);

            TreeNode hisContacts = new TreeNode("\t\t\t\t> Contacts");
            contactName.addChild(hisContacts);

            TreeNode transaction = new TreeNode("\t\t\t\t> Send money");
            TreeNode succesfulTransaction = new TreeNode("\t\t\t\t\t\t\tSuccesful transaction");
            TreeNode failedTransaction = new TreeNode("\t\t\t\t\t\t\tFailed transaction");
            transaction.addChildren(succesfulTransaction, failedTransaction);
            contactName.addChild(transaction);



            contacts.addChild(contactName);


        }
        TreeNode child0 = new TreeNode("ChildNode0");
        TreeNode child1 = new TreeNode("ChildNode1");
       // parent.addChildren(child0, child1);
        root.addChild(contacts);
        root.addChild(pairing);

        AndroidTreeView tView = new AndroidTreeView(this, root);

        ((ConstraintLayout) findViewById(R.id.container)).addView(tView.getView());
    }


    public void addContactForTesting()
    {

    }




    }
