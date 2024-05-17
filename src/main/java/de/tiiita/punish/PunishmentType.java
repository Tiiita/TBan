package de.tiiita.punish;

import com.mongodb.lang.Nullable;

public enum PunishmentType {

    BAN("Ban"),
    MUTE("Mute");

    private final String name;
    PunishmentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public PunishmentType fromName(String name) {
        for (PunishmentType value : values()) {
            if (value.name.equalsIgnoreCase(name)) return value;
        }

        return null;
    }

    public boolean exists(String name) {
        return fromName(name) != null;
    }
}
