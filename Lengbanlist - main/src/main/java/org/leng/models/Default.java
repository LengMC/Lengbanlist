package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

public class Default implements Model {
    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§b╔══════════════════════════════════╗");
        Utils.sendMessage(sender, "§b║ §2§oLengbanlist 帮助信息 - 默认风格 §b║");
        Utils.sendMessage(sender, "§b╠══════════════════════════════════╣");
        Utils.sendMessage(sender, "§2✦ §b/lban list §7- §3查看被封禁的名单");
        Utils.sendMessage(sender, "§2✦ §b/lban a §7- §3广播当前封禁人数");
        Utils.sendMessage(sender, "§2✦ §b/lban toggle §7- §3开启/关闭自动广播");
        Utils.sendMessage(sender, "§2✦ §b/lban model <模型名称> §7- §3切换模型");
        Utils.sendMessage(sender, "§2✦ §b/lban reload §7- §3重新加载配置");
        Utils.sendMessage(sender, "§2✦ §b/lban add <玩家名> <天数> <原因> §7- §3添加封禁");
        Utils.sendMessage(sender, "§2✦ §b/lban remove <玩家名> §7- §3移除封禁");
        Utils.sendMessage(sender, "§2✦ §b/lban mute <玩家名> <原因> §7- §3禁言玩家");
        Utils.sendMessage(sender, "§2✦ §b/lban unmute <玩家名> §7- §3解除禁言");
        Utils.sendMessage(sender, "§2✦ §b/lban list-mute §7- §3查看禁言列表");
        Utils.sendMessage(sender, "§2✦ §b/lban help §7- §3显示帮助信息");
        Utils.sendMessage(sender, "§2✦ §b/lban open §7- §3打开可视化操作界面");
        Utils.sendMessage(sender, "§2✦ §b/lban getIP <玩家名> §7- §3查询玩家的 IP 地址");
        Utils.sendMessage(sender, "§2✦ §b/ban-ip <IP地址> <天数> <原因> §7- §3封禁 IP 地址");
        Utils.sendMessage(sender, "§2✦ §b/unban-ip <IP地址> §7- §3解除 IP 封禁");
        Utils.sendMessage(sender, "§2✦ §b/lban warn <玩家名> <原因> §7- §3警告玩家，三次警告将自动封禁！");
        Utils.sendMessage(sender, "§2✦ §b/lban unwarn <玩家名> §7- §3移除玩家的警告记录。");
        Utils.sendMessage(sender, "§2✦ §b/lban check <玩家名/IP> §7- §3检查玩家或IP的封禁状态");
        Utils.sendMessage(sender, "§2✦ §b/lban language §7- §3打开语言选择页面，选择适合的语言进行操作。");
        Utils.sendMessage(sender, "§2✦ §b/report <玩家名> <原因> §7- §3举报玩家，维护服务器秩序。");
        Utils.sendMessage(sender, "§7-> §2§l/report accept <举报编号> §7- §3受理举报，开始处理。");
        Utils.sendMessage(sender, "§7-> §2§l/report close <举报编号> §7- §3关闭举报，问题已解决。");
        Utils.sendMessage(sender, "§2✦ §b/kick <玩家名> <原因> §7- §3踢出不守规矩的玩家！");
        Utils.sendMessage(sender, "§2✦ §b/lban info §7- §3查看插件信息，了解当前运行状态");
        Utils.sendMessage(sender, "§b╚══════════════════════════════════╝");
        Utils.sendMessage(sender, "§2♡ 当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " §7| §b模型: 默认 Default");
    }

    @Override
    public String getKickMessage(String reason) {
        return "§b╔══════════════════════════╗\n" +
               "§b║   §d默认模型的驱逐通知  §b║\n" +
               "§b╠══════════════════════════╣\n" +
               "§d☠️ 你被默认模型踢出服务器啦！\n\n" +
               "§7原因: §f" + reason + "\n\n" +
               "§d下次请遵守规则哦~\n" +
               "§b╚══════════════════════════╝";
    }

    @Override
    public String onKickSuccess(String playerName, String reason) {
        return "§b✧ 默认模型：§a" + playerName + " §e已被踢出！\n" +
               "§b原因: §f" + reason + "\n" +
               "§b维护秩序，不容破坏！§b(◕‿◕✿)";
    }

    @Override
    public String toggleBroadcast(boolean enabled) {
        return "§b默认模型：§a自动广播已经 " + (enabled ? "开启" : "关闭");
    }

    @Override
    public String reloadConfig() {
        return "§b默认模型：§a配置重新加载完成";
    }

    @Override
    public String addBan(String player, int days, String reason) {
        return "§b默认模型：§a玩家 " + player + " 已被封禁 " + days + " 天，原因是：" + reason;
    }

    @Override
    public String removeBan(String player) {
        return "§b默认模型：§a玩家 " + player + " 已从封禁名单中移除";
    }

    @Override
    public String addMute(String player, String reason) {
        return "§b默认模型：§a玩家 " + player + " 已被禁言，原因是：" + reason;
    }

    @Override
    public String removeMute(String player) {
        return "§b默认模型：§a玩家 " + player + " 的禁言已解除";
    }

    @Override
    public String addBanIp(String ip, int days, String reason) {
        return "§b默认模型：§aIP " + ip + " 已被封禁 " + days + " 天，原因是：" + reason;
    }

    @Override
    public String removeBanIp(String ip) {
        return "§b默认模型：§aIP " + ip + " 的封禁已解除";
    }

    @Override
    public String addWarn(String player, String reason) {
        return "§b默认模型：§a玩家 " + player + " 已被警告，原因是：" + reason + "。警告三次将被自动封禁。";
    }

    @Override
    public String removeWarn(String player) {
        return "§b默认模型：§a玩家 " + player + " 的警告记录已移除。";
    }
}