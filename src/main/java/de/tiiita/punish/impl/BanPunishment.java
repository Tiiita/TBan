package de.tiiita.punish.impl;

import de.tiiita.punish.PunishmentType;
import de.tiiita.punish.reason.PunishReason;
import de.tiiita.punish.Punishment;
import de.tiiita.util.PlayerInfo;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.util.UUID;

public class BanPunishment implements Punishment {

    private final UUID player;
    private final UUID staff;
    private final PunishReason reason;
    private final OffsetDateTime startTime;
    private final OffsetDateTime endTime;

    public BanPunishment(UUID player, UUID staff, PunishReason reason, OffsetDateTime startTime, OffsetDateTime endTime) {
        this.player = player;
        this.staff = staff;
        this.reason = reason;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public OffsetDateTime getStartTime() {
        return startTime;
    }

    @Override
    public OffsetDateTime getEndTime() {
        return endTime;
    }

    @Override
    public PunishReason getReason() {
        return reason;
    }

    @Override
    public UUID getStaff() {
        return staff;
    }

    @Override
    public UUID getPlayer() {
        return player;
    }

    @Override
    public UUID getPunishId() {
        return UUID.randomUUID();
    }

    @Override
    public PunishmentType getType() {
        return PunishmentType.BAN;
    }

    @Override
    public void onPunish() {
        ProxiedPlayer onlinePlayer = ProxyServer.getInstance().getPlayer(player);
        if (onlinePlayer == null) return;

        onlinePlayer.disconnect(new TextComponent());
    }
}
