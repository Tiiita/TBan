package de.tiiita.listener;

import de.tiiita.punish.PunishEvent;
import de.tiiita.punish.Punishment;
import de.tiiita.punish.PunishmentType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import de.tiiita.punish.PunishService;
import net.md_5.bungee.event.EventPriority;

import java.util.concurrent.CompletableFuture;

/**
 * Created on Januar 29, 2023 | 14:16:44
 * (●'◡'●)
 */
public class PunishListener implements Listener {
    private final PunishService punishService;

    public PunishListener(PunishService punishService) {
        this.punishService = punishService;
    }

    @EventHandler
    public void onPunish(PunishEvent event) {
        Punishment punishment = event.getPunishment();
        if (isBan(punishment.getType())) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(punishment.getTargetId());
            if (player == null) return;

            CompletableFuture.runAsync(() -> {
                player.disconnect(new TextComponent(punishService.getPunishScreen(punishment)));
            });
        }
    }

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        CompletableFuture.runAsync(() -> {
            Punishment punishmentDocument = punishService.getActivePunish(player.getUniqueId(), PunishmentType.BAN);
            if (punishmentDocument == null) return;

            player.disconnect(new TextComponent(punishService.getPunishScreen(punishmentDocument)));
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(ChatEvent event) {
        if (!event.getSender().isConnected()) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        CompletableFuture.runAsync(() -> {
            Punishment punishmentDocument = punishService.getActivePunish(player.getUniqueId(), PunishmentType.MUTE);
            if (punishmentDocument == null) return;

            player.sendMessage(new TextComponent(punishService.getPunishScreen(punishmentDocument)));
        });
    }


    private boolean isBan(PunishmentType type) {
        return type == PunishmentType.BAN || type == PunishmentType.IPBAN;
    }

    private boolean isMute(PunishmentType type) {
        return type == PunishmentType.MUTE;
    }
}
