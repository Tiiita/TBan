package de.tiiita.punish;

import com.mongodb.lang.Nullable;

public enum PunishmentType {

    BAN("Ban", 0),
    IPBAN("IPBan", 1),
    MUTE("Mute", 2);

    private final String name;
    private final int id;
    PunishmentType(String name, int id) {
        this.name = name;
        this.id = id;
    }


    @Nullable
    public static PunishmentType fromName(String name) {
        for (PunishmentType value : values()) {
            if (value.name.equalsIgnoreCase(name)) return value;
        }

        return null;
    }

    public static boolean exists(String name) {
        return fromName(name) != null;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
