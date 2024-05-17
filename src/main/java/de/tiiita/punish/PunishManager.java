package de.tiiita.punish;

import de.tiiita.util.PlayerInfo;
import de.tiiita.util.mongodb.MongoDBCollectionClient;
import de.tiiita.util.mongodb.impl.punishments.PunishmentDocument;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Januar 29, 2023 | 14:16:33
 * (●'◡'●)
 */
public class PunishManager {

    private final MongoDBCollectionClient<PunishmentDocument> punishmentDatabaseClient;

    public PunishManager(MongoDBCollectionClient<PunishmentDocument> punishmentDatabaseClient) {
        this.punishmentDatabaseClient = punishmentDatabaseClient;
    }

    public void punish(UUID targetId, Punishment punishment) {
        punishmentDatabaseClient.upsert(PunishmentDocument.fromPunishment(punishment));
        punishment.onPunish();

        //Announce, etc
    }

    public CompletableFuture<Void> removePunish(UUID playerId) {


        return punishmentDatabaseClient.delete(playerId);


        //TODO: Ban should be removes as an active punish but not overall, so it should be a new
        // Entry in the history collection
    }

    public CompletableFuture<PunishmentDocument> getPunishment(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<PunishmentDocument> documentOptional = punishmentDatabaseClient.getSync(uniqueId);
            return documentOptional.orElse(null);
        });
    }

    public String getPunishScreen(int reasonId) {
        return null;
    }

    public boolean isPunished(UUID playerId) {
        return false;
    }

    public PlayerInfo getPlayerInfo(UUID uuid) {
        return null;
    }
}
