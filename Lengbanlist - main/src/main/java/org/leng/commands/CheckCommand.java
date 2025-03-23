package org.leng.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import org.leng.Lengbanlist;
import org.leng.manager.BanManager;
import org.leng.manager.MuteManager;
import org.leng.manager.WarnManager;
import org.leng.utils.SaveIP;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;
import org.leng.object.WarnEntry;

import java.util.ArrayList;
import java.util.List;

public class CheckCommand extends Command implements CommandExecutor {
    private final Lengbanlist plugin;

    public CheckCommand(Lengbanlist plugin) {
        super("check");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("lengbanlist.check")) {
            Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
            return true;
        }

        if (args.length < 1) {
            Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式：/check <玩家名/IP>");
            return true;
        }

        String target = args[0];
        if (target.contains(".")) {
            // 查询 IP 信息
            checkIpInfo(sender, target);
        } else {
            // 查询玩家信息
            checkPlayerInfo(sender, target);
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, label, args);
    }

    private void checkPlayerInfo(CommandSender sender, String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if (!player.hasPlayedBefore() && !player.isOnline()) {
            Utils.sendMessage(sender, plugin.prefix() + "§c未找到玩家：" + playerName);
            return;
        }

        String uuid = player.getUniqueId().toString();
        long lastLogin = player.getLastPlayed();
        String lastLoginTime = lastLogin == 0 ? "从未登录" : TimeUtils.timestampToReadable(lastLogin);
        boolean isMuted = plugin.getMuteManager().isPlayerMuted(playerName);
        boolean isBanned = plugin.getBanManager().isPlayerBanned(playerName);
        boolean isOp = player.isOp();
        List<WarnEntry> warnings = plugin.getWarnManager().getPlayerWarnings(playerName);

        Utils.sendMessage(sender, plugin.prefix() + "§a玩家信息：");
        Utils.sendMessage(sender, plugin.prefix() + "§b玩家名: " + playerName);
        Utils.sendMessage(sender, plugin.prefix() + "§bUUID: " + uuid);
        Utils.sendMessage(sender, plugin.prefix() + "§b最后登录时间: " + lastLoginTime);
        Utils.sendMessage(sender, plugin.prefix() + "§b是否禁言: " + (isMuted ? "是" : "否"));
        Utils.sendMessage(sender, plugin.prefix() + "§b是否封禁: " + (isBanned ? "是" : "否"));
        Utils.sendMessage(sender, plugin.prefix() + "§b是否是OP: " + (isOp ? "是" : "否"));

        if (warnings.isEmpty()) {
            Utils.sendMessage(sender, plugin.prefix() + "§b警告: 无警告");
        } else {
            Utils.sendMessage(sender, plugin.prefix() + "§b警告记录:");
            for (WarnEntry warn : warnings) {
                Utils.sendMessage(sender, plugin.prefix() + "§7- 时间: " + TimeUtils.timestampToReadable(warn.getTime()) +
                        " §7判断者: " + warn.getStaff() +
                        " §7原因: " + warn.getReason());
            }
        }
    }

    private void checkIpInfo(CommandSender sender, String ip) {
        boolean isBanned = plugin.getBanManager().isIpBanned(ip);
        List<String> associatedPlayers = getPlayersAssociatedWithIp(ip);

        Utils.sendMessage(sender, plugin.prefix() + "§aIP信息：");
        Utils.sendMessage(sender, plugin.prefix() + "§bIP: " + ip);
        Utils.sendMessage(sender, plugin.prefix() + "§b是否封禁: " + (isBanned ? "是" : "否"));
        Utils.sendMessage(sender, plugin.prefix() + "§b关联玩家: " + (associatedPlayers.isEmpty() ? "无" : String.join(", ", associatedPlayers)));
    }

    private List<String> getPlayersAssociatedWithIp(String ip) {
        List<String> players = new ArrayList<>();
        List<String> ipList = plugin.getIpFC().getStringList("ip");
        for (String entry : ipList) {
            String[] parts = entry.split(":");
            if (parts.length == 2 && parts[1].equals(ip)) {
                players.add(parts[0]);
            }
        }
        return players;
    }
}