package de.tiiita.punish.impl;

import de.tiiita.punish.PunishmentType;
import de.tiiita.punish.reason.PunishReason;
import de.tiiita.punish.Punishment;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.util.UUID;

public class BanPunishment implements Punishment {

    private final UUID uniqueId;
    private final UUID targetId;
    private final UUID staffId;
    private final PunishReason reason;
    private final OffsetDateTime startTime;
    private final OffsetDateTime endTime;

    public BanPunishment(UUID uniqueId, UUID targetId, UUID staffId, PunishReason reason, OffsetDateTime startTime, OffsetDateTime endTime) {
        this.uniqueId = uniqueId;
        this.targetId = targetId;
        this.staffId = staffId;
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
    public UUID getStaffId() {
        return staffId;
    }

    @Override
    public UUID getTargetId() {
        return targetId;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public PunishmentType getType() {
        return PunishmentType.BAN;
    }

    @Override
    public void onPunish() {
        ProxiedPlayer onlinePlayer = ProxyServer.getInstance().getPlayer(targetId);
        if (onlinePlayer == null) return;

        onlinePlayer.disconnect(new TextComponent());
    }
}
