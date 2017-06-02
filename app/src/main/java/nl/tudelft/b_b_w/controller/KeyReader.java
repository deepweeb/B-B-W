package nl.tudelft.b_b_w.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to read the private key from the file
 */
public class KeyReader {

    /**
     * Class variables
     */
    private BufferedReader reader;
    private final String filePath = "app/privateKey.txt";

    /**
     * Constructor method
     */
    public KeyReader() {
        init();
    }

    /**
     * init method
     * Initializes the BufferedReader
     */
    private void init() {
        try {
            this.reader = new BufferedReader(new FileReader(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath);
        }
    }

    /**
     * readKey method
     * Reads the private secret key from the file
     * @return private key
     */
     final String readKey() {
        try {
            return this.reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read line from file: " + filePath);
        }
    }
}
