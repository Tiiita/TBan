package de.tiiita.punish;

import de.tiiita.punish.reason.PunishReason;
import de.tiiita.util.ConfigWrapper;
import de.tiiita.util.MojangNameFetcher;
import de.tiiita.util.mongodb.DateTimeDifferenceFormatter;
import de.tiiita.util.mongodb.impl.player.PlayerDocument;
import de.tiiita.util.mongodb.MongoDBCollectionClient;
import de.tiiita.util.mongodb.impl.punishments.PunishmentDocument;
import net.md_5.bungee.api.ProxyServer;

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
    private final ConfigWrapper config;

    public PunishManager(MongoDBCollectionClient<PunishmentDocument> punishmentDatabaseClient, MongoDBCollectionClient<PlayerDocument> playerDatabaseClient, ConfigWrapper config) {
        this.punishmentDatabaseClient = punishmentDatabaseClient;
        this.playerDatabaseClient = playerDatabaseClient;
        this.config = config;
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

    public CompletableFuture<String> getPunishScreen(PunishmentDocument punishmentDocument, PunishmentType type) {
        return MojangNameFetcher.fetchName(punishmentDocument.getStaffId()).thenApplyAsync(staffName -> {
            switch (type) {
                case IPBAN:
                case BAN: {
                    StringBuilder stringBuilder = new StringBuilder();
                    List<String> stringList = config.getStringList("ban-screen");

                    for (int i = 0; i < stringList.size(); i++) {

                        String line = replacePlaceholder(punishmentDocument, staffName, stringList.get(i));

                        if (i == 0) {
                            stringBuilder.append(line);
                        } else stringBuilder.append("\n").append(line);
                    }

                    return stringBuilder.toString();
                }

                case MUTE:
                    return replacePlaceholder(punishmentDocument, staffName, config.getString("mute-screen"));
                default:
                    return "§cAn error occurred, please report to admin (TBan - Unkown Punish Type)";
            }
        });
    }

    private String replacePlaceholder(PunishmentDocument punishmentDocument, String staffName, String text) {
        return text
                .replaceAll("&", "§")
                .replaceAll("%reason%", punishmentDocument.getReason().getName())
                .replaceAll("%punishId%", punishmentDocument.getUniqueId().toString())
                .replaceAll("%remaining%", DateTimeDifferenceFormatter.formatDateTimeDifference(punishmentDocument.getStartTime(),
                        punishmentDocument.getEndTime()))
                .replaceAll("%staff%", staffName);

    }
}
