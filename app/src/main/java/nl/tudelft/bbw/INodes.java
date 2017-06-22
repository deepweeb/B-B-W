package nl.tudelft.bbw;

/**
 * Created by jasper on 22-6-17.
 */

public interface INodes {
    int getLength();
    INodes getChild(int i);
    String getName(int i);
}
