package nl.tudelft.bbw.crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to read the crawled database
 */
final class DatabaseReader {

    /**
     * Class attributes
     */
    static final String PATH = "app/src/main/assets/databases/";
    static final String FILE_NAME = "blockdatabase.db";

    /**
     * Private constructor
     * Ensures that the class cannot be instantiated
     */
    private DatabaseReader() {

    }

    /**
     * readDatabase function
     * Interprets the database and converts it to a string
     *
     * @return the interpreted string
     * @throws IOException when the file cannot be read
     */
    static String readDatabase() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH + FILE_NAME));
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

}
