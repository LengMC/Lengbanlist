package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

public class Xiao implements Model {
    @Override
    public String getName() {
        return "Xiao";
    }

@Override
public void showHelp(CommandSender sender) {
    Utils.sendMessage(sender, "§bLengbanlist §2§o帮助信息 - 魈风格:");
    Utils.sendMessage(sender, "§b§l/lban list - §3§o查看被封禁的名单，这些家伙真是麻烦！");
    Utils.sendMessage(sender, "§b§l/lban a - §3§o广播当前封禁人数，让大家都知道这些捣乱的家伙！");
    Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭自动广播，想听就听，不想听就关！");
    Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型，试试别的风格吧！");
    Utils.sendMessage(sender, "§b§l/lban reload - §3§o重新加载配置，说不定能发现新东西！");
    Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o添加封禁，不守规矩就封了！");
    Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o移除封禁，知错能改，就放过他们吧！");
    Utils.sendMessage(sender, "§b§l/lban mute <玩家名> <原因> - §3§o禁言玩家，让他们安静一会儿！");
    Utils.sendMessage(sender, "§b§l/lban unmute <玩家名> - §3§o解除禁言，让他们继续说话吧！");
    Utils.sendMessage(sender, "§b§l/lban list-mute - §3§o查看禁言列表，看看谁被魈禁言了！");
    Utils.sendMessage(sender, "§b§l/lban help - §3§o显示帮助信息，不懂就问，别装懂！");
    Utils.sendMessage(sender, "§b§l/lban open - §3§o打开可视化操作界面，魈带你看看风起的地方！");
    Utils.sendMessage(sender, "§b§l/lban getIP <玩家名> - §3§o查询玩家的 IP 地址，看看谁在捣乱！");
    Utils.sendMessage(sender, "§b§l/ban-ip <IP地址> <天数> <原因> - §3§o封禁 IP 地址，别再捣乱了！");
    Utils.sendMessage(sender, "§b§l/unban-ip <IP地址> - §3§o解除 IP 封禁，放过他们吧！");
    Utils.sendMessage(sender, "§b§l/lban warn <玩家名> <原因> - §3§o警告玩家，三次警告将自动封禁！");
    Utils.sendMessage(sender, "§b§l/lban unwarn <玩家名> - §3§o移除玩家的警告记录。");
    Utils.sendMessage(sender, "§b§l/lban check <玩家名/IP> - §3§o检查玩家或IP的封禁状态，看看谁在捣乱！");
    Utils.sendMessage(sender, "§b§l/lban language - §3§o打开语言选择页面，选择适合的语言进行操作！");
    Utils.sendMessage(sender, "§b§l/report <玩家名> <原因> - §3§o发现捣乱的家伙？快举报给魈，维护风起地的和平！");
    Utils.sendMessage(sender, "§7-> §b§l/report accept <举报编号> - §3§o受理举报，开始处理问题！");
    Utils.sendMessage(sender, "§7-> §b§l/report close <举报编号> - §3§o关闭举报，问题已解决！");
    Utils.sendMessage(sender, "§b§l/lban admin - §3§o打开管理员界面，查看和处理举报。");
    Utils.sendMessage(sender, "§6当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " Model: 魈 Xiao");
}

    @Override
    public String toggleBroadcast(boolean enabled) {
        return "§b魈说：§a自动广播已经 " + (enabled ? "开启！" : "关闭！") + " 想听就听，不想听就关！";
    }

    @Override
    public String reloadConfig() {
        return "§b魈说：§a配置重新加载完成！说不定能发现新东西！";
    }

    @Override
    public String addBan(String player, int days, String reason) {
        return "§b魈说：§a" + player + " 已被封禁 " + days + " 天，原因是：" + reason + "！不守规矩，就别怪魈无情！";
    }

    @Override
    public String removeBan(String player) {
        return "§b魈说：§a" + player + " 已从封禁名单中移除。知错能改，就放过他们吧！";
    }

    @Override
    public String addMute(String player, String reason) {
        return "§b魈说：§a" + player + " 已被禁言，原因是：" + reason + "！让他们安静一会儿吧！";
    }

    @Override
    public String removeMute(String player) {
        return "§b魈说：§a" + player + " 的禁言已解除，可以继续说话了！";
    }

    @Override
    public String addBanIp(String ip, int days, String reason) {
        return "§b魈说：§aIP " + ip + " 已被封禁 " + days + " 天，原因是：" + reason + "！别再捣乱了！";
    }

    @Override
    public String removeBanIp(String ip) {
        return "§b魈说：§aIP " + ip + " 的封禁已解除，放过他们吧！";
    }

    @Override
    public String addWarn(String player, String reason) {
        return "§b魈说：§a玩家 " + player + " 已被警告，原因是：" + reason + "！警告三次将被自动封禁！";
    }

    @Override
    public String removeWarn(String player) {
        return "§b魈说：§a玩家 " + player + " 的警告记录已移除。";
    }
}