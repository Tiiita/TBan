package de.tiiita.util;

import de.tiiita.punish.Punishment;

import java.util.List;

/**
 * Created on Januar 29, 2023 | 15:01:02
 * (●'◡'●)
 */
public class PlayerInfo {
    private final String name;
    private final String uuid;
    private final boolean online;
    private final String ip;
    private final String country;
    private final List<Punishment> punishments;
    private final int logins;
    private final int reportsSend;
    private final int activeReports;

    public PlayerInfo(String name, String uuid, boolean online, String ip, String country, List<Punishment> punishments, int logins, int reportsSend, int activeReports) {
        this.name = name;
        this.uuid = uuid;
        this.online = online;
        this.ip = ip;
        this.country = country;
        this.punishments = punishments;
        this.logins = logins;
        this.reportsSend = reportsSend;
        this.activeReports = activeReports;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
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

    public List<Punishment> getPunishments() {
        return punishments;
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
