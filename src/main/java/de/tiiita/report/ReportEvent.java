package de.tiiita.report;

import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

/**
 * Created on Januar 29, 2023 | 14:19:46
 * (●'◡'●)
 */
public class ReportEvent extends Event {
    private final UUID reporterUUID;
    private final UUID targetUUID;
    private final int reasonID;

    public ReportEvent(UUID reporterUUID, UUID targetUUID, int reasonID) {
        this.reporterUUID = reporterUUID;
        this.targetUUID = targetUUID;
        this.reasonID = reasonID;
    }

    public UUID getReporterUUID() {
        return reporterUUID;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public int getReasonID() {
        return reasonID;
    }
}
