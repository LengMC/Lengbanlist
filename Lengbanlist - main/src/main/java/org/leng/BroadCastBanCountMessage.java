package org.leng;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class BroadCastBanCountMessage extends BukkitRunnable {
    @Override
    public void run() {
        // 检查插件是否仍然启用
        if (!Lengbanlist.getInstance().isEnabled()) {
            this.cancel();
            return;
        }
        
        // 检查是否有在线玩家
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                // 获取默认消息
                String defaultMessage = Lengbanlist.getInstance().getBroadcastFC().getString("default-message");

                // 获取封禁玩家数量和封禁IP数量
                int banCount = Lengbanlist.getInstance().getBanManager().getBanList().size();
                int banIpCount = Lengbanlist.getInstance().getBanManager().getBanIpList().size();

                // 计算总封禁数量
                int totalBans = banCount + banIpCount;

                // 替换占位符
                String replacedMessage = defaultMessage
                        .replace("%s", String.valueOf(banCount)) // 替换封禁玩家数量
                        .replace("%i", String.valueOf(banIpCount)) // 替换封禁IP数量
                        .replace("%t", String.valueOf(totalBans)); // 替换总封禁数量

                // 创建主消息组件
                TextComponent mainMessage = new TextComponent(ChatColor.translateAlternateColorCodes('&', replacedMessage));
                mainMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§a绳§b之§c于§d法§e！").create())); // 彩色悬浮文本

                // 创建点击组件
                TextComponent clickableComponent = new TextComponent("§f【§b点§c击§d查§e看§f】"); // 彩色文本
                clickableComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lban list"));
                clickableComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§a看看封禁列表§bawa").create())); // 彩色悬浮文本

                // 发送消息
                player.spigot().sendMessage(mainMessage, clickableComponent);
            }
        } catch (Exception e) {
            Lengbanlist.getInstance().getLogger().warning("广播任务执行出错: " + e.getMessage());
        }
    }
}