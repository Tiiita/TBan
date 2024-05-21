package de.tiiita.report;

import de.tiiita.util.reason.Reason;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Report {
    protected UUID uniqueId;
    protected UUID reporterId;
    protected Reason reason;
    protected OffsetDateTime creationTime;

    public Report(UUID uniqueId, UUID reporterId, Reason reason, OffsetDateTime creationTime) {
        this.uniqueId = uniqueId;
        this.reporterId = reporterId;
        this.reason = reason;
        this.creationTime = creationTime;
    }

    public Report() {}


    public UUID getUniqueId() {
        return uniqueId;
    }

    public UUID getReporterId() {
        return reporterId;
    }

    public Reason getReason() {
        return reason;
    }

    public OffsetDateTime getCreationTime() {
        return creationTime;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setReporterId(UUID reporterId) {
        this.reporterId = reporterId;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public void setCreationTime(OffsetDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
