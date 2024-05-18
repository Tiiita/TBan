package de.tiiita.punish;

import de.tiiita.punish.reason.PunishmentReason;
import de.tiiita.util.mongodb.UUIDIndexedDocument;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Punishment implements UUIDIndexedDocument {
    protected UUID uniqueId;
    protected UUID targetId;
    protected UUID staffId;
    protected PunishmentReason reason;
    protected OffsetDateTime startTime;
    protected OffsetDateTime endTime;
    protected PunishmentType type;

    public Punishment(UUID uniqueId, UUID targetId, UUID staffId, PunishmentReason reason, OffsetDateTime startTime, OffsetDateTime endTime, PunishmentType type) {
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

    
    public PunishmentReason getReason() {
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

    
    public void setReason(PunishmentReason reason) {
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
