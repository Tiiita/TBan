package de.tiiita.punish;

import de.tiiita.punish.reason.PunishmentReason;
import de.tiiita.util.ConfigWrapper;
import de.tiiita.util.MojangNameFetcher;
import de.tiiita.util.mongodb.DateTimeDifferenceFormatter;
import de.tiiita.util.mongodb.impl.player.PlayerDocument;
import de.tiiita.util.mongodb.MongoDBCollectionClient;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * This class uses sync database operations. It will block the thread it is called in.
 * This means, using this without calling the method in a different thread than the main
 * you will block the main thread and the server will have huge lag.
 */
public class PunishService {

    private final MongoDBCollectionClient<Punishment> punishmentDatabaseClient;
    private final MongoDBCollectionClient<PlayerDocument> playerDatabaseClient;
    private final ConfigWrapper config;

    public PunishService(MongoDBCollectionClient<Punishment> punishmentDatabaseClient,
                         MongoDBCollectionClient<PlayerDocument> playerDatabaseClient, ConfigWrapper config) {
        this.punishmentDatabaseClient = punishmentDatabaseClient;
        this.playerDatabaseClient = playerDatabaseClient;
        this.config = config;
    }

    /**
     * Punish the user with the given uuid wiht a specific punishment.
     * This method will also announce the punishment in the chat (see config)
     * This method also fires a punish event which can be cancelled.
     *
     * @param uniqueId   the uuid of the player that should be punished.
     * @param punishment the implemented punishment.
     */
    public void punish(UUID uniqueId, Punishment punishment) {
        PunishEvent event = new PunishEvent(punishment);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        punishmentDatabaseClient.upsertSync(punishment);
        playerDatabaseClient.upsertSync(uniqueId, playerDocument -> {

            List<UUID> punishments = playerDocument.getPunishmentIds();
            punishments.add(punishment.getUniqueId());
            playerDocument.setPunishmentIds(punishments);
            return playerDocument;

        });

        ProxyServer.getInstance().broadcast(new TextComponent(translatePlaceholder(punishment,
                config.getString("punish-announcement"))));
    }

    /**
     * Remove a punishment from the database. This will NOT remove it from the player
     * it will only set the end date of the punishment to the current datetime.
     * The punishment will still be available in the player punishments list but will be
     * counted as an exceeded one.
     *
     * @param punishId the uuid of the punishment, this is not the uuid of a player!
     */
    public void removePunish(UUID punishId) {
        punishmentDatabaseClient.upsertSync(punishId, punishmentDocument -> {
            punishmentDocument.setEndTime(OffsetDateTime.now());

            PunishmentReason reason = punishmentDocument.getReason();
            reason.setName(reason.getName() + " (Removed)");
            punishmentDocument.setReason(reason);
            return punishmentDocument;
        });
    }

    /**
     * Get a punishment as a java object from the database
     * This has nothing to do with a player punishment. It is the punishment itself.
     * To get a players punishment use {@link #getActivePunish}, {@link #getLatestPunishment}, {@link #getActivePunish}
     *
     * @param punishId the uuid of the punishment.
     * @return the found punishment bind to the punishId or null if no punish was found.
     */
    public Punishment getPunishment(UUID punishId) {
        return punishmentDatabaseClient.get(punishId).join().orElse(null);
    }


    /**
     * Get the first active punishment of a player from a specific punishment type.
     *
     * @param uniqueId the uuid of the player.
     * @param type     the type the active punishments should be filtered of.
     * @return the found active punishment or null if the player does not have any active punishments with the specific type.
     */
    public Punishment getActivePunish(UUID uniqueId, PunishmentType type) {
        Set<Punishment> playerPunishments = getPlayerPunishments(uniqueId);

        for (Punishment punishment : playerPunishments) {
            if (punishment.getEndTime().isAfter(OffsetDateTime.now())
                    && punishment.getType().getId() == type.getId()) {

                return punishment;
            }
        }
        return null;
    }

    /**
     * Checks if a player has an active punish running.
     *
     * @param uniqueId the uuid of the player.
     * @param type     the type of the punishment it should check for.
     * @return true if the player has an active punishment with the type, false if not.
     * @see #getActivePunish(UUID, PunishmentType)
     */
    public Boolean isPunished(UUID uniqueId, PunishmentType type) {
        return getActivePunish(uniqueId, type) != null;
    }

    /**
     * Get a players information sheet from the database.
     * It contains the whole player's data and all punishments the player has had.
     *
     * @param uniqueId the uuid of the player.
     * @return the found document, or null if there is no entry for that uuid.
     */
    public PlayerDocument getPlayerInfo(UUID uniqueId) {
        return playerDatabaseClient.get(uniqueId).join().orElse(null);
    }

    /**
     * Get the latest punishments from the players punishment history.
     *
     * @param uniqueId the uuid of the player.
     * @return the latest punishment or null if the player has not got any punishments yet.
     */
    public Punishment getLatestPunishment(UUID uniqueId) {
        Set<Punishment> punishments = getPlayerPunishments(uniqueId);
        return Collections.max(punishments, Comparator.comparing(Punishment::getStartTime));
    }

    /**
     * Get the player's punishment history from the database.
     *
     * @param uniqueId uuid of the player.
     * @return the set of the punishments, empty if no history or no player was found.
     */
    public Set<Punishment> getPlayerPunishments(UUID uniqueId) {
        PlayerDocument playerInfo = getPlayerInfo(uniqueId);
        if (playerInfo == null) return Collections.emptySet();

        Set<Punishment> playerPunishments = new HashSet<>();
        for (UUID currentPunishmentId : playerInfo.getPunishmentIds()) {
            playerPunishments.add(punishmentDatabaseClient.getSync(currentPunishmentId).orElse(null));
        }

        return playerPunishments;
    }


    /**
     * Translates the punishment into a ban screen or a message that can be broadcast.
     *
     * @param punishment the punishment that should be translated to a message for users.
     * @return the translated message or an error message if the type is not available.
     */
    public String getPunishScreen(Punishment punishment) {
        switch (punishment.getType()) {
            case IPBAN:
            case BAN: {
                StringBuilder stringBuilder = new StringBuilder();
                List<String> stringList = config.getStringList("ban-screen");

                for (int i = 0; i < stringList.size(); i++) {

                    String line = translatePlaceholder(punishment, stringList.get(i));

                    if (i == 0) {
                        stringBuilder.append(line);
                    } else {
                        stringBuilder.append("\n").append(line);
                    }
                }

                return stringBuilder.toString();
            }

            case MUTE:
                return translatePlaceholder(punishment, config.getString("mute-screen"));
            default:
                return "§cAn error occurred, please report to admin (TBan - Unkown Punish Type)";
        }
    }

    private String translatePlaceholder(Punishment punishment, String s) {
        String text = s.replaceAll("&", "§")
                .replaceAll("%reason%", punishment.getReason().getName())
                .replaceAll("%punishId%", punishment.getUniqueId().toString())
                .replaceAll("%remaining%", DateTimeDifferenceFormatter.formatDateTimeDifference(
                        punishment.getStartTime(), punishment.getEndTime()));

        String playerPlaceholder = "%player%";
        String staffPlaceholder = "%staff%";

        if (text.contains(playerPlaceholder)) {

            text = text.replaceAll(playerPlaceholder, MojangNameFetcher.fetchNameSafely(punishment.getTargetId()));
        }
        if (text.contains(staffPlaceholder)) {
            text = text.replaceAll(playerPlaceholder, MojangNameFetcher.fetchNameSafely(punishment.getStaffId()));
        }

        return text;
    }
}
