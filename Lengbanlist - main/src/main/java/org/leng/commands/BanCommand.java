package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;

public class BanCommand implements CommandExecutor {
    private final Lengbanlist plugin;

    public BanCommand(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查权限
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.isOp() || !(player.hasPermission("lengban.ban"))) {
                Utils.sendMessage(sender, "§c你没有权限使用此命令。");
                return false;
            }
        }

        // 检查参数长度
        if (args.length < 3) {
            Utils.sendMessage(sender, "§c用法错误: /ban <玩家> <时间> <原因>");
            Utils.sendMessage(sender, "§c默认时间单位为天（若以天为单位请勿添加单位）");
            return false;
        }

        // 检查玩家是否已经被封禁
        if (plugin.getBanManager().isPlayerBanned(args[0])) {
            Utils.sendMessage(sender, "§c玩家 " + args[0] + " 已经被封禁");
            return false;
        }

        // 解析封禁时间
        long banTimestamp = TimeUtils.parseTime(args[1]);
        if (banTimestamp == -1) {
            Utils.sendMessage(sender, "§c时间格式错误，请使用以下格式:");
            Utils.sendMessage(sender, "§c - 10s: 秒 (10 秒)");
            Utils.sendMessage(sender, "§c - 5m: 分钟 (5 分钟)");
            Utils.sendMessage(sender, "§c - 2h: 小时 (2 小时)");
            Utils.sendMessage(sender, "§c - 7d: 天 (7 天)");
            Utils.sendMessage(sender, "§c - 1w: 周 (1 周，等于 7 天)");
            Utils.sendMessage(sender, "§c - 1M: 月 (1 月，按 30 天计算)");
            Utils.sendMessage(sender, "§c - 1y: 年 (1 年，按 365 天计算)");
            Utils.sendMessage(sender, "§c - forever: 永久封禁");
            return false;
        }

        // 执行封禁操作
        plugin.getBanManager().banPlayer(
                new org.leng.object.BanEntry(args[0], sender.getName(), banTimestamp, args[2])
        );
        if (banTimestamp == Long.MAX_VALUE) {
            Utils.sendMessage(sender, "§l§a成功永久封禁 玩家: " + args[0]);
        } else {
            Utils.sendMessage(sender, "§l§a成功封禁 玩家: " + args[0] + "，时长: " + args[1]);
        }
        return true;
    }
}