package de.tiiita.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created on Januar 29, 2023 | 14:28:24
 * (●'◡'●)
 */
public class PlayerInfoCommand extends Command {

    public PlayerInfoCommand() {
        super("playerinfo", "proxy.command.playerinfo", "pinfo", "pi");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }
}
