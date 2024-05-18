package de.tiiita.command;

import de.tiiita.punish.PunishService;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created on Januar 29, 2023 | 14:28:47
 * (●'◡'●)
 */
public class PunishCommand extends Command {

    private final PunishService banManager;

    public PunishCommand(PunishService banManager) {
        super("punish", "tban.punish");
        this.banManager = banManager;
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

    }
}
