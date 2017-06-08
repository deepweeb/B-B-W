package nl.tudelft.b_b_w.Objects;


import nl.tudelft.b_b_w.model.block.BlockData;

/**
 * This class represents a block object.
 */
public class Block {

    private User contact;
    private String blockOwner;
    private BlockData blockData;

    /**
     * This method constructs an block object.
     * @param contact
     *                  a contact.
     * @param blockOwner
     *                  the owner of the chain.
     * @param blockData
     *                  some block data.
     */
    public Block (User contact, String blockOwner, BlockData blockData){
        this.contact = contact;
        this.blockOwner = blockOwner;
        this.blockData = blockData;
    }

    /**
     * This method returns the contact of the block object.
     * @return
     *          the contact object.
     */
    public User getContact() {
        return contact;
    }


    /**
     * This method returns the chainOwner of the block object.
     * @return   chain owner.
     */
    public String getBlockOwner() {
        return blockOwner;
    }

    /**
     * This method returns the data of the block object.
     * @return
     *          the block data.
     */
    public BlockData getBlockData() {
        return blockData;
    }
}
