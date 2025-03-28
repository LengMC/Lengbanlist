package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

public class Furina implements Model {
    @Override
    public String getName() {
        return "Furina";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§b╔══════════════════════════════════╗");
        Utils.sendMessage(sender, "§b║ §2§oLengbanlist 帮助信息 - 芙宁娜风格 §b║");
        Utils.sendMessage(sender, "§b╠══════════════════════════════════╣");
        Utils.sendMessage(sender, "§2✦ §b/lban list §7- §3查看水酱列表，看看谁在划水！");
        Utils.sendMessage(sender, "§2✦ §b/lban a §7- §3广播当前划水人数，让大家都知道！");
        Utils.sendMessage(sender, "§2✦ §b/lban toggle §7- §3开启/关闭自动广播，想听就开，不想听就关！");
        Utils.sendMessage(sender, "§2✦ §b/lban model <模型名称> §7- §3切换模型，试试别的风格吧！");
        Utils.sendMessage(sender, "§2✦ §b/lban reload §7- §3重启水神的大脑，说不定能发现新东西！");
        Utils.sendMessage(sender, "§2✦ §b/lban add <玩家名> <天数> <原因> §7- §3把划水的人加入黑名单！");
        Utils.sendMessage(sender, "§2✦ §b/lban remove <玩家名> §7- §3从黑名单里移除一个人，知错能改，善莫大焉！");
        Utils.sendMessage(sender, "§2✦ §b/lban mute <玩家名> <原因> §7- §3让划水的人安静一会儿！");
        Utils.sendMessage(sender, "§2✦ §b/lban unmute <玩家名> §7- §3让划水的人继续说话！");
        Utils.sendMessage(sender, "§2✦ §b/lban list-mute §7- §3查看禁言列表，看看谁被芙宁娜禁言了！");
        Utils.sendMessage(sender, "§2✦ §b/lban help §7- §3显示芙宁娜的帮助，不懂就问！");
        Utils.sendMessage(sender, "§2✦ §b/lban open §7- §3打开可视化操作界面，芙宁娜带你看看水的力量！");
        Utils.sendMessage(sender, "§2✦ §b/lban getIP <玩家名> §7- §3查询玩家的 IP 地址，看看谁在偷偷摸鱼！");
        Utils.sendMessage(sender, "§2✦ §b/ban-ip <IP地址> <天数> <原因> §7- §3封禁 IP 地址，别再划水啦！");
        Utils.sendMessage(sender, "§2✦ §b/unban-ip <IP地址> §7- §3解除 IP 封禁，给他们一个机会！");
        Utils.sendMessage(sender, "§2✦ §b/lban warn <玩家名> <原因> §7- §3警告玩家，三次警告将自动封禁！");
        Utils.sendMessage(sender, "§2✦ §b/lban unwarn <玩家名> §7- §3移除玩家的警告记录。");
        Utils.sendMessage(sender, "§2✦ §b/lban check <玩家名/IP> §7- §3检查玩家或IP的封禁状态，看看谁在划水！");
        Utils.sendMessage(sender, "§2✦ §b/lban language §7- §3打开语言选择页面，选择适合的语言进行操作。");
        Utils.sendMessage(sender, "§2✦ §b/report <玩家名> <原因> §7- §3发现有人划水？快举报给芙宁娜，让水神来处理！");
        Utils.sendMessage(sender, "§7-> §2§l/report accept <举报编号> §7- §3受理举报，开始处理划水行为！");
        Utils.sendMessage(sender, "§7-> §2§l/report close <举报编号> §7- §3关闭举报，问题已解决！");
        Utils.sendMessage(sender, "§2✦ §b/kick <玩家名> <原因> §7- §3踢出划水的玩家！");
        Utils.sendMessage(sender, "§2✦ §b/lban info §7- §3查看插件信息，了解当前运行状态，芙宁娜带你看看水的力量！");
        Utils.sendMessage(sender, "§b╚══════════════════════════════════╝");
        Utils.sendMessage(sender, "§2♡ 当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " §7| §b模型: 芙宁娜 Furina");
    }

    @Override
    public String getKickMessage(String reason) {
        return "§b╔══════════════════════════╗\n" +
               "§b║   §d芙宁娜的驱逐通知  §b║\n" +
               "§b╠══════════════════════════╣\n" +
               "§d☠️ 你被芙宁娜踢出服务器啦！\n\n" +
               "§7原因: §f" + reason + "\n\n" +
               "§d下次请遵守规则哦~\n" +
               "§b╚══════════════════════════╝";
    }

    @Override
    public String onKickSuccess(String playerName, String reason) {
        return "§b✧ 芙宁娜说：§a" + playerName + " §e已被踢出！\n" +
               "§b原因: §f" + reason + "\n" +
               "§b维护秩序，不容破坏！§b(◕‿◕✿)";
    }

    @Override
    public String toggleBroadcast(boolean enabled) {
        return "§b芙宁娜说：§a自动广播已经 " + (enabled ? "开启啦！" : "关闭啦！") + " 水酱们要注意啦！";
    }

    @Override
    public String reloadConfig() {
        return "§b芙宁娜说：§a配置重新加载完成！水神的大脑又清晰啦！";
    }

    @Override
    public String addBan(String player, int days, String reason) {
        return "§b芙宁娜说：§a" + player + " 已被加入黑名单！封禁 " + days + " 天，原因是：" + reason + "。划水可不是好习惯哦！";
    }

    @Override
    public String removeBan(String player) {
        return "§b芙宁娜说：§a" + player + " 已从黑名单中移除啦！知错能改，善莫大焉！";
    }

    @Override
    public String addMute(String player, String reason) {
        return "§b芙宁娜说：§a" + player + " 已被禁言，原因是：" + reason + "！让他们安静一会儿吧！";
    }

    @Override
    public String removeMute(String player) {
        return "§b芙宁娜说：§a" + player + " 的禁言已解除，可以继续说话啦！";
    }

    @Override
    public String addBanIp(String ip, int days, String reason) {
        return "§b芙宁娜说：§aIP " + ip + " 已被封禁 " + days + " 天，原因是：" + reason + "。别再划水啦！";
    }

    @Override
    public String removeBanIp(String ip) {
        return "§b芙宁娜说：§aIP " + ip + " 的封禁已解除，给他们一个机会！";
    }

    @Override
    public String addWarn(String player, String reason) {
        return "§b芙宁娜说：§a玩家 " + player + " 已被警告，原因是：" + reason + "！警告三次将被自动封禁！";
    }

    @Override
    public String removeWarn(String player) {
        return "§b芙宁娜说：§a玩家 " + player + " 的警告记录已移除。";
    }
}