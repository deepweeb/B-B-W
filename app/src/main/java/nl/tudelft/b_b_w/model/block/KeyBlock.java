package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.model.HashException;

/**
 * Block for adding or revoking a key
 */
public abstract class KeyBlock extends Block {
    /**
     * {@inheritDoc}
     */
    public KeyBlock(BlockData blockData) throws HashException {
        super(blockData);
    }
}
