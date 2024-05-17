package de.tiiita.util.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.lang.Nullable;
import de.tiiita.util.mongodb.config.ConnectionConfig;
import org.bson.UuidRepresentation;
import org.jetbrains.annotations.NotNull;

/**
 * Util class for creating {@link MongoClient}s intended for being used when creating {@link MongoDBCollectionClient}s
 * {@link #createClient} methods create MongoClients with predefined settings set up for saving UUID indexed documents.
 */
public class Clients {

    /**
     * @see #createSettings(ConnectionConfig, MongoClientSettings)
     */
    public static MongoClient createClient(ConnectionConfig connectionConfig) {
        return createClient(connectionConfig, null);
    }

    /**
     * Creates a MongoClient with predefined setting. If additional settings are needed, an additional {@link MongoClientSettings}
     *
     * @param connectionConfig A {@link ConnectionConfig} object that contains information
     *                         needed to connect to the database including information to create the credential.
     * @param mongoClientSettings An optional {@link MongoClientSettings} object if additional MongoClient configuration is needed.
     *                            Is potentially overridden by the predefined settings.
     * @return A new MongoClient that can be used for queries at the database given in the config.
     */
    public static MongoClient createClient(ConnectionConfig connectionConfig, @Nullable MongoClientSettings mongoClientSettings) {
        MongoClientSettings settings = createSettings(connectionConfig);
        return MongoClients.create(settings);
    }

    /**
     * @see #createSettings(ConnectionConfig, MongoClientSettings)
     */
    public static MongoClientSettings createSettings(@NotNull ConnectionConfig connectionConfig) {
        return createSettings(connectionConfig, null);
    }

    /**
     * Creates a {@link MongoClientSettings} object with predefined settings intended for use with {@link MongoDBCollectionClient}
     * @param connectionConfig A connection config object used for creating the {@link ConnectionConfig}
     * @param mongoClientSettings An optional {@link MongoClientSettings} object if additional MongoClient configuration is needed.
     *                            Is potentially overridden by the predefined settings.
     * @return A {@link MongoClientSettings} object with predefined settings intended for use with {@link MongoDBCollectionClient}
     */
    public static MongoClientSettings createSettings(@NotNull ConnectionConfig connectionConfig, @Nullable MongoClientSettings mongoClientSettings) {
        MongoClientSettings.Builder builder =
                (mongoClientSettings != null) ?
                        MongoClientSettings.builder(mongoClientSettings) :
                        MongoClientSettings.builder();
        return builder
                .uuidRepresentation(UuidRepresentation.STANDARD)// uses java.util.UUID to save uuids
                .applyConnectionString(new ConnectionString("mongodb://" + connectionConfig.getHost() + ":" + connectionConfig.getPort()))
                .credential(MongoCredential.createCredential(connectionConfig.getUser(), connectionConfig.getDatabase(), connectionConfig.getPassword()))
                .build();
    }
}
