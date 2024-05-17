package de.tiiita.punish;

import de.tiiita.punish.reason.PunishReason;
import java.time.OffsetDateTime;
import java.util.UUID;

public interface Punishment {
    OffsetDateTime getStartTime();
    OffsetDateTime getEndTime();
    PunishReason getReason();
    UUID getStaff();
    UUID getPlayer();
    UUID getPunishId();
    PunishmentType getType();
    void onPunish();
}
