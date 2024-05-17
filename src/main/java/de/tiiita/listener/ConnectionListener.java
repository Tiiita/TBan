package de.tiiita.listener;

import de.tiiita.punish.PunishmentType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import de.tiiita.punish.PunishManager;

/**
 * Created on Januar 29, 2023 | 14:16:44
 * (●'◡'●)
 */
public class ConnectionListener implements Listener {
    private final PunishManager punishManager;

    public ConnectionListener(PunishManager punishManager) {
        this.punishManager = punishManager;
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        punishManager.getActivePunish(player.getUniqueId(), PunishmentType.BAN).whenComplete((punishmentDocument, throwable) -> {
            if (punishmentDocument == null) return;
            player.disconnect(new TextComponent(punishManager.getPunishScreen(punishmentDocument)));
        });
    }
}
