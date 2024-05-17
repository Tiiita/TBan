package de.tiiita.util.mongodb.impl.player;

import de.tiiita.punish.Punishment;
import de.tiiita.util.mongodb.UUIDIndexedDocument;

import java.util.List;
import java.util.UUID;

/**
 * Created on Januar 29, 2023 | 15:01:02
 * (●'◡'●)
 */
public class PlayerDocument implements UUIDIndexedDocument {
    private final UUID uniqueId;
    private boolean online;
    private String ip;
    private String country;
    private List<UUID> punishmentIds;
    private int logins;
    private int reportsSend;
    private int activeReports;

    public PlayerDocument(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPunishmentIds(List<UUID> punishmentIds) {
        this.punishmentIds = punishmentIds;
    }

    public void setLogins(int logins) {
        this.logins = logins;
    }

    public void setReportsSend(int reportsSend) {
        this.reportsSend = reportsSend;
    }

    public void setActiveReports(int activeReports) {
        this.activeReports = activeReports;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public boolean isOnline() {
        return online;
    }

    public int getLogins() {
        return logins;
    }

    public String getIp() {
        return ip;
    }

    public List<UUID> getPunishmentIds() {
        return punishmentIds;
    }

    public int getReportsSend() {
        return reportsSend;
    }

    public int getActiveReports() {
        return activeReports;
    }

    public String getCountry() {
        return country;
    }
    
    
}
