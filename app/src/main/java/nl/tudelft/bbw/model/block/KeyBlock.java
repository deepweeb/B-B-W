package nl.tudelft.bbw.model.block;

import nl.tudelft.bbw.model.HashException;

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
