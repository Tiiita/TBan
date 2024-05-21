package de.tiiita.util.mongodb.impl.punishments;

import de.tiiita.minecraft.bungee.BungeeConfig;
import de.tiiita.punish.Punishment;
import de.tiiita.punish.PunishmentType;
import de.tiiita.util.reason.Reason;
import de.tiiita.util.mongodb.DocumentAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PunishmentDocumentAdapter implements DocumentAdapter<Punishment> {
    private final BungeeConfig config;

    public PunishmentDocumentAdapter(BungeeConfig config) {
        this.config = config;
    }

    @Override
    public Punishment fromBson(@NotNull Document document) {
        Punishment punishment = new Punishment();
        punishment.setType(PunishmentType.fromName(document.getString("type")));
        punishment.setTargetId(document.get("targetId", UUID.class));
        punishment.setStaffId(UUID.fromString(document.getString("staffId")));
        punishment.setReason(Reason.fromId(document.getInteger("reason"), config));
        punishment.setStartTime(OffsetDateTime.parse(document.getString("startTime")));
        punishment.setEndTime(OffsetDateTime.parse(document.getString("endTime")));
        punishment.setUniqueId(document.get("uuid", UUID.class));
        return punishment;
    }

    @Override
    public @NotNull Document toBson(@NotNull Punishment punishment) {
        return new Document()
                .append("uniqueId", punishment.getUniqueId())
                .append("targetId", punishment.getTargetId())
                .append("staffId", punishment.getStaffId())
                .append("reason", punishment.getReason().getId())
                .append("startTime", punishment.getStartTime())
                .append("endTime", punishment.getEndTime())
                .append("type", punishment.getType().toString());
    }

    @Override
    public Punishment getEmpty(UUID uuid) {
        return new Punishment();
    }
}
