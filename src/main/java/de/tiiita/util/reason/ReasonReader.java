package de.tiiita.util.reason;

import de.tiiita.minecraft.bungee.BungeeConfig;
import de.tiiita.punish.PunishmentType;
import de.tiiita.util.DateTimeUtils;
import net.md_5.bungee.config.Configuration;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReasonReader {

    private final BungeeConfig config;

    public ReasonReader(BungeeConfig config) {
        this.config = config;
    }


    public List<Reason> getReasons() {
        List<Reason> reasons = new ArrayList<>();
        for (String reasonId : config.getSection("reason").getKeys()) {
            reasons.add(createReason(Integer.parseInt(reasonId)));
        }

        return reasons;
    }


    @Nullable
    public Reason createReason(int id) {
        Configuration section = config.getSection("" + id);
        PunishmentType type = PunishmentType.fromName(section.getString("type"));
        String displayName = section.getString("display-name");
        List<String> durationStrings = (section.getStringList("durations"));
        List<Duration> durations = new ArrayList<>();

        for (String durationString : durationStrings) {
            durations.add(DateTimeUtils.parseDuration(durationString));
        }
       return new Reason(id, durations, displayName, type);
    }



}
