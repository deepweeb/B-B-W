package nl.tudelft.b_b_w;

import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Singleton key store of all public and private keys. Handles caching and database connection
 * as well as private key storing and loading.
 */

public class Store {
    private Set<String> keys;

    public Store() {
        keys = new HashSet<String>();

        if (new File("keys.txt").exists()) {
            try {
                Scanner sc = new Scanner("keys.txt");
                while (sc.hasNext()) {
                    String key = sc.next();
                    addKey(key);
                }
                sc.close();
            } catch (Exception e) {
                Toast.makeText(MainActivity.self, "Could not load private keys", Toast.LENGTH_LONG);
            }
        }
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter("keys.txt");
        } catch (IOException e) {
            Toast.makeText(MainActivity.self, "Could not store private keys", Toast.LENGTH_LONG);
        }
    }


    public void addKey(String key) {
        keys.add(key);
    }

    // singleton nice
    private static Store store;

    public static Store getStore() {
        if (store == null)
            store = new Store();
        return store;
    }
}
