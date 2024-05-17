package de.tiiita;

import de.tiiita.util.ConfigWrapper;
import de.tiiita.util.mongodb.Clients;
import de.tiiita.util.mongodb.MongoDBCollectionClient;
import de.tiiita.util.mongodb.config.ConnectionConfig;
import de.tiiita.util.mongodb.impl.punishments.PunishmentDocument;
import de.tiiita.util.mongodb.impl.punishments.PunishmentDocumentAdapter;
import net.md_5.bungee.api.plugin.Plugin;

public final class TBan extends Plugin {

    private ConfigWrapper config;
    private MongoDBCollectionClient<PunishmentDocument> punishmentDatabaseClient;
    private MongoDBCollectionClient<PunishmentDocument> playerDatabaseClient;

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = new ConfigWrapper("config.yml", getDataFolder(), this);
        connectDatabase();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void connectDatabase() {
        String username = config.getString("mongodb.username");
        String password = config.getString("mongodb.password");
        String host = config.getString("mongodb.host");
        String database = config.getString("mongodb.database");
        int port = config.getInt("mongodb.port");

        ConnectionConfig connectionConfig = ConnectionConfig.create(username, password.toCharArray(), host, port, database);
        this.punishmentDatabaseClient = new MongoDBCollectionClient<>(Clients.createClient(connectionConfig),
                new PunishmentDocumentAdapter(), database, "punishments");

    }
}
