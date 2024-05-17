package de.tiiita.util.mongodb.impl.punishments;

import de.tiiita.punish.Punishment;
import de.tiiita.punish.reason.PunishReason;
import de.tiiita.util.mongodb.UUIDIndexedDocument;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PunishmentDocument implements UUIDIndexedDocument {

    private final UUID uniqueId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private PunishReason reason;
    private UUID staffId;
    private UUID targetId;

    public PunishmentDocument(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public static PunishmentDocument fromPunishment(Punishment punishment) {
        PunishmentDocument punishmentDocument = new PunishmentDocument(punishment.getUniqueId());
        punishmentDocument.setStartTime(punishment.getStartTime());
        punishmentDocument.setEndTime(punishment.getEndTime());
        punishmentDocument.setReason(punishment.getReason());
        punishmentDocument.setTargetId(punishment.getTargetId());
        punishmentDocument.setStaffId(punishment.getStaffId());
        return punishmentDocument;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }

    public void setReason(PunishReason reason) {
        this.reason = reason;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public void setTargetId(UUID targetId) {
        this.targetId = targetId;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
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

    public UUID getStaffId() {
        return staffId;
    }

    public UUID getTargetId() {
        return targetId;
    }
}
