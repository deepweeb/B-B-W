package nl.tudelft.b_b_w.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Information storage for a user.
 */
public class User {
    private int id;
    private String name;
    private String iban;
    private double trust;
    private List<String> publicKeys;

    /**
     * Fill in immutable fielsd. Other fields should not be given to the constructor
     * but be calculated directly.
     * @param name
     * @param iban
     */
    public User(int id, String name, String iban) {
        this.id = id;
        this.name = name;
        this.iban = iban;
        this.publicKeys = new ArrayList<String>();
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIban() {
        return iban;
    }

    public double getTrust() {
        return trust;
    }

    public void addPublicKey(String key) {
        publicKeys.add(key);
    }

    public List<String> getPublicKeys() {
        return publicKeys;
    }
}
