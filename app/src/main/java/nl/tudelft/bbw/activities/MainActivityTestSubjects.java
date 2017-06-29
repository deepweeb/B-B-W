package nl.tudelft.bbw.activities;

import android.app.Activity;
import android.content.Context;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.tudelft.bbw.BlockChainAPI;
import nl.tudelft.bbw.blockchain.Acquaintance;
import nl.tudelft.bbw.blockchain.Block;
import nl.tudelft.bbw.blockchain.BlockData;
import nl.tudelft.bbw.blockchain.BlockType;
import nl.tudelft.bbw.blockchain.Hash;
import nl.tudelft.bbw.blockchain.TrustValues;
import nl.tudelft.bbw.blockchain.User;
import nl.tudelft.bbw.controller.ED25519;
import nl.tudelft.bbw.crawler.CrawledBlocksDatabase;
import nl.tudelft.bbw.crawler.ReadCrawlerBlocksQuery;
import nl.tudelft.bbw.exception.BlockAlreadyExistsException;
import nl.tudelft.bbw.exception.HashException;

public class MainActivityTestSubjects extends Activity {

    public static void addContactForTesting() throws BlockAlreadyExistsException, HashException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
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
        keyblock.setTrustValue(25);
        multichain.add(test2);
        userB.setMultichain(multichain);

        userB.setPrivateKey(privateKey);

        //Add multichain of userB into your database right after pairing
        BlockChainAPI.addAcquaintanceMultichain(userB);

        //Add contact userB to userA (yourself)
        Block blockB = BlockChainAPI.addContact(userB);
    }

    public static void addContactForTesting2() throws BlockAlreadyExistsException, HashException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
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
        keyblock.setTrustValue(47);
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


    public static Acquaintance generateAcquaintanceForTest() throws BlockAlreadyExistsException, HashException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
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
        userBGenesis.setTrustValue(21);
        test.add(userBGenesis);
        keyblock.setTrustValue(15);
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

    public static Acquaintance generateAcquaintanceForTest2() throws BlockAlreadyExistsException, HashException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
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
        userBGenesis.setTrustValue(81);
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

    public static List<Acquaintance> generateAcquaintanceFromCrawler(Context context) throws IOException, InvalidKeySpecException {
        List<Acquaintance> acquaintancesList = new ArrayList<Acquaintance>();
        CrawledBlocksDatabase database = new CrawledBlocksDatabase(context);
        ReadCrawlerBlocksQuery query = new ReadCrawlerBlocksQuery();
        database.read(query);


        for (Map.Entry<EdDSAPublicKey, List<Block>> entry : query.getMultiChain().entrySet()) {
            List<List<Block>> multichain = new ArrayList<List<Block>>();

            EdDSAPublicKey chainOwnerPubKey = entry.getKey();
            List<Block> chain = entry.getValue();
            multichain.add(chain);

          /*  for(int i = 1; i<chain.size(); i++)
            {
                Block genesisBlock = new Block(chain.get(i).getContact());
                List<Block> test = new ArrayList<Block>();
                test.add(genesisBlock);
                multichain.add(test);
            }*/

            Acquaintance acquaintance = new Acquaintance(chain.get(0).getOwnerName(), "N/A", chainOwnerPubKey, multichain);
            acquaintancesList.add(acquaintance);
        }

        return acquaintancesList;
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
    public static Block createKeyBlock(Block latest, Block contact,
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
