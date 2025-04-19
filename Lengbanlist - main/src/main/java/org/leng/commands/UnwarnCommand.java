package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.manager.WarnManager;
import org.leng.object.WarnEntry;
import org.leng.utils.Utils;

import java.util.List;

public class UnwarnCommand extends Command implements CommandExecutor {
    private final Lengbanlist plugin;

    public UnwarnCommand(Lengbanlist plugin) {
        super("unwarn");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        // 检查权限
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!sender.isOp() && !player.hasPermission("lengbanlist.unwarn")) {
                Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                return false;
            }
        }

        // 检查参数长度
        if (args.length < 1) {
            Utils.sendMessage(sender, plugin.prefix() + "§c用法错误: /lban unwarn <玩家名/IP> [警告ID]");
            return false;
        }

        String target = args[0];
        WarnManager warnManager = plugin.getWarnManager();

        // 检查是否是 IP
        boolean isIp = target.contains(".");

        // 检查目标是否有警告记录
        List<WarnEntry> activeWarnings;
        if (isIp) {
            activeWarnings = warnManager.getActiveWarnings(target);
            if (activeWarnings.isEmpty()) {
                Utils.sendMessage(sender, plugin.prefix() + "§cIP " + target + " 没有警告记录。");
                return false;
            }
        } else {
            activeWarnings = warnManager.getActiveWarnings(target);
            if (activeWarnings.isEmpty()) {
                Utils.sendMessage(sender, plugin.prefix() + "§c玩家 " + target + " 没有警告记录。");
                return false;
            }
        }

        try {
            // 如果有警告ID，移除特定警告
            if (args.length > 1) {
                int warnId = parseWarnId(args[1], activeWarnings);
                if (warnId != -1 && warnManager.unwarnPlayer(target, warnId)) {
                    Utils.sendMessage(sender, plugin.prefix() + "§a警告 #" + warnId + " 已移除");
                } else {
                    Utils.sendMessage(sender, plugin.prefix() + "§c警告ID无效或已被移除");
                }
            } else {
                // 移除所有警告
                for (int i = 0; i < activeWarnings.size(); i++) {
                    warnManager.unwarnPlayer(target, i + 1); // 警告ID从1开始
                }
                if (isIp) {
                    Utils.sendMessage(sender, plugin.prefix() + "§a已移除IP " + target + " 的所有警告");
                } else {
                    Utils.sendMessage(sender, plugin.prefix() + "§a已移除玩家 " + target + " 的所有警告");
                }
            }
        } catch (Exception e) {
            Utils.sendMessage(sender, plugin.prefix() + "§c处理警告时出错: " + e.getMessage());
            return false;
        }

        return true;
    }

    private int parseWarnId(String input, List<WarnEntry> warnings) {
        try {
            // 尝试解析为数字ID
            int id = Integer.parseInt(input);
            if (id > 0 && id <= warnings.size()) {
                return id;
            }
        } catch (NumberFormatException e) {
            // 如果不是数字，尝试匹配UUID
            for (int i = 0; i < warnings.size(); i++) {
                if (warnings.get(i).getId().equalsIgnoreCase(input)) {
                    return i + 1; // 返回基于1的索引
                }
            }
        }
        return -1;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, label, args);
    }
}