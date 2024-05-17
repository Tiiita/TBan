package de.tiiita.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import de.tiiita.punish.PunishManager;

import java.util.UUID;

/**
 * Created on Januar 29, 2023 | 14:16:44
 * (●'◡'●)
 */
public class ConnectionListener implements Listener {
    private final PunishManager banManager;

    public ConnectionListener(PunishManager banManager) {
        this.banManager = banManager;
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (banManager.isPunished(uuid)) {
            //Check if ban
        }
    }
}
