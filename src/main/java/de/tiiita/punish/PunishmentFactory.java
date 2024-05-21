package de.tiiita.punish;

import de.tiiita.minecraft.bungee.BungeeConfig;
import de.tiiita.util.reason.Reason;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PunishmentFactory {

    public static Punishment create(CommandSender sender, UUID targetId, String reasonId, BungeeConfig config, String usageMessage, PunishService punishService) {
        if (!validateReason(sender, reasonId, usageMessage, config)) return null;
        Reason reason = Reason.fromId(Integer.parseInt(reasonId), config);
        OffsetDateTime startTime = OffsetDateTime.now();

        return new Punishment(
                UUID.randomUUID(),
                targetId,
                getStaffId(sender),
                reason,
                startTime,
                punishService.getPunishEnd(targetId, reason, startTime),
                reason.getType());
    }

    private static UUID getStaffId(CommandSender commandSender) {
        return (commandSender instanceof ProxiedPlayer)
                ? ((ProxiedPlayer) commandSender).getUniqueId()
                : UUID.fromString("00000000-0000-0000-0000-000000000000");

    }

    private static boolean validateReason(CommandSender sender, String reasonArg, String usageMessage, BungeeConfig config) {
        try {
            int reasonId = Integer.parseInt(reasonArg);
            Reason reason = Reason.fromId(reasonId, config);

            if (reason == null) {
                sender.sendMessage(new TextComponent("§cNo reason with given id found."));
                return false;
            } else return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(new TextComponent(usageMessage));
            return false;
        }
    }
}
