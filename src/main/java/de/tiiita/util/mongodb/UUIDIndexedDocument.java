package de.tiiita.util.mongodb;

import java.util.UUID;


/**
 * Represents a mongodb/BSON document that's indexed by a UUID.
 */
public interface UUIDIndexedDocument {
    UUID getUniqueId();
}
