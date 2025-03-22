package org.leng.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.leng.Lengbanlist;
import org.leng.manager.BanManager;
import org.leng.manager.MuteManager;
import org.leng.utils.SaveIP;

public class PlayerJoinListener implements Listener {
    private final Lengbanlist plugin;

    public PlayerJoinListener(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SaveIP.saveIP(player); // 保存玩家的 IP
        if (plugin.getBanManager().isPlayerBanned(player.getName())) {
            plugin.getBanManager().checkBanOnJoin(player); // 检查玩家是否被封禁
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.getMuteManager().isPlayerMuted(player.getName())) {
            event.setCancelled(true); // 取消发言事件
            player.sendMessage(plugin.prefix() + "§c你不准说话喵！"); // 发送提示
        }
    }
}