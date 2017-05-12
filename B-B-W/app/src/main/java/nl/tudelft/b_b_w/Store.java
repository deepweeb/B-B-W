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
    }

    public void read() {
        try {
            Scanner sc = new Scanner(new File("keys.txt"));
            while (sc.hasNext()) {
                String key = sc.next();
                addKey(key);
            }
            sc.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not load private keys");
//            Toast.makeText(MainActivity.self, "Could not load private keys", Toast.LENGTH_LONG);
        }
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter("keys.txt");
            for (String s: keys) {
                writer.write(s);
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not store private keys");
//            Toast.makeText(MainActivity.self, "Could not store private keys", Toast.LENGTH_LONG);
        }
    }

    public Set<String> getKeys() {
        return keys;
    }


    public void addKey(String key) {
        keys.add(key);
    }
}
