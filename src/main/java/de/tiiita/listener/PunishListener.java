package de.tiiita.listener;

import de.tiiita.punish.PunishmentType;
import de.tiiita.util.mongodb.impl.punishments.PunishmentDocument;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import de.tiiita.punish.PunishManager;
import net.md_5.bungee.event.EventPriority;

import java.util.concurrent.CompletableFuture;

/**
 * Created on Januar 29, 2023 | 14:16:44
 * (●'◡'●)
 */
public class PunishListener implements Listener {
    private final PunishManager punishManager;

    public PunishListener(PunishManager punishManager) {
        this.punishManager = punishManager;
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        CompletableFuture.runAsync(() -> {
            PunishmentDocument punishmentDocument = punishManager.getActivePunish(player.getUniqueId(), PunishmentType.BAN).join();
            if (punishmentDocument == null) return;

            player.disconnect(new TextComponent(punishManager.getPunishScreen(punishmentDocument, PunishmentType.BAN).join()));
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(ChatEvent event) {
        if (!event.getSender().isConnected()) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        CompletableFuture.runAsync(() -> {
            PunishmentDocument punishmentDocument = punishManager.getActivePunish(player.getUniqueId(), PunishmentType.MUTE).join();
            if (punishmentDocument == null) return;

            player.sendMessage(new TextComponent(punishManager.getPunishScreen(punishmentDocument, PunishmentType.MUTE).join()));
        });
    }
}
