package de.tiiita.punish.reason;

public class PunishmentReason {

    private final int id;
    private String name;

    public PunishmentReason(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PunishmentReason fromId(int id) {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
