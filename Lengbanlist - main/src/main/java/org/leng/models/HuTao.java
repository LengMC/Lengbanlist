package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

public class HuTao implements Model {
    @Override
    public String getName() {
        return "HuTao";
    }

@Override
public void showHelp(CommandSender sender) {
    Utils.sendMessage(sender, "§bLengbanlist §2§o帮助信息 - 胡桃风格:");
    Utils.sendMessage(sender, "§b§l/lban list - §3§o查看往生堂的黑名单！");
    Utils.sendMessage(sender, "§b§l/lban a - §3§o广播当前往生人数，看看谁又倒霉了！");
    Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭自动广播，想听就开，不想听就关！");
    Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型，试试别的风格吧！");
    Utils.sendMessage(sender, "§b§l/lban reload - §3§o重启胡桃的大脑，说不定能发现新东西！");
    Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o把不听话的人加入往生堂！");
    Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o从往生堂名单里移除一个人，知错能改，善莫大焉！");
    Utils.sendMessage(sender, "§b§l/lban mute <玩家名> <原因> - §3§o让不听话的人安静一会儿！");
    Utils.sendMessage(sender, "§b§l/lban unmute <玩家名> - §3§o让不听话的人继续说话！");
    Utils.sendMessage(sender, "§b§l/lban list-mute - §3§o查看禁言列表，看看谁被胡桃禁言了！");
    Utils.sendMessage(sender, "§b§l/lban help - §3§o显示胡桃的帮助，不懂就问！");
    Utils.sendMessage(sender, "§b§l/lban open - §3§o打开可视化操作界面，看看胡桃的魔法！");
    Utils.sendMessage(sender, "§b§l/lban getIP <玩家名> - §3§o查询玩家的 IP 地址，看看谁在捣乱！");
    Utils.sendMessage(sender, "§b§l/ban-ip <IP地址> <天数> <原因> - §3§o封禁 IP 地址，别想再捣乱啦！");
    Utils.sendMessage(sender, "§b§l/unban-ip <IP地址> - §3§o解除 IP 封禁，给他们一个机会！");
    Utils.sendMessage(sender, "§b§l/lban warn <玩家名> <原因> - §3§o警告玩家，三次警告将自动封禁！");
    Utils.sendMessage(sender, "§b§l/lban unwarn <玩家名> - §3§o移除玩家的警告记录。");
    Utils.sendMessage(sender, "§b§l/lban check <玩家名/IP> - §3§o检查玩家或IP的封禁状态，看看谁又倒霉啦！");
    Utils.sendMessage(sender, "§b§l/lban language - §3§o打开语言选择页面，选择适合的语言进行操作。");
    Utils.sendMessage(sender, "§b§l/report <玩家名> <原因> - §3§o发现捣乱的家伙？快告诉胡桃，让往生堂来处理！");
    Utils.sendMessage(sender, "§6当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " Model: 胡桃 Hu Tao");
    Utils.sendMessage(sender, "§d" + Lengbanlist.getInstance().getHitokoto());
}

    @Override
    public String toggleBroadcast(boolean enabled) {
        return "§b胡桃说：§a自动广播已经 " + (enabled ? "开启啦！" : "关闭啦！") + " 快来听听谁又倒霉啦！";
    }

    @Override
    public String reloadConfig() {
        return "§b胡桃说：§a配置重新加载完成！胡桃的大脑又清晰啦！";
    }

    @Override
    public String addBan(String player, int days, String reason) {
        return "§b胡桃说：§a" + player + " 已被加入往生堂黑名单！封禁 " + days + " 天，原因是：" + reason;
    }

    @Override
    public String removeBan(String player) {
        return "§b胡桃说：§a" + player + " 已从往生堂黑名单中移除啦！知错能改，善莫大焉！";
    }

    @Override
    public String addMute(String player, String reason) {
        return "§b胡桃说：§a" + player + " 已被禁言，原因是：" + reason + "！让他们安静一会儿吧！";
    }

    @Override
    public String removeMute(String player) {
        return "§b胡桃说：§a" + player + " 的禁言已解除，可以继续说话啦！";
    }

    @Override
    public String addBanIp(String ip, int days, String reason) {
        return "§b胡桃说：§aIP " + ip + " 已被封禁 " + days + " 天，原因是：" + reason + "。别想再捣乱啦！";
    }

    @Override
    public String removeBanIp(String ip) {
        return "§b胡桃说：§aIP " + ip + " 的封禁已解除，给他们一个机会！";
    }

    @Override
    public String addWarn(String player, String reason) {
        return "§b胡桃说：§a玩家 " + player + " 已被警告，原因是：" + reason + "！警告三次将被自动封禁！";
    }

    @Override
    public String removeWarn(String player) {
        return "§b胡桃说：§a玩家 " + player + " 的警告记录已移除。";
    }
}