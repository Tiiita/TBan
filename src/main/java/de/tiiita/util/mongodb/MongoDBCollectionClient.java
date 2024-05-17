package de.tiiita.util.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import de.tiiita.util.mongodb.config.ConnectionConfig;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A MongoDB client wrapper that eases CRUD operations with simple "{@link UUIDIndexedDocument}s".
 * @param <T> A UUID indexed document.
 */
public class MongoDBCollectionClient<T extends UUIDIndexedDocument> {
    private final MongoClient client;
    private final DocumentAdapter<T> documentAdapter;
    private final String collectionName;
    private final String databaseName;

    /**
     * Creates a new MongoDBCollectionClient
     * @param client A {@link MongoClient} instance. Recommended to be created with
     * @param documentAdapter An adapter adapting between {@link Document} and a {@link UUIDIndexedDocument}.
     * @param databaseName The name of the database you want to connect against.
     * @param collectionName The name of the collection where the given document type lives.
     */
    public MongoDBCollectionClient(MongoClient client, DocumentAdapter<T> documentAdapter, String databaseName, String collectionName) {
        this.client = client;
        this.documentAdapter = documentAdapter;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
        setUpIndex();
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public MongoDBCollectionClient(ConnectionConfig config, DocumentAdapter<T> documentAdapter, String collectionName) {
        this(Clients.createClient(config), documentAdapter, config.getDatabase(), collectionName);
    }

    private void setUpIndex() {
        getCollection().createIndex(Indexes.ascending("uuid"));
    }

    /**
     * Creates a {@link Bson} filter to filter for the given {@link UUID}
     * @param uuid The uuid to filter for.
     * @return The filter to filter for the given {@link UUID}
     */
    private Bson uuidFilter(@NotNull UUID uuid) {
        return Filters.eq("uuid", uuid);
    }

    public CompletableFuture<Void> upsert(@NotNull T playerDocument) {
        return CompletableFuture.runAsync(() -> upsertSync(playerDocument));
    }

    /**
     * Uploads the given player document to the database.
     * For the case it is already present in the database
     * (meaning a document with the passed document's uuid is already present),
     * it will override the document in the database with the passed document.
     *
     * @param playerDocument The document to upload.
     */
    public void upsertSync(@NotNull T playerDocument) {
        MongoCollection<Document> collection = getCollection();
        collection.replaceOne(
                uuidFilter(playerDocument.getUniqueId()),
                documentAdapter.toBson(playerDocument),
                new ReplaceOptions().upsert(true)
        );
    }

    /**
     * For the given UUID, gets a document from the database, updates it with the given function
     * and then uploads it again, thus updating the document in the database with the given updater function.
     * If no document is found in the database, it will apply the function to a new empty document instead and upload that one.
     * For this to work properly, the {@link DocumentAdapter}'s {@link DocumentAdapter#getEmpty(UUID)} method must be
     * implemented correctly.
     *
     * @param uuid            The player's uuid of whose document should be updated.
     * @param updaterFunction The updater function that is performed on the document.
     * @return Returns the updated document
     * @throws NullPointerException If the updater function returns null.
     */
    @Nullable
    public T upsertSync(@NotNull UUID uuid, Function<T, T> updaterFunction) {
        Optional<T> documentOptional = getSync(uuid);
        T updatedDocument;
        if (documentOptional.isPresent()) {
            updatedDocument = documentOptional.get();
            updatedDocument = updaterFunction.apply(updatedDocument);
        } else {
            updatedDocument = updaterFunction.apply(documentAdapter.getEmpty(uuid));
        }
        if (updatedDocument == null) { // if the updater function returns null
            throw new NullPointerException("Updater function returned null");
        } else {
            upsertSync(updatedDocument);
        }
        return updatedDocument;
    }

    /**
     * @param uuid            The player's uuid of whose document should be updated.
     * @param updaterFunction The updater function that is performed on the document.
     * @return A completable future that is completed once the document is updated. Also contains the updated document.
     * @see #upsertSync(UUID, Function)
     */
    public CompletableFuture<T> upsert(@NotNull UUID uuid, Function<T, T> updaterFunction) {
        return CompletableFuture.supplyAsync(() -> upsertSync(uuid, updaterFunction));
    }

    public CompletableFuture<Optional<T>> get(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getSync(uuid));
    }

    public Optional<T> getSync(@NotNull UUID uuid) {
        Document document = getCollection().find(uuidFilter(uuid)).first();
        if (document == null) return Optional.empty();
        return Optional.of(documentAdapter.fromBson(document));
    }

    public CompletableFuture<Void> delete(@NotNull UUID uuid) {
        return CompletableFuture.runAsync(() -> deleteSync(uuid));
    }

    public void deleteSync(@NotNull UUID uuid) {
        getCollection().deleteOne(uuidFilter(uuid));
    }

    /**
     * Checks if a document with the given UUID exists in the database.
     *
     * @param uuid The uuid you want to check for.
     * @return True if the document exists in the database, false if not.
     */
    public boolean isPresentSync(@NotNull UUID uuid) {
        return getSync(uuid).isPresent();
    }


    /**
     * Checks if a document with the given UUID exists in the database asynchronously.
     *
     * @param uuid The uuid you want to check for.
     * @return A completable future that completes as soon as the database operation is done.
     * The future contains true if the document exists in the database, false if not.
     * @see #isPresentSync(UUID)
     */
    public CompletableFuture<Boolean> isPresent(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getSync(uuid).isPresent());
    }

    public MongoCollection<Document> getCollection() {
        return getDatabase().getCollection(collectionName);
    }

    public MongoDatabase getDatabase() {
        return client.getDatabase(databaseName);
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public DocumentAdapter<T> getDocumentAdapter() {
        return documentAdapter;
    }

    /**
     * Frees up resources used by this client
     */
    public void close() {
        client.close();
    }

    /**
     * @return Returns the internal {@link MongoClient}
     */
    public MongoClient getMongoClient() {
        return client;
    }

    public String getCollectionName() {
        return collectionName;
    }
}