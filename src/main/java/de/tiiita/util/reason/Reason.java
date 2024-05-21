package de.tiiita.util.reason;

import de.tiiita.minecraft.bungee.BungeeConfig;
import de.tiiita.punish.PunishmentType;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;

public class Reason {
    private final int id;
    private final List<Duration> durations;
    private String name;
    private final PunishmentType type;

    protected Reason(int id, List<Duration> durations, String name, PunishmentType type) {
        this.id = id;
        this.durations = durations;
        this.name = name;
        this.type = type;
    }

    @Nullable
    public static Reason fromId(int id, BungeeConfig config) {
        return new ReasonReader(config).createReason(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Duration> getDurations() {
        return durations;
    }

    public PunishmentType getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
