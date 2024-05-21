package de.tiiita;

import de.tiiita.command.PunishCommand;
import de.tiiita.listener.PunishListener;
import de.tiiita.minecraft.bungee.BungeeConfig;
import de.tiiita.punish.PunishService;
import de.tiiita.punish.Punishment;
import de.tiiita.report.ReportListener;
import de.tiiita.report.ReportService;
import de.tiiita.util.mongodb.Clients;
import de.tiiita.util.mongodb.MongoDBCollectionClient;
import de.tiiita.util.mongodb.config.ConnectionConfig;
import de.tiiita.util.mongodb.impl.player.PlayerDocument;
import de.tiiita.util.mongodb.impl.player.PlayerDocumentAdapter;
import de.tiiita.util.mongodb.impl.punishments.PunishmentDocumentAdapter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public final class TBan extends Plugin {

    private BungeeConfig config;
    private PunishService punishService;
    private ReportService reportService;
    private MongoDBCollectionClient<Punishment> punishmentDatabaseClient;
    private MongoDBCollectionClient<PlayerDocument> playerDatabaseClient;


    @Override
    public void onEnable() {
        // Plugin startup logic
        config = new BungeeConfig("config.yml", this);

        connectDatabase();
        punishService = new PunishService(punishmentDatabaseClient, playerDatabaseClient, config);
        reportService = new ReportService();


        registerListener();
        registerCommand();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void registerCommand() {
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();

        pluginManager.registerCommand(this, new PunishCommand(punishService, config));
    }

    private void registerListener() {
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();

        pluginManager.registerListener(this, new PunishListener(punishService));
        pluginManager.registerListener(this, new ReportListener(reportService));
    }

    private void connectDatabase() {
        String username = config.getString("mongodb.username");
        String password = config.getString("mongodb.password");
        String host = config.getString("mongodb.host");
        String database = config.getString("mongodb.database");
        int port = config.getInt("mongodb.port");

        ConnectionConfig connectionConfig = ConnectionConfig.create(username, password.toCharArray(), host, port, database);
        punishmentDatabaseClient = new MongoDBCollectionClient<>(Clients.createClient(connectionConfig),
                new PunishmentDocumentAdapter(config), database, "punishments");

        playerDatabaseClient = new MongoDBCollectionClient<>(Clients.createClient(connectionConfig),
                new PlayerDocumentAdapter(), database, "players");
    }
}
