package nl.tudelft.b_b_w.model.block;

import nl.tudelft.b_b_w.model.HashException;

/**
 * Block for adding a key
 */
public class AddKeyBlock extends KeyBlock {
    public AddKeyBlock(BlockData blockData) throws HashException {
        super(blockData);

        // verify integrity
        assert (blockData.getBlockType() == BlockType.ADD_KEY);
    }
}
