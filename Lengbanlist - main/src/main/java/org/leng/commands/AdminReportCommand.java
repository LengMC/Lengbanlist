package org.leng.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.manager.ReportManager;
import org.leng.object.ReportEntry;
import org.leng.utils.Utils;

import java.util.List;

public class AdminReportCommand implements CommandExecutor {
    private final Lengbanlist plugin;

    public AdminReportCommand(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lengbanlist.admin")) {
            Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
            return true;
        }

        if (!(sender instanceof Player)) {
            Utils.sendMessage(sender, plugin.prefix() + "§c此命令只能由玩家执行。");
            return true;
        }

        Player player = (Player) sender;
        showAdminReportUI(player);
        return true;
    }

    private void showAdminReportUI(Player player) {
        ReportManager reportManager = plugin.getReportManager();

        // 获取未关闭的举报数量
        int pendingReports = reportManager.getPendingReportCount();
        // 获取当前在线管理员数量
        int onlineAdmins = (int) Bukkit.getOnlinePlayers().stream().filter(p -> p.isOp()).count();

        // 构建举报管理界面消息
        String adminUI = "§7————————————————\n" +
                "§bLengbanlist Report Admin\n" +
                "§e当前待处理举报数：§c" + pendingReports + "\n" +
                "§e当前在线管理员：§c" + onlineAdmins + "\n" +
                "§7————————————————\n";

        player.sendMessage(adminUI);

        // 获取所有未关闭的举报
        List<ReportEntry> reports = reportManager.getPendingReports();
        if (reports.isEmpty()) {
            player.sendMessage("§a暂无待处理的举报！");
            return;
        }

        for (ReportEntry report : reports) {
            String status = report.getStatus() == null ? "" : "§a【当前状态：" + report.getStatus() + "】";
            String reportInfo = "§e被举报人：§c" + report.getTarget() + " §e举报人：§c" + report.getReporter() + " §e举报原因:§c" + report.getReason() + " §e举报编号：" + report.getId() + "\n" +
                    status +
                    "§a【点击受理】§b【点击关闭】§c【点击封禁】\n" +
                    "§7————————————————\n";

            // 发送可点击的举报信息
            sendClickableReportMessage(player, reportInfo, report);
        }
    }

    private void sendClickableReportMessage(Player player, String message, ReportEntry report) {
        player.spigot().sendMessage(
                Utils.clickableText("§a【点击受理】", "/report accept " + report.getId()),
                Utils.clickableText("§b【点击关闭】", "/report close " + report.getId()),
                Utils.clickableText("§c【点击封禁】", "/ban " + report.getTarget())
        );
    }
}