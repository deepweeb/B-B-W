package nl.tudelft.b_b_w.controller;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.b_b_w.model.Block;
import nl.tudelft.b_b_w.model.BlockFactory;
import nl.tudelft.b_b_w.model.GetDatabaseHandler;
import nl.tudelft.b_b_w.model.MutateDatabaseHandler;
import nl.tudelft.b_b_w.model.TrustValues;

/**
 * Performs the actions on the blockchain
 */

public class BlockController implements BlockControllerInterface {
    /**
     * For when info is not available.
     */
    private static final String NA = "N/A";

    /**
     * Block argument to create a block
     */
    private static final String BLOCK = "BLOCK";

    /**
     * Block argument to create a revoke block
     */
    private static final String REVOKE = "REVOKE";

    /**
     * Context of the block database
     */
    private Context context;

    /**
     *  Databasehandlers to use
     */
    private GetDatabaseHandler getDatabaseHandler;
    private MutateDatabaseHandler mutateDatabaseHandler;

    /**
     * Constructor to initialize all the involved entities
     *
     * @param _context the instance
     */
    public BlockController(Context _context) {
        this.context = _context;
        this.getDatabaseHandler = new GetDatabaseHandler(_context);
        this.mutateDatabaseHandler = new MutateDatabaseHandler(_context);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final boolean blockExists(String owner, String key, boolean revoked) {
        return getDatabaseHandler.blockExists(owner, key, revoked);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final List<Block> addBlockToChain(Block block) {
        // Check if the block already exists
        String owner = block.getOwner();
        Block latest = getDatabaseHandler.getLatestBlock(owner);

        if (latest == null) {
            mutateDatabaseHandler.addBlock(block);
        } else if (latest.isRevoked()) {
            throw new RuntimeException("Error - Block is already revoked");
        } else {
            if (block.isRevoked()) {
                revokedTrustValue(latest);
                mutateDatabaseHandler.updateBlock(latest);
                mutateDatabaseHandler.addBlock(block);
            }
            else {
                throw new RuntimeException("Error - Block already exists");
            }
        }

        return getBlocks(owner);
    }


    /**
     * @inheritDoc
     */
    @Override
    public final void addBlock(Block block) {

        if (blockExists(block.getOwner(), block.getPublicKey(), block.isRevoked()))
            throw new RuntimeException("block already exists");

        mutateDatabaseHandler.addBlock(block);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final void clearAllBlocks() {
        mutateDatabaseHandler.clearAllBlocks();
    }

    /**
     * @inheritDoc
     */
    @Override
    public final List<Block> getBlocks(String owner) {
        // retrieve all blocks in the database and then sort it in order of sequence number
        List<Block> blocks = getDatabaseHandler.getAllBlocks(owner);
        List < Block > res = new ArrayList<>();

        for (Block block : blocks) {
            if (block.isRevoked()) {
                res = removeBlock(res, block);
            } else {
                res.add(block);
            }
        }
        return res;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final String getContactName(String hash) {
        return getDatabaseHandler.getContactName(hash);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block getLatestBlock(String owner) {
        return getDatabaseHandler.getLatestBlock(owner);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final int getLatestSeqNumber(String owner) {
        return getDatabaseHandler.lastSeqNumberOfChain(owner);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final List<Block> revokeBlock(Block block) {
        String owner = block.getOwner();
        addBlock(BlockFactory.getBlock(
                REVOKE,
                owner,
                getLatestSeqNumber(owner) + 1,
                block.getOwnHash(),
                block.getPreviousHashChain(),
                block.getPreviousHashSender(),
                block.getPublicKey(),
                block.getIban(),
                block.getTrustValue()));
        return getBlocks(owner);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final List<Block> removeBlock(List<Block> list, Block block) {
        List<Block> res = new ArrayList<>();
        for (Block blc : list) {
            if (!(blc.getOwner().equals(block.getOwner()) && blc.getPublicKey().equals(block.
                    getPublicKey()))) {
                res.add(blc);
            }
        }
        return res;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block verifyIBAN(Block block) {
        block.setTrustValue(TrustValues.VERIFIED.getValue());
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block successfulTransaction(Block block) {
        block.setTrustValue(block.getTrustValue() + TrustValues.SUCCESFUL_TRANSACTION.getValue());
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block failedTransaction(Block block) {
        block.setTrustValue(block.getTrustValue() + TrustValues.FAILED_TRANSACTION.getValue());
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Block revokedTrustValue(Block block) {
        block.setTrustValue(TrustValues.REVOKED.getValue());
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final boolean isDatabaseEmpty() {
        return getDatabaseHandler.isDatabaseEmpty();
    }

    /**
     * Create genesis block for an owner
     * @param owner the new owner of the block
     * @return the freshly created block
     * @throws Exception when the key hashing method does not work
     */
    public Block createGenesis(String owner) throws Exception {
        String chainHash = NA;
        String senderHash = NA;
        String publicKey = NA;
        String iban = NA;
        ConversionController conversionController = new ConversionController(owner, publicKey,
                chainHash, senderHash, iban);
        String hash = conversionController.hashKey();
        Block block = BlockFactory.getBlock(
                BLOCK,
                owner,
                getLatestSeqNumber(owner) + 1,
                hash,
                chainHash,
                senderHash,
                publicKey,
                iban,
                0
        );
        addBlock(block);
        return block;
    }

    /**
     * Create a block which adds a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     * @param owner owner of the block
     * @param contact of whom is the information
     * @param publicKey public key you want to store
     * @param iban IBAN number to store in this block
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    public Block createKeyBlock(String owner, String contact, String publicKey, String iban) throws
            Exception {
        return createBlock(owner, contact, publicKey, iban, false);
    }

    /**
     * Create a block which revokes a key for a certain user and weaves it into the blockchain.
     * The initial trust value is zero.
     * @param owner owner of the block
     * @param contact of whom is the information
     * @param publicKey public key you want to store
     * @param iban IBAN number to store in this block
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    public Block createRevokeBlock(String owner, String contact, String publicKey, String iban)
            throws Exception {
        return createBlock(owner, contact, publicKey, iban, true);
    }

    /**
     * Creates a block with given revoke status. The block is added to the blockchain automatically
     * with all fields set correctly.
     * @param owner owner of the block
     * @param contact of whom is the information
     * @param publicKey public key you want to store
     * @param iban IBAN number to store in this block
     * @param revoke whether to revoke?
     * @return the newly created block
     * @throws Exception when the hashing algorithm is not available
     */
    private Block createBlock(String owner, String contact, String publicKey, String iban, boolean
            revoke) throws Exception {
        Block latest = getLatestBlock(owner);
        String previousBlockHash = latest.getOwnHash();

        // always link to genesis of contact blocks
        String contactBlockHash;
        if (owner.equals(contact))
            contactBlockHash = NA;
        else
            contactBlockHash = getBlocks(contact).get(0).getOwnHash();
        int seqNumber = latest.getSequenceNumber() + 1;

        ConversionController conversionController = new ConversionController(
                owner, publicKey, previousBlockHash, contactBlockHash, iban
        );
        String hash = conversionController.hashKey();
        Block block = BlockFactory.getBlock(
                revoke ? REVOKE : BLOCK,
                owner,
                seqNumber,
                hash,
                previousBlockHash,
                contactBlockHash,
                publicKey,
                iban,
                0
        );
        addBlock(block);
        return block;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Block backtrack(Block block) {
        String previousHashSender = block.getPreviousHashSender();
        Block loop_block = block;
        int i=0;
        
        while (!previousHashSender.equals("N/A")) {
            loop_block = getDatabaseHandler.getByHash(previousHashSender);
            if (loop_block == null) throw new
                    Resources.NotFoundException("Error - Block cannot be backtracked: " + block.toString());
            previousHashSender = loop_block.getPreviousHashSender();
        }

        return loop_block;
    }
}
