package nl.tudelft.bbw.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.bbw.BlockChainAPI;
import nl.tudelft.bbw.R;
import nl.tudelft.bbw.blockchain.Acquaintance;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

import static nl.tudelft.bbw.activities.MainActivityTestSubjects.generateAcquaintanceFromCrawler;

public class MainActivity extends Activity {
    final Context context = this;
    List<Acquaintance> acquaintancesList = new ArrayList<Acquaintance>() ;
    User APIUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        try {
            APIUser = BlockChainAPI.initializeAPI("Naqib", "Nl22RABO222231123", context);
        } catch (HashException e) {
            e.printStackTrace();
        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        }


        //TODO: remove after testing
        try {


        /*    MainActivityTestSubjects.addContactForTesting();
            MainActivityTestSubjects.addContactForTesting2();*/

         /*   Acquaintance testAcquaintance = MainActivityTestSubjects.generateAcquaintanceForTest();
            Acquaintance testAcquaintance2 = MainActivityTestSubjects.generateAcquaintanceForTest2();

            BlockChainAPI.addAcquaintanceMultichain(testAcquaintance);
            BlockChainAPI.addAcquaintanceMultichain(testAcquaintance2);
            acquaintancesList.add(testAcquaintance);
            acquaintancesList.add(testAcquaintance2);*/


            acquaintancesList =  generateAcquaintanceFromCrawler(this);
            for(Acquaintance a: acquaintancesList)
            {
                BlockChainAPI.addAcquaintanceMultichain(a);
            }
        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        } catch (HashException e) {
            e.printStackTrace();
    /*    } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();*/
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }


