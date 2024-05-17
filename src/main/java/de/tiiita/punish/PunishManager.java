package de.tiiita.punish;

import de.tiiita.punish.reason.PunishReason;
import de.tiiita.util.mongodb.impl.player.PlayerDocument;
import de.tiiita.util.mongodb.MongoDBCollectionClient;
import de.tiiita.util.mongodb.impl.punishments.PunishmentDocument;
import net.md_5.bungee.api.ProxyServer;

import java.awt.peer.ComponentPeer;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Created on Januar 29, 2023 | 14:16:33
 * (●'◡'●)
 */
public class PunishManager {

    private final MongoDBCollectionClient<PunishmentDocument> punishmentDatabaseClient;
    private final MongoDBCollectionClient<PlayerDocument> playerDatabaseClient;

    public PunishManager(MongoDBCollectionClient<PunishmentDocument> punishmentDatabaseClient, MongoDBCollectionClient<PlayerDocument> playerDatabaseClient) {
        this.punishmentDatabaseClient = punishmentDatabaseClient;
        this.playerDatabaseClient = playerDatabaseClient;
    }

    public void punish(UUID uniqueId, Punishment punishment) {
        punishmentDatabaseClient.upsert(PunishmentDocument.fromPunishment(punishment));
        playerDatabaseClient.upsert(uniqueId, playerDocument -> {
            List<UUID> punishments = playerDocument.getPunishmentIds();
            punishments.add(punishment.getUniqueId());
            playerDocument.setPunishmentIds(punishments);
            return playerDocument;

        });
        punishment.onPunish();

        //Announce, etc
    }

    public CompletableFuture<Void> removePunish(UUID punishId) {
        return CompletableFuture.runAsync(() -> {
            punishmentDatabaseClient.upsertSync(punishId, punishmentDocument -> {
                punishmentDocument.setEndTime(OffsetDateTime.now());

                PunishReason reason = punishmentDocument.getReason();
                reason.setName(reason.getName() + " (Removed)");
                punishmentDocument.setReason(reason);
                return punishmentDocument;
            });
        });
    }

    public CompletableFuture<PunishmentDocument> getPunishment(UUID punishId) {
        return punishmentDatabaseClient.get(punishId)
                .thenApplyAsync(punishmentDocument -> punishmentDocument.orElse(null));
    }

    public String getPunishScreen(PunishmentDocument punishmentDocument) {
        return null;
    }

    public CompletableFuture<PunishmentDocument> getActivePunish(UUID uniqueId, PunishmentType type) {
        return getPlayerPunishments(uniqueId).thenApplyAsync(punishmentDocuments -> {

            for (PunishmentDocument punishmentDocument : punishmentDocuments) {
                if (punishmentDocument.getEndTime().isAfter(OffsetDateTime.now())
                        && punishmentDocument.getPunishmentType().getId() == type.getId()) {

                    return punishmentDocument;
                }
            }

            return null;
        });
    }

    public CompletableFuture<Boolean> isPunished(UUID uniqueId, PunishmentType type) {
        return getActivePunish(uniqueId, type).thenApplyAsync(Objects::nonNull);
    }

    public CompletableFuture<PlayerDocument> getPlayerInfo(UUID uniqueId) {
        return playerDatabaseClient.get(uniqueId)
                .thenApplyAsync(playerDocument -> playerDocument.orElse(null));
    }


    public CompletableFuture<PunishmentDocument> getLatestPunishment(UUID uniqueId) {
        return getPlayerPunishments(uniqueId).thenApplyAsync(punishmentDocuments -> {
            return Collections.max(punishmentDocuments, Comparator.comparing(PunishmentDocument::getStartTime));
        });
    }

    public CompletableFuture<List<PunishmentDocument>> getPlayerPunishments(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerDocument playerInfo = getPlayerInfo(uniqueId).join();
            if (playerInfo == null) {
                ProxyServer.getInstance().getLogger().log(Level.SEVERE, "No player data found with uuid: " + uniqueId);
                return null;
            }


            List<PunishmentDocument> playerPunishments = new ArrayList<>();
            for (UUID currentPunishmentId : playerInfo.getPunishmentIds()) {
                playerPunishments.add(punishmentDatabaseClient.getSync(currentPunishmentId).orElse(null));
            }

            return playerPunishments;

        });
    }
}
