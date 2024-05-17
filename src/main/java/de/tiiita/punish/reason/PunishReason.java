package de.tiiita.punish.reason;

public class PunishReason {

    private final int id;
    private String name;

    public PunishReason(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PunishReason fromId(int id) {
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
