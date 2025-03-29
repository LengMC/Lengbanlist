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
            if (!sender.isOp() && !player.hasPermission("lengban.warn")) {
                Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
                return false;
            }
        }

        if (args.length < 2) {
            Utils.sendMessage(sender, plugin.prefix() + "§c用法错误: /lban warn <玩家名> <原因>");
            return false;
        }

        String target = args[0];
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Model currentModel = plugin.getModelManager().getCurrentModel();
        WarnManager warnManager = plugin.getWarnManager();

        List<WarnEntry> warnings = warnManager.getActiveWarnings(target);
        if (warnings.size() >= 3) {
            Utils.sendMessage(sender, plugin.prefix() + "§c玩家 " + target + " 已被警告 3 次，无法继续警告。");
            return false;
        }

        warnManager.warnPlayer(target, sender.getName(), reason);
        Utils.sendMessage(sender, currentModel.addWarn(target, reason));

        if (warnings.size() + 1 >= 3) {
            plugin.getBanManager().banPlayer(new org.leng.object.BanEntry(
                target, 
                "System", 
                Long.MAX_VALUE, 
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