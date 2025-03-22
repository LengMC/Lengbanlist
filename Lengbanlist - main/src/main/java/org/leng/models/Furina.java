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
    Utils.sendMessage(sender, "§bLengbanlist §2§o帮助信息 - 芙宁娜风格:");
    Utils.sendMessage(sender, "§b§l/lban list - §3§o查看水酱列表，看看谁在划水！");
    Utils.sendMessage(sender, "§b§l/lban a - §3§o广播当前划水人数，让大家都知道！");
    Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭自动广播，想听就开，不想听就关！");
    Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型，试试别的风格吧！");
    Utils.sendMessage(sender, "§b§l/lban reload - §3§o重启水神的大脑，说不定能发现新东西！");
    Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o把划水的人加入黑名单！");
    Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o从黑名单里移除一个人，知错能改，善莫大焉！");
    Utils.sendMessage(sender, "§b§l/lban mute <玩家名> <原因> - §3§o让划水的人安静一会儿！");
    Utils.sendMessage(sender, "§b§l/lban unmute <玩家名> - §3§o让划水的人继续说话！");
    Utils.sendMessage(sender, "§b§l/lban list-mute - §3§o查看禁言列表，看看谁被芙宁娜禁言了！");
    Utils.sendMessage(sender, "§b§l/lban help - §3§o显示芙宁娜的帮助，不懂就问！");
    Utils.sendMessage(sender, "§b§l/lban open - §3§o打开可视化操作界面，芙宁娜带你看看水的力量！");
    Utils.sendMessage(sender, "§b§l/lban getIP <玩家名> - §3§o查询玩家的 IP 地址，看看谁在偷偷摸鱼！");
    Utils.sendMessage(sender, "§b§l/ban-ip <IP地址> <天数> <原因> - §3§o封禁 IP 地址，别再划水啦！");
    Utils.sendMessage(sender, "§b§l/unban-ip <IP地址> - §3§o解除 IP 封禁，给他们一个机会！");
    Utils.sendMessage(sender, "§b§l/lban warn <玩家名> <原因> - §3§o警告玩家，三次警告将自动封禁！");
    Utils.sendMessage(sender, "§b§l/lban unwarn <玩家名> - §3§o移除玩家的警告记录。");
    Utils.sendMessage(sender, "§b§l/lban check <玩家名/IP> - §3§o检查玩家或IP的封禁状态，看看谁在划水！");
    Utils.sendMessage(sender, "§b§l/lban language - §3§o打开语言选择页面，选择适合的语言进行操作。");
    Utils.sendMessage(sender, "§b§l/report <玩家名> <原因> - §3§o发现有人划水？快举报给芙宁娜，让水神来处理！");
    Utils.sendMessage(sender, "§6当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " Model: 芙宁娜 Furina");
    Utils.sendMessage(sender, "§d" + Lengbanlist.getInstance().getHitokoto());
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