package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.model.HashException;

/**
 * Block for adding or revoking a key
 */
public abstract class KeyBlock extends Block {
    public KeyBlock(BlockData blockData) throws HashException {
        super(blockData);
    }

    public abstract boolean isRevoked();
}
