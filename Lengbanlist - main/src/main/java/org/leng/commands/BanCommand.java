package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.object.BanEntry;
import org.leng.object.BanIpEntry;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;

public class BanCommand implements CommandExecutor {
    private final Lengbanlist plugin;

    public BanCommand(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.isOp() || !player.hasPermission("lengban.ban")) {
                Utils.sendMessage(sender, "§c你没有权限使用此命令。");
                return false;
            }
        }

        if (args.length < 3) {
            Utils.sendMessage(sender, "§c用法错误: /ban <玩家> <时间|auto> <原因>");
            Utils.sendMessage(sender, "§c时间单位: s(秒), m(分), h(时), d(天), w(周), M(月), y(年)");
            Utils.sendMessage(sender, "§c使用 auto 自动计算封禁时间（基于警告次数）");
            return false;
        }

        if (plugin.getBanManager().isPlayerBanned(args[0])) {
            Utils.sendMessage(sender, "§c玩家 " + args[0] + " 已经被封禁");
            return false;
        }

        long banTimestamp;
        boolean isAuto = false;

        if (args[1].equalsIgnoreCase("auto")) {
            isAuto = true;
            banTimestamp = calculateAutoBanTime(args[0]);
        } else {
            banTimestamp = TimeUtils.parseTime(args[1]);
            if (banTimestamp == -1) {
                showTimeFormatError(sender);
                return false;
            }
        }

        // 修复显示0天实际1天的问题
        if (banTimestamp < TimeUtils.daysToMillis(1) && banTimestamp > 0) {
            banTimestamp = TimeUtils.daysToMillis(1);
        }

        BanEntry entry = new BanEntry(
                args[0], 
                sender.getName(), 
                banTimestamp, 
                args[2],
                isAuto
        );
        
        plugin.getBanManager().banPlayer(entry);
        sendBanResult(sender, args[0], banTimestamp, isAuto, args[1]);
        return true;
    }

    private void showTimeFormatError(CommandSender sender) {
        Utils.sendMessage(sender, "§c时间格式错误，请使用以下格式:");
        Utils.sendMessage(sender, "§c - 10s: 秒 (10 秒)");
        Utils.sendMessage(sender, "§c - 5m: 分钟 (5 分钟)");
        Utils.sendMessage(sender, "§c - 2h: 小时 (2 小时)");
        Utils.sendMessage(sender, "§c - 7d: 天 (7 天)");
        Utils.sendMessage(sender, "§c - 1w: 周 (1 周，等于 7 天)");
        Utils.sendMessage(sender, "§c - 1M: 月 (1 月，按 30 天计算)");
        Utils.sendMessage(sender, "§c - 1y: 年 (1 年，按 365 天计算)");
        Utils.sendMessage(sender, "§c - forever: 永久封禁");
        Utils.sendMessage(sender, "§c - auto: 自动计算封禁时间");
    }

    private void sendBanResult(CommandSender sender, String player, long banTimestamp, boolean isAuto, String timeString) {
        String duration;
        if (banTimestamp == Long.MAX_VALUE) {
            duration = "永久";
        } else {
            long remainingDays = (banTimestamp - System.currentTimeMillis()) / TimeUtils.daysToMillis(1);
            // 确保至少显示1天
            remainingDays = Math.max(1, remainingDays);
            duration = remainingDays + "天";
        }
        
        String message = String.format("§l§a成功封禁 玩家: %s，时长: %s%s",
                player, 
                duration,
                isAuto ? " §6<auto>" : "");

        Utils.sendMessage(sender, message);
    }

    private long calculateAutoBanTime(String playerName) {
        int warnCount = Math.max(0, plugin.getWarnManager().getActiveWarnings(playerName).size());
        
        switch (warnCount) {
            case 0:  return TimeUtils.daysToMillis(1);  // 无警告记录也封1天
            case 1:  return TimeUtils.daysToMillis(3);
            case 2:  return TimeUtils.daysToMillis(7);
            case 3:  return TimeUtils.daysToMillis(14);
            case 4:  return TimeUtils.daysToMillis(30);
            default: return Long.MAX_VALUE; // 只有超过4次才永久
        }
    }
}