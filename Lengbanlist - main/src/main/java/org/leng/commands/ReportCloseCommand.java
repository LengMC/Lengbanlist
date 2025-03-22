package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.manager.ReportManager;
import org.leng.object.ReportEntry;
import org.leng.utils.Utils;

public class ReportCloseCommand implements CommandExecutor {
    private final Lengbanlist plugin;

    public ReportCloseCommand(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Utils.sendMessage(sender, plugin.prefix() + "§c此命令只能由玩家执行。");
            return true;
        }

        if (args.length < 1) {
            Utils.sendMessage(sender, plugin.prefix() + "§c用法错误: /report close <举报编号>");
            return true;
        }

        ReportEntry report = plugin.getReportManager().getReport(args[0]);
        if (report == null) {
            Utils.sendMessage(sender, plugin.prefix() + "§c未找到举报编号: " + args[0]);
            return true;
        }

        plugin.getReportManager().removeReport(args[0]);
        Utils.sendMessage(sender, plugin.prefix() + "§a你已关闭举报: " + report.getId());
        return true;
    }
}