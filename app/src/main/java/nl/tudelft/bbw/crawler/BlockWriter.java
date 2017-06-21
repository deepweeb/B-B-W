package nl.tudelft.bbw.crawler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Class to write a block to a file
 */
final class BlockWriter {

    /**
     * Class attributes
     */
    public static final String OUTPUT_PATH = "output.json";

    /**
     * Empty constructor
     * Ensures that the class cannot be instantiated
     */
    private BlockWriter() {

    }

    /**
     * writeToJson function
     * Writes the given mapper to the output file
     *
     * @param mapper
     * @throws IOException
     */
    static void writeToJson(Map mapper) throws IOException {
        Writer writer = new FileWriter(OUTPUT_PATH);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(mapper, writer);
        writer.close();
    }

}
