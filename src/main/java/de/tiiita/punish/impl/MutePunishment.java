package de.tiiita.punish.impl;

import de.tiiita.punish.Punishment;
import de.tiiita.punish.PunishmentType;
import de.tiiita.punish.reason.PunishReason;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MutePunishment implements Punishment {
    private final UUID uniqueId;
    private final UUID targetId;
    private final UUID staffId;
    private final PunishReason reason;
    private final OffsetDateTime startTime;
    private final OffsetDateTime endTime;

    public MutePunishment(UUID uniqueId, UUID targetId, UUID staffId, PunishReason reason, OffsetDateTime startTime, OffsetDateTime endTime) {
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
        return PunishmentType.MUTE;
    }

    @Override
    public void onPunish() {}
}
