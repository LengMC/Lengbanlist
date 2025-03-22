package org.leng.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.leng.Lengbanlist;
import org.leng.manager.ReportManager;
import org.leng.object.ReportEntry;
import org.leng.utils.Utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ReportCommand implements CommandExecutor {
    private final Lengbanlist plugin;

    public ReportCommand(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查权限
        if (!sender.hasPermission("lengbanlist.report")) {
            Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
            return true;
        }

        // 检查参数
        if (args.length < 2) {
            Utils.sendMessage(sender, plugin.prefix() + "§c用法错误: /report <玩家ID> <举报原因>");
            return true;
        }

        // 获取被举报玩家
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            Utils.sendMessage(sender, plugin.prefix() + "§c未找到在线玩家: " + args[0]);
            return true;
        }

        // 获取举报原因
        String reason = String.join(" ", args).substring(args[0].length() + 1);

        // 生成随机编号
        String reportId = generateRandomId();

        // 创建举报记录
        ReportEntry reportEntry = new ReportEntry(targetPlayer.getName(), sender.getName(), reason, reportId);
        plugin.getReportManager().addReport(reportEntry);

        // 发送消息给所有在线的 OP
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                sendReportMessage(player, reportEntry);
            }
        }

        // 7天后自动删除举报记录
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getReportManager().removeReport(reportId);
            }
        }.runTaskLater(plugin, 7L * 24 * 60 * 60 * 20);

        Utils.sendMessage(sender, plugin.prefix() + "§a举报成功！编号：" + reportId);
        return true;
    }

    private String generateRandomId() {
        return "abcde" + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private void sendReportMessage(Player op, ReportEntry report) {
        String message = "§7————————————————\n" +
                "§bLengbanlist Report\n" +
                "§e被举报人：§c" + report.getTarget() + "§e，举报人:§c" + report.getReporter() + "§e，举报原因:§c" + report.getReason() + "§e，该玩家已被举报次数：§c" + plugin.getReportManager().getReportCount(report.getTarget()) + "§e，编号：§c" + report.getId() + "\n" +
                "§7————————————————\n" +
                "§a【点击受理】§b【点击结束】§c【点击封禁】";

        op.spigot().sendMessage(
                Utils.clickableText("§a【点击受理】", "/report accept " + report.getId()),
                Utils.clickableText("§b【点击结束】", "/report close " + report.getId()),
                Utils.clickableText("§c【点击封禁】", "/ban " + report.getTarget())
        );
    }
}