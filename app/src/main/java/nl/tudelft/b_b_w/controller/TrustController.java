package nl.tudelft.b_b_w.controller;

import nl.tudelft.b_b_w.model.TrustValues;
import nl.tudelft.b_b_w.model.block.Block;

/**
 * Class to edit the trust value of a block
 */
class TrustController {

    /**
     * Regularization parameters
     */
    private static final int REGULARIZATION_TRANSACTION = 1;
    private static final int REGULARIZATION_VERIFIED_IBAN = 3;

    /**
     * succesfulTransaction
     * Calculates the new trust value given a succesful transaction
     *
     * @param block the given block
     * @return the block with the new trust value
     */
    static Block succesfulTransaction(Block block) {
        final double newValue = distributionFunction(getX(block.getTrustValue())
                + REGULARIZATION_TRANSACTION);
        block.setTrustValue(newValue);
        return block;
    }

    /**
     * failedTransaction
     * Calculates the new trust value given a failed transaction
     *
     * @param block the given block
     * @return the block with the new trust value
     */
    static Block failedTransaction(Block block) {
        final double newValue = checkCeiling(distributionFunction(getX(block.getTrustValue())
                - REGULARIZATION_TRANSACTION));
        block.setTrustValue(newValue);
        return block;
    }

    /**
     * verifiedIBAN
     * Calculates the new trust value given the IBAN is verfied
     *
     * @param block the given block
     * @return the block with the new trust value
     */
    static Block verifiedIBAN(Block block) {
        final double newValue = distributionFunction(getX(block.getTrustValue())
                + REGULARIZATION_VERIFIED_IBAN);
        block.setTrustValue(newValue);
        return block;
    }

    /**
     * revokeBlock
     * Sets the block value to the revoked trust value
     *
     * @param block given block to revoke
     * @return block with the new trust value
     */
    static Block revokeBlock(Block block) {
        block.setTrustValue(TrustValues.REVOKED.getValue());
        return block;
    }

    /**
     * distributionFunction
     * The exponential distribution function which distributes the trust value
     *
     * @param x the given parameter to calculate the distribution value from
     * @return the distribution value
     */
    private static double distributionFunction(double x) {
        return 100 - 100 * Math.exp(-0.05 * x);
    }

    /**
     * getX
     * Gets the parameter of which the distribution value belongs to
     *
     * @param y given distribution value
     * @return parameter belonging to the distribution value
     */
    private static double getX(double y) {
        return Math.log(1 - y / 100) * -20;
    }

    /**
     * checkCeiling
     * Checks the limit of the value and limits it to a value above 0
     *
     * @param value given value to check
     * @return new value with limits checked
     */
    private static double checkCeiling(double value) {
        if (value < 0) {
            return 0;
        } else {
            return value;
        }
    }
}