package nl.tudelft.bbw;

/**
 * Created by jasper on 22-6-17.
 */

public class ArrayNodes implements INodes {
    String[] names;
    INodes[] children;

    public ArrayNodes(String[] names, INodes[] children) {
        if (names.length != children.length)
            throw new IllegalArgumentException("array size mismatch");
        this.names = names;
        this.children = children;
    }

    @Override
    public int getLength() {
        return names.length;
    }

    @Override
    public INodes getChild(int i) {
        return children[i];
    }

    @Override
    public String getName(int i) {
        return names[i];
    }
}
