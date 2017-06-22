package nl.tudelft.bbw.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;

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


//        //TODO: remove after testing
//        try {
//            addContactForTesting();
//        } catch (BlockAlreadyExistsException e) {
//            e.printStackTrace();
//        } catch (HashException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (SignatureException e) {
//            e.printStackTrace();
//        }


        updateView();

    }

    public void updateView() {


        TreeNode root = TreeNode.root();

        TreeNode contacts = new TreeNode("> My Contacts");
        TreeNode pairing = new TreeNode("> Bluetooth Pairing");


        List<Block> myContacts = BlockChainAPI.getMyContacts();

        for (Block block : myContacts) {
            TreeNode contactName = new TreeNode("\t> " + block.getContactName());
            TreeNode iban = new TreeNode("\t\t\t\t IBAN: " + block.getContactIban());
            TreeNode trust = new TreeNode("\t\t\t\t Trust Value: " + block.getTrustValue());
            TreeNode publicKey = new TreeNode("\t\t\t\t Public Key: " + block.getContactPublicKey());
            TreeNode hisContacts = new TreeNode("\t\t\t\t> Contacts");
            TreeNode transaction = new TreeNode("\t\t\t\t> Send money");
            TreeNode succesfulTransaction = new TreeNode("\t\t\t\t\t\t\tSuccesful transaction");
            TreeNode failedTransaction = new TreeNode("\t\t\t\t\t\t\tFailed transaction");

            transaction.addChildren(succesfulTransaction, failedTransaction);
            contactName.addChildren(iban, trust, publicKey, hisContacts, transaction);

            contacts.addChild(contactName);

        }

        TreeNode acquaintance1 = new TreeNode("\t>Acquaintance1");
        TreeNode ibanAq1 = new TreeNode("\t\t\t\t IBAN: NL Aq1 111111111111");
        TreeNode trustAq1 = new TreeNode("\t\t\t\t Trust Value: 25");
        TreeNode pubKeyAq1 = new TreeNode("\t\t\t\t Public Key: dj83uf9hf389h");
        TreeNode contactsAq1 = new TreeNode("\t\t\t\t < Contacts");
        acquaintance1.addChildren(ibanAq1, trustAq1, pubKeyAq1);


        root.addChild(contacts);
        root.addChild(pairing);

        AndroidTreeView tView = new AndroidTreeView(this, root);

        ((ConstraintLayout) findViewById(R.id.container)).addView(tView.getView());
    }


    /////////////////////////////////////////////////////////////////////////////////////////////


    public void addContactForTesting() throws BlockAlreadyExistsException, HashException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        EdDSAPrivateKey privateKey = ED25519.generatePrivateKey();
        Acquaintance userB = new Acquaintance("Luat", "NL623423423423", ED25519.getPublicKey(privateKey), new ArrayList<List<Block>>());
        Block userBGenesis = new Block(userB);

        //Initializing C.
        User userC = new User("Ymte", "NLe1242343424", ED25519.getPublicKey(privateKey));
        Block userCGenesis = new Block(userC);


        //Add C to the chain of B
        Block keyblock = createKeyBlock(userBGenesis, userCGenesis, BlockType.ADD_KEY);
        List<List<Block>> multichain = new ArrayList<List<Block>>();
        List<Block> test = new ArrayList<Block>();
        test.add(userBGenesis);
        test.add(keyblock);
        multichain.add(test);

        //Add genesis of C into multichain of B
        List<Block> test2 = new ArrayList<Block>();
        test2.add(userCGenesis);
        multichain.add(test2);
        userB.setMultichain(multichain);

        userB.setPrivateKey(privateKey);

        //Add multichain of B into your database right after pairing
        BlockChainAPI.addAcquaintanceMultichain(userB);

        //Add contact B to A (yourself)
        Block blockB = BlockChainAPI.addContact(userB);
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
