package de.tiiita.util.mongodb;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Serves as an adapter between a {@link UUIDIndexedDocument} type and a MongoDB/BSON {@link Document}
 * @param <T>
 */
public interface DocumentAdapter<T extends UUIDIndexedDocument> {

    /**
     * Converts a mongodb bson document to a player document.
     * @param document Takes in a document and returns a
     * @return The playerDocument if it could be deserialized from the bson document.
     */
    T fromBson(@NotNull Document document);


    /**
     * Converts a player document to a mongodb bson document.
     * @param document The player document
     * @return returns the mongodb bson Document
     */
    @NotNull
    Document toBson(@NotNull T document);

    /**
     * Return an empty document with just the player's uuid.
     * @param uuid The player's uuid
     * @return An empty player document
     */
    T getEmpty(UUID uuid);
}
