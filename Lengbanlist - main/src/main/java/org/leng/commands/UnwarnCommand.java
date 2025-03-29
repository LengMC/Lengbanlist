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
            if (!sender.isOp() && !player.hasPermission("lengban.unwarn")) {
                Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                return false;
            }
        }

        // 检查参数长度
        if (args.length < 1) {
            Utils.sendMessage(sender, plugin.prefix() + "§c用法错误: /lban unwarn <玩家名> [警告ID]");
            return false;
        }

        String target = args[0];
        WarnManager warnManager = plugin.getWarnManager();

        // 检查玩家是否有警告记录
        if (plugin.getWarnManager().getActiveWarnings(target).isEmpty()) {
            Utils.sendMessage(sender, plugin.prefix() + "§c玩家 " + target + " 没有警告记录。");
            return false;
        }

        // 如果有警告ID，移除特定警告
        if (args.length > 1) {
            try {
                int warnId = Integer.parseInt(args[1]);
                if (warnManager.unwarnPlayer(target, warnId)) {
                    Utils.sendMessage(sender, "警告已移除");
                } else {
                    Utils.sendMessage(sender, "警告ID无效");
                }
            } catch (NumberFormatException e) {
                Utils.sendMessage(sender, "警告ID必须是数字");
            }
        } else {
            // 移除所有警告
            plugin.getWarnManager().getActiveWarnings(target).forEach(warn -> plugin.getWarnManager().unwarnPlayer(target, Integer.parseInt(warn.getId())));
            Utils.sendMessage(sender, "所有警告已移除");
        }

        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, label, args);
    }
}