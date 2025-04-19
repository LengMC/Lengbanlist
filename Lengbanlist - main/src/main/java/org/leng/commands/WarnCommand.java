package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.manager.WarnManager;
import org.leng.models.Model;
import org.leng.utils.Utils;
import org.leng.object.WarnEntry;

import java.util.List;
import java.util.Arrays;

public class WarnCommand extends Command implements CommandExecutor {
    private final Lengbanlist plugin;

    public WarnCommand(Lengbanlist plugin) {
        super("warn");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.isOp() && !player.hasPermission("lengbanlist.warn")) {
                Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                return false;
            }
        }

        if (args.length < 2) {
            Utils.sendMessage(sender, plugin.prefix() + "§c用法错误: /lban warn <玩家名/IP> <原因>");
            return false;
        }

        // 检查是否是 IP
        if (args[0].contains(".")) {
            if (!plugin.getBanManager().isValidIp(args[0])) {
                Utils.sendMessage(sender, plugin.prefix() + "§c无效的IP地址");
                return false;
            }
            // IP警告逻辑
            plugin.getWarnManager().warnIp(args[0], sender.getName(), 
                String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
            Utils.sendMessage(sender, plugin.prefix() + "§a已警告IP: " + args[0]);
            return true;
        }

        // 玩家警告逻辑
        String target = args[0];
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Model currentModel = plugin.getModelManager().getCurrentModel();
        WarnManager warnManager = plugin.getWarnManager();

        List<WarnEntry> warnings = warnManager.getActiveWarnings(target);
        if (warnings.size() >= 3) {
            Utils.sendMessage(sender, plugin.prefix() + "§c玩家 " + target + " 已被警告 3 次，无法继续警告，请unwarn该玩家后再给予警告！");
            return false;
        }

        warnManager.warnPlayer(target, sender.getName(), reason);
        Utils.sendMessage(sender, currentModel.addWarn(target, reason));

        if (warnings.size() + 1 >= 3) {
            // 使用WarnManager的计算方法而不是硬编码永久封禁
            long banDuration = plugin.getWarnManager().calculateBanDuration(1); // 第一次触发
            plugin.getBanManager().banPlayer(new org.leng.object.BanEntry(
                target, 
                "System", 
                System.currentTimeMillis() + banDuration, 
                "警告次数过多", 
                true));
            Utils.sendMessage(sender, plugin.prefix() + "§c警告次数过多，已自动封禁玩家 " + target + "。");
        }

        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, label, args);
    }
}