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
        Utils.sendMessage(sender, "§5╔══════════════════════════════════╗");
        Utils.sendMessage(sender, "§5║  §bLengbanlist §5§l胡桃の帮助菜单  §5║");
        Utils.sendMessage(sender, "§5╠══════════════════════════════════╣");
        Utils.sendMessage(sender, "§5✦ §b/lban list §7- §d查看往生堂黑名单 §5⚰️");
        Utils.sendMessage(sender, "§5✦ §b/lban a §7- §d广播当前往生人数 §5👻");
        Utils.sendMessage(sender, "§5✦ §b/lban toggle §7- §d开关自动广播 §5🔊");
        Utils.sendMessage(sender, "§5✦ §b/lban model <名称> §7- §d切换风格 §5🎭");
        Utils.sendMessage(sender, "§5✦ §b/lban reload §7- §d重载胡桃小脑瓜 §5🧠");
        Utils.sendMessage(sender, "§5✦ §b/lban add <玩家> <天> <原因> §7- §d加入黑名单 §5📜");
        Utils.sendMessage(sender, "§5✦ §b/lban remove <玩家> §7- §d移除黑名单 §5✂️");
        Utils.sendMessage(sender, "§5✦ §b/kick <玩家> <原因> §7- §d踢出不听话的家伙 §5👢");
        Utils.sendMessage(sender, "§5✦ §b/lban mute <玩家> <原因> §7- §d禁言 §5🤫");
        Utils.sendMessage(sender, "§5✦ §b/lban unmute <玩家> §7- §d解除禁言 §5🗣️");
        Utils.sendMessage(sender, "§5✦ §b/lban warn <玩家> <原因> §7- §d警告 §5⚠️");
        Utils.sendMessage(sender, "§5✦ §b/lban check <玩家/IP> §7- §d检查状态 §5🔍");
        Utils.sendMessage(sender, "§5✦ §b/report <玩家> <原因> §7- §d举报捣蛋鬼 §5📢");
        Utils.sendMessage(sender, "§7-> §5§l/report accept <举报编号> §7- §d受理举报 §5✅");
        Utils.sendMessage(sender, "§7-> §5§l/report close <举报编号> §7- §d关闭举报 §5❌");
        Utils.sendMessage(sender, "§5✦ §b/lban info §7- §d查看插件信息，了解当前运行状态，胡桃的小脑瓜又清晰啦！");
        Utils.sendMessage(sender, "§5╚══════════════════════════════════╝");
        Utils.sendMessage(sender, "§5♡ 当前版本: " + Lengbanlist.getInstance().getPluginVersion() + " §7| §5胡桃模式");
    }

    @Override
    public String getKickMessage(String reason) {
        return "§5╔══════════════════════════╗\n" +
               "§5║   §d往生堂驱逐通知  §5║\n" +
               "§5╠══════════════════════════╣\n" +
               "§d⚰️ 你被胡桃踢出服务器啦！\n\n" +
               "§7原因: §f" + reason + "\n\n" +
               "§d想回来记得找胡桃买棺材哦~\n" +
               "§5╚══════════════════════════╝";
    }

    @Override
    public String onKickSuccess(String playerName, String reason) {
        return "§d✧ 胡桃说：§a" + playerName + " §e已被踢出！\n" +
               "§5原因: §f" + reason + "\n" +
               "§d调皮捣蛋可是要额外收费的~ §5(◕‿◕✿)";
    }

    @Override
    public String toggleBroadcast(boolean enabled) {
        return "§d胡桃说：§a自动广播已经 " + (enabled ? "开启啦！" : "关闭啦！") + " 快来听听谁又倒霉啦！";
    }

    @Override
    public String reloadConfig() {
        return "§d胡桃说：§a配置重新加载完成！胡桃的大脑又清晰啦！";
    }

    @Override
    public String addBan(String player, int days, String reason) {
        return "§d胡桃说：§a" + player + " 已被加入往生堂黑名单！封禁 " + days + " 天，原因是：" + reason;
    }

    @Override
    public String removeBan(String player) {
        return "§d胡桃说：§a" + player + " 已从往生堂黑名单中移除啦！知错能改，善莫大焉！";
    }

    @Override
    public String addMute(String player, String reason) {
        return "§d胡桃说：§a" + player + " 已被禁言，原因是：" + reason + "！让他们安静一会儿吧！";
    }

    @Override
    public String removeMute(String player) {
        return "§d胡桃说：§a" + player + " 的禁言已解除，可以继续说话啦！";
    }

    @Override
    public String addBanIp(String ip, int days, String reason) {
        return "§d胡桃说：§aIP " + ip + " 已被封禁 " + days + " 天，原因是：" + reason + "。别想再捣乱啦！";
    }

    @Override
    public String removeBanIp(String ip) {
        return "§d胡桃说：§aIP " + ip + " 的封禁已解除，给他们一个机会！";
    }

    @Override
    public String addWarn(String player, String reason) {
        return "§d胡桃说：§a玩家 " + player + " 已被警告，原因是：" + reason + "！警告三次将被自动封禁！";
    }

    @Override
    public String removeWarn(String player) {
        return "§d胡桃说：§a玩家 " + player + " 的警告记录已移除。";
    }
}