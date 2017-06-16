package nl.tudelft.bbw.database;

/**
 * Exception thrown when the database reports an error.
 * We cannot fix this from insdide the app but we should report it properly.
 */
public class DatabaseException extends RuntimeException {
    /**
     * {@inheritDoc}
     */
    public DatabaseException(String message) {
        super(message);
    }
}
