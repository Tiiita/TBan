package de.tiiita.command;

import de.tiiita.minecraft.bungee.BungeeConfig;
import de.tiiita.minecraft.bungee.Command;
import de.tiiita.minecraft.util.MojangProfileFetcher;
import de.tiiita.punish.PunishService;
import de.tiiita.punish.Punishment;
import de.tiiita.punish.PunishmentFactory;
import de.tiiita.util.DateTimeDifferenceFormatter;
import de.tiiita.util.reason.Reason;
import de.tiiita.util.reason.ReasonReader;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Januar 29, 2023 | 14:28:47
 * (●'◡'●)
 */
public class PunishCommand extends Command {

    private final PunishService punishService;
    private final BungeeConfig config;

    public PunishCommand(PunishService punishService, BungeeConfig config) {
        super("punish", "§cUse: /punish <player> <id>", "§cAccess denied", "tban.punish");
        this.punishService = punishService;
        this.config = config;
    }

    @Override
    public void onExecute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            sendMessage(commandSender, "§8» §cTBan - Reasons");
            sendMessage(commandSender, " ");

            for (Reason reason : new ReasonReader(config).getReasons()) {
                String message = "§8- §c" + reason.getId() + " §8| §7" + reason.getName() + "§8» " + reason.getType().toString();
                sendMessage(commandSender, message);
            }
            return;
        }

        if (args.length == 2) {
            handlePlayerSearch(args[0]).whenCompleteAsync((uuidString, throwable) -> {
                if (uuidString == null) {
                    sendMessage(commandSender, "§cNo player found");
                    return;
                }

                Punishment punishment = PunishmentFactory.create(commandSender, UUID.fromString(uuidString), args[1],
                        config, getUsageMessage(), punishService);

                punishService.punish(punishment);
                sendMessage(commandSender, "§cTBan §8» §7You have punished §a" + args[0] + " §7for §e"
                        + DateTimeDifferenceFormatter.formatDateTimeDifference(punishment.getStartTime(), punishment.getEndTime())
                        + "§8(§7" + punishment.getType().toString() + "§8)");
            });
            return;
        }

        sendUsage(commandSender);
    }

    private CompletableFuture<String> handlePlayerSearch(String name) {
        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(name);
        if (targetPlayer != null)
            return CompletableFuture.completedFuture(targetPlayer.getUniqueId().toString());

        return CompletableFuture.supplyAsync(() -> MojangProfileFetcher.fetchDataOrNull(name));
    }
}
