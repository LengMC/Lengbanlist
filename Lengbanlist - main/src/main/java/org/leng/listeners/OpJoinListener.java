package org.leng.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.leng.Lengbanlist;
import org.leng.utils.GitHubUpdateChecker;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class OpJoinListener implements Listener {
    private final Lengbanlist plugin;

    // 修改构造函数，接受 Lengbanlist 参数
    public OpJoinListener(Lengbanlist plugin) {
        this.plugin = plugin;
    }

@EventHandler
public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (player.isOp()) {
        try {
            String pluginName = plugin.getDescription().getName();
            String pluginVersion = plugin.getDescription().getVersion();
            // 更新为当前仓库的地址
            String updateUrl = "https://github.com/LengMC/Lengbanlist/releases/latest";
            String latestVersion = GitHubUpdateChecker.getLatestReleaseVersion();

            // 检查是否有更新
            if (GitHubUpdateChecker.isUpdateAvailable(pluginVersion)) {
                String prefix = plugin.prefix(); // 使用 plugin.prefix() 获取前缀
                TextComponent message = new TextComponent(prefix + " §a喵喵发现有新版本可用，当前版本：§e" + pluginVersion + "§a，最新版本：§e" + latestVersion + "§a 请前往: §b" + updateUrl);
                TextComponent clickableLink = new TextComponent("§f【§b点击前往喵~§f】");
                clickableLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, updateUrl));
                clickableLink.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a点击打开更新页面喵~").create()));

                // 发送消息给 OP
                player.spigot().sendMessage(message, clickableLink);
            } else {
                player.sendMessage(plugin.prefix() + " §a喵喵发现现在是最新版本！"); // 使用 plugin.prefix() 获取前缀
            }
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(plugin.prefix() + "§c无法获取最新版本信息，请检查网络连接！"); // 使用 plugin.prefix() 获取前缀
        }
    }
}
}