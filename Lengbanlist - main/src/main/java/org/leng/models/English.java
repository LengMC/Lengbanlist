package org.leng.models;

import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

public class English implements Model {
    @Override
    public String getName() {
        return "English";
    }

    @Override
    public void showHelp(CommandSender sender) {
        Utils.sendMessage(sender, "§b╔══════════════════════════════════════╗");
        Utils.sendMessage(sender, "§b║ §2§oLengbanlist Help - English Style §b║");
        Utils.sendMessage(sender, "§b╠══════════════════════════════════════╣");
        Utils.sendMessage(sender, "§2✦ §b/lban list §7- §3View banned players list");
        Utils.sendMessage(sender, "§2✦ §b/lban a §7- §3Broadcast current ban count");
        Utils.sendMessage(sender, "§2✦ §b/lban toggle §7- §3Toggle automatic broadcast");
        Utils.sendMessage(sender, "§2✦ §b/lban model <model name> §7- §3Switch model");
        Utils.sendMessage(sender, "§2✦ §b/lban reload §7- §3Reload configuration");
        Utils.sendMessage(sender, "§2✦ §b/lban add <player> <time/auto> <reason> §7- §3Add a ban");
        Utils.sendMessage(sender, "§7  = §b/ban");
        Utils.sendMessage(sender, "§2✦ §b/lban remove <player> §7- §3Remove a ban");
        Utils.sendMessage(sender, "§7  = §b/unban");
        Utils.sendMessage(sender, "§2✦ §b/lban mute <player> <reason> §7- §3Mute a player");
        Utils.sendMessage(sender, "§2✦ §b/lban unmute <player> §7- §3Unmute a player");
        Utils.sendMessage(sender, "§2✦ §b/lban list-mute §7- §3View muted players list");
        Utils.sendMessage(sender, "§2✦ §b/lban help §7- §3Show help information");
        Utils.sendMessage(sender, "§2✦ §b/lban open §7- §3Open visual operation interface");
        Utils.sendMessage(sender, "§2✦ §b/lban getIP <player> §7- §3Query player's IP address");
        Utils.sendMessage(sender, "§2✦ §b/ban-ip <IP address> <time/auto> <reason> §7- §3Ban IP address");
        Utils.sendMessage(sender, "§2✦ §b/unban-ip <IP address> §7- §3Unban IP address");
        Utils.sendMessage(sender, "§2✦ §b/lban warn <player> <reason> §7- §3Warn player, 3 warnings will auto-ban!");
        Utils.sendMessage(sender, "§7  = §b/warn");
        Utils.sendMessage(sender, "§7-> §2§l/unwarn <player> <warning ID or UUID> §7- §3Remove specific warning");
        Utils.sendMessage(sender, "§7-> §2§l/unwarn <player> §7- §3Remove all warnings");
        Utils.sendMessage(sender, "§2✦ §b/lban unwarn <player> §7- §3Remove player's warning records");
        Utils.sendMessage(sender, "§7  = §b/unwarn");
        Utils.sendMessage(sender, "§2✦ §b/lban check <player/IP> §7- §3Check ban status of player or IP");
        Utils.sendMessage(sender, "§2✦ §b/report <player> <reason> §7- §3Report player, maintain server order");
        Utils.sendMessage(sender, "§7-> §2§l/report accept <report ID> §7- §3Accept report, start processing");
        Utils.sendMessage(sender, "§7-> §2§l/report close <report ID> §7- §3Close report, issue resolved");
        Utils.sendMessage(sender, "§2✦ §b/kick <player> <reason> §7- §3Kick unruly players!");
        Utils.sendMessage(sender, "§2✦ §b/lban info §7- §3View plugin information and current status");
        Utils.sendMessage(sender, "§2✦ §b/setban <player/IP> <time/forever/auto> <reason> §7- §3Reset ban time, maintain order!");
        Utils.sendMessage(sender, "§b╚══════════════════════════════════════╝");
        Utils.sendMessage(sender, "§2♡ Current Version: " + Lengbanlist.getInstance().getPluginVersion() + " §7| §bModel: English");
    }

    @Override
    public String getKickMessage(String reason) {
        return "§b╔══════════════════════════╗\n" +
               "§b║   §dEnglish Model Kick Notice  §b║\n" +
               "§b╠══════════════════════════╣\n" +
               "§d☠️ You have been kicked from the server!\n\n" +
               "§7Reason: §f" + reason + "\n\n" +
               "§dPlease follow the rules next time~\n" +
               "§b╚══════════════════════════╝";
    }

    @Override
    public String onKickSuccess(String playerName, String reason) {
        return "§b✧ English Model: §a" + playerName + " §ehas been kicked!\n" +
               "§bReason: §f" + reason + "\n" +
               "§bMaintaining order, no disruption allowed! §b(◕‿◕✿)";
    }

    @Override
    public String toggleBroadcast(boolean enabled) {
        return "§bEnglish Model: §aAutomatic broadcast has been " + (enabled ? "enabled" : "disabled");
    }

    @Override
    public String reloadConfig() {
        return "§bEnglish Model: §aConfiguration reloaded successfully";
    }

    @Override
    public String addBan(String player, int days, String reason) {
        String durationText = (days == Integer.MAX_VALUE / (1000 * 60 * 60 * 24)) ? "permanently" : days + " days";
        return "§bEnglish Model: §aPlayer " + player + " has been banned for " + durationText + ", reason: " + reason;
    }

    @Override
    public String removeBan(String player) {
        return "§bEnglish Model: §aPlayer " + player + " has been removed from ban list";
    }

    @Override
    public String addMute(String player, String reason) {
        return "§bEnglish Model: §aPlayer " + player + " has been muted, reason: " + reason;
    }

    @Override
    public String removeMute(String player) {
        return "§bEnglish Model: §aPlayer " + player + " has been unmuted";
    }

    @Override
    public String addBanIp(String ip, int days, String reason) {
        String durationText = (days == Integer.MAX_VALUE / (1000 * 60 * 60 * 24)) ? "permanently" : days + " days";
        return "§bEnglish Model: §aIP " + ip + " has been banned for " + durationText + ", reason: " + reason;
    }

    @Override
    public String removeBanIp(String ip) {
        return "§bEnglish Model: §aIP " + ip + " has been unbanned";
    }

    @Override
    public String addWarn(String player, String reason) {
        return "§bEnglish Model: §aPlayer " + player + " has been warned, reason: " + reason + ". 3 warnings will result in automatic ban.";
    }

    @Override
    public String removeWarn(String player) {
        return "§bEnglish Model: §aWarning records for " + player + " have been removed.";
    }
}