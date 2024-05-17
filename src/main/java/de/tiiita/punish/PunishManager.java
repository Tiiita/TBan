package de.tiiita.punish;

import de.tiiita.util.PlayerInfo;
import de.tiiita.util.mongodb.MongoDBCollectionClient;
import de.tiiita.util.mongodb.impl.PunishmentDocument;

import java.rmi.server.UID;
import java.util.UUID;

/**
 * Created on Januar 29, 2023 | 14:16:33
 * (●'◡'●)
 */
public class PunishManager {

    private final MongoDBCollectionClient<PunishmentDocument> punishmentDatabaseClient;

    public PunishManager(MongoDBCollectionClient<PunishmentDocument> punishmentDatabaseClient) {
        this.punishmentDatabaseClient = punishmentDatabaseClient;
    }

    public void punish(UUID uuid, Punishment punishment) {


    }

    public void removePunish(UUID uuid) {

    }

    public UUID getPunishId(UUID uuid) {
        return null;
    }

    public String getPunishScreen(int reasonId) {
        return null;
    }

    public boolean isPunished(UUID uuid) {
        return false;
    }

    public PlayerInfo getPlayerInfo(UUID uuid) {


        return null;
    }
}
