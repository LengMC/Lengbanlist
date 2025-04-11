package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;

public class BanIpCommand extends Command implements CommandExecutor {
    private final Lengbanlist plugin;

    public BanIpCommand(Lengbanlist plugin) {
        super("ban-ip");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        // 检查权限
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.isOp() || !(player.hasPermission("lengbanlist.banip"))) {
                Utils.sendMessage(sender, "§c你没有权限使用此命令。");
                return false;
            }
        }

        // 检查参数长度
        if (args.length < 3) {
            Utils.sendMessage(sender, "§c用法错误: /ban-ip <IP> <时间> <原因>");
            Utils.sendMessage(sender, "§c默认时间单位为天（若以天为单位请勿添加单位）");
            return false;
        }

        // 检查 IP 是否已经被封禁
        if (Lengbanlist.getInstance().banManager.isIpBanned(args[0])) {
            Utils.sendMessage(sender, "§cIP " + args[0] + " 已经被封禁");
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
        Lengbanlist.getInstance().banManager.banIp(
                new org.leng.object.BanIpEntry(args[0], sender.getName(), banTimestamp, args[2])
        );
        if (banTimestamp == Long.MAX_VALUE) {
            Utils.sendMessage(sender, "§l§a成功永久封禁 IP: " + args[0]);
        } else {
            Utils.sendMessage(sender, "§l§a成功封禁 IP: " + args[0] + "，时长: " + args[1]);
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, label, args);
    }
}