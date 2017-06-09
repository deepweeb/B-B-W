package nl.tudelft.b_b_w.controller;

import android.content.Context;

import nl.tudelft.b_b_w.model.GetDatabaseHandler;
import nl.tudelft.b_b_w.model.MutateDatabaseHandler;
import nl.tudelft.b_b_w.model.TrustValues;
import nl.tudelft.b_b_w.model.block.Block;

/**
 * Created by Ashay on 08/06/2017.
 */

public class TrustValueController {

    private Context context;
    private GetDatabaseHandler getDatabaseHandler;
    private MutateDatabaseHandler mutateDatabaseHandler;

    public TrustValueController(Context context) {
        this.context = context;
        this.getDatabaseHandler = new GetDatabaseHandler(context);
        this.mutateDatabaseHandler = new MutateDatabaseHandler(context);
    }

    public final Block verifyIBAN(Block block) {
        block.setTrustValue(TrustValues.VERIFIED.getValue());
        mutateDatabaseHandler.updateBlock(block);
        return block;
    }

    public final Block successfulTransaction(Block block) {
        final int maxCeil = 100;
        final int newTrust = block.getTrustValue() + TrustValues.SUCCESFUL_TRANSACTION.getValue();
        if (newTrust > maxCeil) {
            block.setTrustValue(maxCeil);
        }
        block.setTrustValue(newTrust);
        mutateDatabaseHandler.updateBlock(block);
        return block;
    }

    public final Block failedTransaction(Block block) {
        final int minCeil = 0;
        final int newTrust = block.getTrustValue() + TrustValues.FAILED_TRANSACTION.getValue();
        if (newTrust < minCeil) {
            block.setTrustValue(minCeil);
        }
        block.setTrustValue(newTrust);
        mutateDatabaseHandler.updateBlock(block);
        return block;
    }

    public final Block revokedTrustValue(Block block) {
        block.setTrustValue(TrustValues.REVOKED.getValue());
        mutateDatabaseHandler.updateBlock(block);
        return block;
    }
}
