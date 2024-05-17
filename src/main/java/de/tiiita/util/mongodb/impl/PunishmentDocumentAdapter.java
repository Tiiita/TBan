package de.tiiita.util.mongodb.impl;

import de.tiiita.punish.reason.PunishReason;
import de.tiiita.util.mongodb.DocumentAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PunishmentDocumentAdapter implements DocumentAdapter<PunishmentDocument> {
    @Override
    public PunishmentDocument fromBson(@NotNull Document document) {
        final PunishmentDocument punishmentDocument = new PunishmentDocument(document.get("uuid", UUID.class));
        punishmentDocument.setPlayer(document.get("player", UUID.class));
        punishmentDocument.setStaff(document.get("staff", UUID.class));
        punishmentDocument.setReason(PunishReason.fromId(document.getInteger("reason")));
        punishmentDocument.setStartTime(OffsetDateTime.parse(document.getString("startTime")));
        punishmentDocument.setEndTime(OffsetDateTime.parse(document.getString("endTime")));

        return punishmentDocument;
    }

    @Override
    public @NotNull Document toBson(@NotNull PunishmentDocument punishmentDocument) {
        return new Document()
                .append("uuid", punishmentDocument.getUniqueId())
                .append("player", punishmentDocument.getPlayer())
                .append("staff", punishmentDocument.getStaff())
                .append("reason", punishmentDocument.getReason().getId())
                .append("startTime", punishmentDocument.getStartTime())
                .append("endTime", punishmentDocument.getEndTime());
    }

    @Override
    public PunishmentDocument getEmpty(UUID uuid) {
        return new PunishmentDocument(uuid);
    }
}
