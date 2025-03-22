package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.manager.WarnManager;
import org.leng.models.Model;
import org.leng.utils.Utils;

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
        if (args.length < 2) {
            Utils.sendMessage(sender, plugin.prefix() + "§c用法错误: /lban unwarn <玩家名>");
            return false;
        }

        String target = args[1];
        WarnManager warnManager = plugin.getWarnManager();
        Model currentModel = plugin.getModelManager().getCurrentModel();

        // 检查玩家是否有警告记录
        if (!warnManager.isPlayerWarned(target)) {
            Utils.sendMessage(sender, plugin.prefix() + "§c玩家 " + target + " 没有警告记录。");
            return false;
        }

        // 移除警告
        warnManager.unwarnPlayer(target);
        Utils.sendMessage(sender, currentModel.removeWarn(target));

        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, label, args);
    }
}