        updateView();

    }
    public void updateView() {


        TreeNode root = TreeNode.root();
        TreeNode myName = new TreeNode("       My Name: " + APIUser.getName());
        TreeNode myIban = new TreeNode("       My IBAN: " + APIUser.getIban());
        TreeNode myPublicKey = new TreeNode("       My Public Key: " +  APIUser.getPublicKey().getEncoded());
        TreeNode contacts = new TreeNode("> My Contacts");
        for (final Block block : BlockChainAPI.getContactsOf(APIUser)) {
            if (!block.getContactIban().equals(APIUser.getIban())) {

                TreeNode contactName = new TreeNode("\t> " + block.getContactName());
                TreeNode iban = new TreeNode("\t\t\t\tIBAN: " + block.getContactIban());
                TreeNode trust = new TreeNode("\t\t\t\tTrust Value: " + block.getTrustValue());
                TreeNode publicKey = new TreeNode("\t\t\t\tPublic Key: " + block.getContactPublicKey().getEncoded());
                TreeNode transaction = new TreeNode("\t\t\t\t> Send money");

                TreeNode ibanVerification = new TreeNode("\t\t\t\t\t\t\tVerified IBAN transaction");
                ibanVerification.setClickListener(new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        Toast.makeText(context, "IBAN verified, Trust Value upgraded!", Toast.LENGTH_SHORT).show();
                        BlockChainAPI.verifyIBANTrustUpdate(block);
                        refreshView();
                    }
                });

                TreeNode succesfulTransaction = new TreeNode("\t\t\t\t\t\t\tSuccesful transaction");
                succesfulTransaction.setClickListener(new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        Toast.makeText(context, "Transaction Succeeded, Trust Value upgraded!", Toast.LENGTH_SHORT).show();
                        BlockChainAPI.successfulTransactionTrustUpdate(block);
                        refreshView();
                    }
                });

                TreeNode failedTransaction = new TreeNode("\t\t\t\t\t\t\tFailed transaction");
                failedTransaction.setClickListener(new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        Toast.makeText(context, "Transaction Failed, Trust Value downgraded!", Toast.LENGTH_SHORT).show();
                        BlockChainAPI.failedTransactionTrustUpdate(block);
                        refreshView();
                    }
                });
                transaction.addChildren(ibanVerification, succesfulTransaction, failedTransaction);
                TreeNode revoke = new TreeNode("\t\t\t\tRevoke this contact");
                revoke.setClickListener(new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        Toast.makeText(context, "Contact revoked from your chain!", Toast.LENGTH_SHORT).show();
                        try {
                            BlockChainAPI.revokeContact(block.getContact());
                        } catch (HashException e) {
                            e.printStackTrace();
                        } catch (BlockAlreadyExistsException e) {
                            e.printStackTrace();
                        }
                        refreshView();
                    }
                });
                TreeNode hisContacts = displayContact(block.getContact(), BlockChainAPI.getContactsOf(block.getContact()), 1);
                contactName.addChildren(iban, trust, publicKey, transaction, revoke, hisContacts);
                //             failedTransaction.isExpanded();
                //         failedTransaction.setExpanded(true);
                contacts.addChild(contactName);

            }
        }

        TreeNode pairing = pairingSimutation(acquaintancesList);
        root.addChildren(myName, myIban, myPublicKey, contacts, pairing);

        AndroidTreeView tView = new AndroidTreeView(this, root);

        ((ConstraintLayout) findViewById(R.id.container)).addView(tView.getView());
    }

    public TreeNode pairingSimutation(List<Acquaintance> acquaintanceList) {
        TreeNode pairing = new TreeNode("> Bluetooth Pairing (Simulation)");
        for (final Acquaintance acquaintance : acquaintancesList)
        {
            TreeNode acquaintanceName = new TreeNode("\t> " + acquaintance.getName());
            TreeNode iban = new TreeNode("\t\t\t\tIBAN: " +  acquaintance.getIban());
            TreeNode trust = new TreeNode("\t\t\t\tTrust Value: " + BlockChainAPI.getContactsOf(acquaintance).get(0).getTrustValue());
            TreeNode publicKey = new TreeNode("\t\t\t\tPublic Key: " + acquaintance.getPublicKey().getEncoded());
            TreeNode addToContactList = new TreeNode("\t\t\t\tAdd to your contact list");
            addToContactList.setClickListener(new TreeNode.TreeNodeClickListener() {
                @Override
                public void onClick(TreeNode node, Object value) {
                    try {
                        BlockChainAPI.addContact(acquaintance);
                        Toast.makeText(context, acquaintance.getName()+ " is now added into your list!", Toast.LENGTH_SHORT).show();
                    } catch (HashException e) {
                        e.printStackTrace();
                    } catch (BlockAlreadyExistsException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                        e.printStackTrace();
                    } catch (SignatureException e) {
                        e.printStackTrace();
                    }
                    acquaintancesList.remove(acquaintance);
                    refreshView();
                }
            });

            TreeNode hisContacts = displayContact(acquaintance, BlockChainAPI.getContactsOf(acquaintance), 1);
            acquaintanceName.addChildren(iban, publicKey, trust, addToContactList, hisContacts);
            //             failedTransaction.isExpanded();
            //         failedTransaction.setExpanded(true);
            pairing.addChild(acquaintanceName);
        }

        return pairing;
    }

    public TreeNode displayContact(User contact, List<Block> hisBlockList, int layerCount) {
        if (hisBlockList.size() == 1) {
            return new TreeNode(addSpace(layerCount) + "> Contacts (No Contact Available)");
        }
        TreeNode hisContacts = new TreeNode(addSpace(layerCount) + "> "+ contact.getName()+ "'s contacts");

        for (final Block hisBlock : hisBlockList) {
            if (!hisBlock.getContactIban().equals(contact.getIban())) {
                TreeNode contactName = new TreeNode(addSpace(layerCount + 1) + "> " + hisBlock.getContactName());
                TreeNode iban = new TreeNode(addSpace(layerCount + 1) + "IBAN: " + hisBlock.getContactIban());
                TreeNode trust = new TreeNode(addSpace(layerCount + 1) + "Trust Value: " + hisBlock.getTrustValue());
                TreeNode publicKey = new TreeNode(addSpace(layerCount + 1) + "Public Key: " + hisBlock.getContactPublicKey().getEncoded());
                TreeNode addToContactList = new TreeNode((addSpace(layerCount + 1) +"Add to your contact list"));
                addToContactList.setClickListener(new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        try {
                            BlockChainAPI.addContact(hisBlock.getContact());
                            Toast.makeText(context, hisBlock.getContact().getName()+ " is now added into your list!", Toast.LENGTH_SHORT).show();
                        } catch (HashException e) {
                            e.printStackTrace();
                        } catch (BlockAlreadyExistsException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Block already existed in database!", Toast.LENGTH_SHORT).show();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (SignatureException e) {
                            e.printStackTrace();
                        }

                        refreshView();
                    }
                });
                TreeNode contactl = displayContact(hisBlock.getContact(), BlockChainAPI.getContactsOf(hisBlock.getContact()), layerCount + 1);

                contactName.addChildren(iban, trust, publicKey, addToContactList, contactl);
                hisContacts.addChild(contactName);
            }
        }

        return hisContacts;
    }

    public String addSpace(int layerCount) {
        String space = "\t\t\t\t";
        for (int j = 1; j < layerCount; j++) {
            space = space + space;
        }
        return space;
    }

    public void refreshView(){
        ViewGroup vg = (ViewGroup) findViewById (R.id.container);
        vg.removeAllViews();
        vg.refreshDrawableState();
        updateView();
    }
}
