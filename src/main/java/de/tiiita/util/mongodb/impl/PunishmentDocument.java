package de.tiiita.util.mongodb.impl;

import de.tiiita.punish.reason.PunishReason;
import de.tiiita.util.mongodb.UUIDIndexedDocument;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PunishmentDocument implements UUIDIndexedDocument {

    private final UUID punishId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private PunishReason reason;
    private UUID staff;
    private UUID player;

    public PunishmentDocument(UUID punishId) {
        this.punishId = punishId;
    }

    public UUID getUniqueId() {
        return punishId;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }

    public PunishReason getReason() {
        return reason;
    }

    public UUID getStaff() {
        return staff;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public void setStaff(UUID staff) {
        this.staff = staff;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public void setEndTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }

    public void setReason(PunishReason reason) {
        this.reason = reason;
    }
}
