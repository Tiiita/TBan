package de.tiiita.punish;

import de.tiiita.util.reason.Reason;
import de.tiiita.util.mongodb.UUIDIndexedDocument;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Punishment implements UUIDIndexedDocument {
    private UUID uniqueId;
    private UUID targetId;
    private UUID staffId;
    private Reason reason;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private PunishmentType type;

    protected Punishment(UUID uniqueId, UUID targetId, UUID staffId, Reason reason, OffsetDateTime startTime, OffsetDateTime endTime, PunishmentType type) {
        this.uniqueId = uniqueId;
        this.targetId = targetId;
        this.staffId = staffId;
        this.reason = reason;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }
    public Punishment() {}

    public OffsetDateTime getStartTime() {
        return startTime;
    }


    public OffsetDateTime getEndTime() {
        return endTime;
    }


    public Reason getReason() {
        return reason;
    }

    public UUID getStaffId() {
        return staffId;
    }


    public UUID getTargetId() {
        return targetId;
    }


    public UUID getUniqueId() {
        return uniqueId;
    }


    public PunishmentType getType() {
        return type;
    }


    public void setStartTime(OffsetDateTime dateTime) {
        this.startTime = dateTime;
    }


    public void setEndTime(OffsetDateTime dateTime) {
        this.endTime = dateTime;
    }


    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setTargetId(UUID targetId) {
        this.targetId = targetId;
    }

    public void setType(PunishmentType type) {
        this.type = type;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }
}
