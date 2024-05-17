package de.tiiita.util.mongodb;

/**
 * Is thrown when no player document could be serialized from a bson document.
 * Currently unused.
 */
public class PlayerDocumentSerializationException extends Exception {

    public PlayerDocumentSerializationException(String message) {
        super(message);
    }

    public PlayerDocumentSerializationException() {
    }
}
