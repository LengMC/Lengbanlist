package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

public class Zhongli implements Model {
    @Override
    public String getName() {
        return "Zhongli";
    }

@Override
public void showHelp(CommandSender sender) {
    Utils.sendMessage(sender, "§bLengbanlist §2§o帮助信息 - 钟离风格:");
    Utils.sendMessage(sender, "§b§l/lban list - §3§o查看被封禁的名单，一切尽在掌控之中。");
    Utils.sendMessage(sender, "§b§l/lban a - §3§o广播当前封禁人数，维护秩序。");
    Utils.sendMessage(sender, "§b§l/lban toggle - §3§o开启/关闭自动广播，一切尽在掌控之中。");
    Utils.sendMessage(sender, "§b§l/lban model <模型名称> - §3§o切换模型，体验不同的风格。");
    Utils.sendMessage(sender, "§b§l/lban reload - §3§o重新加载配置，确保一切完美无缺。");
    Utils.sendMessage(sender, "§b§l/lban add <玩家名> <天数> <原因> - §3§o添加封禁，维护秩序。");
    Utils.sendMessage(sender, "§b§l/lban remove <玩家名> - §3§o移除封禁，宽恕是美德。");
    Utils.sendMessage(sender, "§b§l/lban mute <玩家名> <原因> - §3§o禁言玩家，维护秩序。");
    Utils.sendMessage(sender, "§b§l/lban unmute <玩家名> - §3§o解除禁言，宽恕是美德。");
    Utils.sendMessage(sender, "§b§l/lban list-mute - §3§o查看禁言列表，维护秩序。");
    Utils.sendMessage(sender, "§b§l/lban help - §3§o显示帮助信息，优雅地解决问题。");
    Utils.sendMessage(sender, "§b§l/lban open - §3§o打开可视化操作界面，钟离带你看看秩序的力量！");
    Utils.sendMessage(sender, "§b§l/lban getIP <玩家名> - §3§o查询玩家的 IP 地址，维护秩序。");
    Utils.sendMessage(sender, "§b§l/ban-ip <IP地址> <天数> <原因> - §3§o封禁 IP 地址，维护秩序。");
    Utils.sendMessage(sender, "§b§l/unban-ip <IP地址> - §3§o解除 IP 封禁，宽恕是美德。");
    Utils.sendMessage(sender, "§b§l/lban warn <玩家名> <原因> - §3§o警告玩家，三次警告将自动封禁！");
    Utils.sendMessage(sender, "§b§l/lban unwarn <玩家名> - §3§o移除玩家的警告记录。");
    Utils.sendMessage(sender, "§b§l/lban check <玩家名/IP> - §3§o检查玩家或IP的封禁状态，一切尽在掌控之中。");
    Utils.sendMessage(sender, "§b§l/lban language - §3§o打开语言选择页面，选择适合的语言进行操作。");
    Utils.sendMessage(sender, "§b§l/report <玩家名> <原因> - §3§o发现破坏秩序的行为？及时举报，维护世界平衡。");
    Utils.sendMessage(sender, "§6当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " Model: 钟离 Zhongli");
    Utils.sendMessage(sender, "§d" + Lengbanlist.getInstance().getHitokoto());
}

    @Override
    public String toggleBroadcast(boolean enabled) {
        return "§b钟离说：§a自动广播已经 " + (enabled ? "开启。" : "关闭。") + " 秩序需要维护。";
    }

    @Override
    public String reloadConfig() {
        return "§b钟离说：§a配置重新加载完成。一切尽在掌控之中。";
    }

    @Override
    public String addBan(String player, int days, String reason) {
        return "§b钟离说：§a玩家 " + player + " 已被封禁 " + days + " 天，原因是：" + reason + "。秩序不容破坏。";
    }

    @Override
    public String removeBan(String player) {
        return "§b钟离说：§a玩家 " + player + " 已从封禁名单中移除。宽恕是美德。";
    }

    @Override
    public String addMute(String player, String reason) {
        return "§b钟离说：§a玩家 " + player + " 已被禁言，原因是：" + reason + "。秩序不容破坏。";
    }

    @Override
    public String removeMute(String player) {
        return "§b钟离说：§a玩家 " + player + " 的禁言已解除。宽恕是美德。";
    }

    @Override
    public String addBanIp(String ip, int days, String reason) {
        return "§b钟离说：§aIP " + ip + " 已被封禁 " + days + " 天，原因是：" + reason + "。秩序不容破坏。";
    }

    @Override
    public String removeBanIp(String ip) {
        return "§b钟离说：§aIP " + ip + " 的封禁已解除。宽恕是美德。";
    }

    @Override
    public String addWarn(String player, String reason) {
        return "§b钟离说：§a玩家 " + player + " 已被警告，原因是：" + reason + "。警告三次将被自动封禁。";
    }

    @Override
    public String removeWarn(String player) {
        return "§b钟离说：§a玩家 " + player + " 的警告记录已移除。";
    }
}