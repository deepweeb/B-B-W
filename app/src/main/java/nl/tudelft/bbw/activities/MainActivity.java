package nl.tudelft.bbw.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.bbw.BlockChainAPI;
import nl.tudelft.bbw.R;
import nl.tudelft.bbw.blockchain.Acquaintance;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.BlockData;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.blockchain.Hash;
import nl.tudelft.bbw.blockchain.TrustValues;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.ED25519;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

public class MainActivity extends Activity {
    final Context context = this;
    List<Acquaintance> acquaintancesList = new ArrayList<Acquaintance>() ;

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


        //TODO: remove after testing
        try {


            addContactForTesting();
            addContactForTesting2();


            Acquaintance testAcquaintance = generateAcquaintanceForTest();
            Acquaintance testAcquaintance2 = generateAcquaintanceForTest2();

            BlockChainAPI.addAcquaintanceMultichain(testAcquaintance);
            BlockChainAPI.addAcquaintanceMultichain(testAcquaintance2);
            acquaintancesList.add(testAcquaintance);
            acquaintancesList.add(testAcquaintance2);

        } catch (BlockAlreadyExistsException e) {
            e.printStackTrace();
        } catch (HashException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }











        updateView();

    }

    public void updateView() {


        TreeNode root = TreeNode.root();
        TreeNode myName = new TreeNode("       My Name: " + BlockChainAPI.getMyName());
        TreeNode myIban = new TreeNode("       My IBAN: " + BlockChainAPI.getMyIban());
        TreeNode myPublicKey = new TreeNode("       My Public Key: " +  BlockChainAPI.getMyPublicKey().getEncoded());
        TreeNode contacts = new TreeNode("> My Contacts");
        for (final Block block : BlockChainAPI.getMyContacts()) {
            if (!block.getContactIban().equals(BlockChainAPI.getMyIban())) {

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

        TreeNode pairing = new TreeNode("> Bluetooth Pairing (Simulation)");
        for (final Acquaintance acquaintance : acquaintancesList)
        {
            TreeNode acquaintanceName = new TreeNode("\t> " + acquaintance.getName());
            TreeNode iban = new TreeNode("\t\t\t\tIBAN: " +  acquaintance.getIban());
         //   TreeNode trust = new TreeNode("\t\t\t\tTrust Value: " + acquaintance.get);
            TreeNode publicKey = new TreeNode("\t\t\t\tPublic Key: " + acquaintance.getPublicKey().getEncoded());
            TreeNode addToContactList = new TreeNode("\t\t\t\tAdd to your contact list");
            addToContactList.setClickListener(new TreeNode.TreeNodeClickListener() {
                @Override
                public void onClick(TreeNode node, Object value) {
                    Toast.makeText(context, "Contact is now added into your list!", Toast.LENGTH_SHORT).show();
                    try {
                        BlockChainAPI.addContact(acquaintance);
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
            acquaintanceName.addChildren(iban, publicKey, addToContactList, hisContacts);
            //             failedTransaction.isExpanded();
            //         failedTransaction.setExpanded(true);
            pairing.addChild(acquaintanceName);
        }
        root.addChildren(myName, myIban, myPublicKey, contacts, pairing);

        AndroidTreeView tView = new AndroidTreeView(this, root);

        ((ConstraintLayout) findViewById(R.id.container)).addView(tView.getView());
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
                        Toast.makeText(context, "Contact is now added into your list!", Toast.LENGTH_SHORT).show();
                        try {
                            BlockChainAPI.addContact(hisBlock.getContact());
                        } catch (HashException e) {
                            e.printStackTrace();
                        } catch (BlockAlreadyExistsException e) {
                            e.printStackTrace();
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
            return space;
        }
        return space;
    }

    public void refreshView(){
        ViewGroup vg = (ViewGroup) findViewById (R.id.container);
        vg.removeAllViews();
        vg.refreshDrawableState();
        updateView();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////


    public void addContactForTesting() throws BlockAlreadyExistsException, HashException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        Acquaintance userB = new Acquaintance("Luat", "NL623423423423", ED25519.getPublicKey(privateKey), new ArrayList<List<Block>>());
        Block userBGenesis = new Block(userB);

        //Initializing userC.
        EdDSAPrivateKey privateKey2 = ED25519.generatePrivateKey();
        User userC = new User("Ymte", "NLe1242343424", ED25519.getPublicKey(privateKey2));
        Block userCGenesis = new Block(userC);
        userC.setPrivateKey(privateKey2);

        //Add userC to the chain of userB
        Block keyblock = createKeyBlock(userBGenesis, userCGenesis, BlockType.ADD_KEY);
        List<List<Block>> multichain = new ArrayList<List<Block>>();
        List<Block> test = new ArrayList<Block>();
        test.add(userBGenesis);
        test.add(keyblock);
        multichain.add(test);

        //Add genesis of userC into multichain of userB
        List<Block> test2 = new ArrayList<Block>();
        test2.add(userCGenesis);
        multichain.add(test2);
        userB.setMultichain(multichain);

        userB.setPrivateKey(privateKey);

        //Add multichain of userB into your database right after pairing
        BlockChainAPI.addAcquaintanceMultichain(userB);

        //Add contact userB to userA (yourself)
        Block blockB = BlockChainAPI.addContact(userB);
    }

    public void addContactForTesting2() throws BlockAlreadyExistsException, HashException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        Acquaintance userB = new Acquaintance("Jasper", "NL64544463423423423", ED25519.getPublicKey(privateKey), new ArrayList<List<Block>>());
        Block userBGenesis = new Block(userB);

        //Initializing userC.
        EdDSAPrivateKey privateKey2 = ED25519.generatePrivateKey();
        User userC = new User("Ashay", "NL55332343424", ED25519.getPublicKey(privateKey2));
        Block userCGenesis = new Block(userC);
        userC.setPrivateKey(privateKey2);

        //Add userC to the chain of userB
        Block keyblock = createKeyBlock(userBGenesis, userCGenesis, BlockType.ADD_KEY);
        List<List<Block>> multichain = new ArrayList<List<Block>>();
        List<Block> test = new ArrayList<Block>();
        test.add(userBGenesis);
        test.add(keyblock);
        multichain.add(test);

        //Add genesis of userC into multichain of userB
        List<Block> test2 = new ArrayList<Block>();
        test2.add(userCGenesis);
        multichain.add(test2);
        userB.setMultichain(multichain);

        userB.setPrivateKey(privateKey);

        //Add multichain of userB into your database right after pairing
        BlockChainAPI.addAcquaintanceMultichain(userB);

        //Add contact userB to userA (yourself)
        Block blockB = BlockChainAPI.addContact(userB);
    }


    public Acquaintance generateAcquaintanceForTest() throws BlockAlreadyExistsException, HashException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        Acquaintance userB = new Acquaintance("Yusuf", "NLABN23423523523424", ED25519.getPublicKey(privateKey), new ArrayList<List<Block>>());
        Block userBGenesis = new Block(userB);

        //Initializing userC.
        EdDSAPrivateKey privateKey2 = ED25519.generatePrivateKey();
        User userC = new User("Mohammed", "NLEFR232423423423234", ED25519.getPublicKey(privateKey2));
        Block userCGenesis = new Block(userC);
        userC.setPrivateKey(privateKey2);

        //Add userC to the chain of userB
        Block keyblock = createKeyBlock(userBGenesis, userCGenesis, BlockType.ADD_KEY);
        List<List<Block>> multichain = new ArrayList<List<Block>>();
        List<Block> test = new ArrayList<Block>();
        keyblock.setTrustValue(15);
        test.add(userBGenesis);
        test.add(keyblock);
        multichain.add(test);

        //Add genesis of userC into multichain of userB
        List<Block> test2 = new ArrayList<Block>();
        test2.add(userCGenesis);
        multichain.add(test2);
        userB.setMultichain(multichain);

        userB.setPrivateKey(privateKey);
        return userB;
    }

    public Acquaintance generateAcquaintanceForTest2() throws BlockAlreadyExistsException, HashException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        Acquaintance userB = new Acquaintance("Karina", "NLEW23423423423", ED25519.getPublicKey(privateKey), new ArrayList<List<Block>>());
        Block userBGenesis = new Block(userB);

        //Initializing userC.
        EdDSAPrivateKey privateKey2 = ED25519.generatePrivateKey();
        User userC = new User("Fatima", "NLER2342342342", ED25519.getPublicKey(privateKey2));
        Block userCGenesis = new Block(userC);
        userC.setPrivateKey(privateKey2);

        //Add userC to the chain of userB
        Block keyblock = createKeyBlock(userBGenesis, userCGenesis, BlockType.ADD_KEY);
        List<List<Block>> multichain = new ArrayList<List<Block>>();
        List<Block> test = new ArrayList<Block>();
        test.add(userBGenesis);
        keyblock.setTrustValue(35);
        test.add(keyblock);
        multichain.add(test);

        //Add genesis of userC into multichain of userB
        List<Block> test2 = new ArrayList<Block>();
        test2.add(userCGenesis);
        multichain.add(test2);
        userB.setMultichain(multichain);

        userB.setPrivateKey(privateKey);
        return userB;
    }

    /**
     * Method to return a key block to add to a chain
     * This method is made  to create test blocks for the tests
     *
     * @param contact
     * @param blockType
     * @return
     * @throws HashException
     * @throws BlockAlreadyExistsException
     */
    public Block createKeyBlock(Block latest, Block contact,
                                BlockType blockType) throws HashException, BlockAlreadyExistsException {
        Hash previousBlockHash = latest.getOwnHash();
        // always link to genesis of contact blocks
        Hash contactBlockHash;
        contactBlockHash = contact.getOwnHash();
        int seqNumber = latest.getSequenceNumber() + 1;

        BlockData blockData = new BlockData(blockType, seqNumber, previousBlockHash,
                contactBlockHash, TrustValues.INITIALIZED.getValue()
        );
        final Block block = new Block(latest.getBlockOwner(), contact.getBlockOwner(), blockData);
        return block;
    }


}
