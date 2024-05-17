package de.tiiita.util.mongodb.impl.punishments;

import de.tiiita.punish.reason.PunishReason;
import de.tiiita.util.mongodb.DocumentAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PunishmentDocumentAdapter implements DocumentAdapter<PunishmentDocument> {
    @Override
    public PunishmentDocument fromBson(@NotNull Document document) {
        final PunishmentDocument punishmentDocument = new PunishmentDocument(document.get("uniqueId", UUID.class));
        punishmentDocument.setTargetId(document.get("targetId", UUID.class));
        punishmentDocument.setStaffId(document.get("staffId", UUID.class));
        punishmentDocument.setReason(PunishReason.fromId(document.getInteger("reason")));
        punishmentDocument.setStartTime(OffsetDateTime.parse(document.getString("startTime")));
        punishmentDocument.setEndTime(OffsetDateTime.parse(document.getString("endTime")));

        return punishmentDocument;
    }

    @Override
    public @NotNull Document toBson(@NotNull PunishmentDocument document) {
        return new Document()
                .append("uniqueId", document.getUniqueId())
                .append("targetId", document.getTargetId())
                .append("staffId", document.getStaffId())
                .append("reason", document.getReason().getId())
                .append("startTime", document.getStartTime())
                .append("endTime", document.getEndTime());
    }

    @Override
    public PunishmentDocument getEmpty(UUID uuid) {
        return new PunishmentDocument(uuid);
    }
}
