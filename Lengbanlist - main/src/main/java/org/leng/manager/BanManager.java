package org.leng.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.models.Model;
import org.leng.object.BanEntry;
import org.leng.object.BanIpEntry;
import org.leng.object.MuteEntry;
import org.leng.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class BanManager {
    // 封禁玩家（修复编译错误）
    public void banPlayer(BanEntry banEntry) {
        Model currentModel = Lengbanlist.getInstance().getModelManager().getCurrentModel();
        String banResult = currentModel.addBan(
            banEntry.getTarget(), 
            (int)((banEntry.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)), 
            banEntry.getReason()
        );
        
        if (banResult != null && !banResult.isEmpty()) {
            String ban = banEntry.toString();
            List<String> banList = Lengbanlist.getInstance().getBanFC().getStringList("ban-list");
            banList.add(ban);
            Lengbanlist.getInstance().getBanFC().set("ban-list", banList);
            Lengbanlist.getInstance().saveBanConfig();
            Player targetPlayer = Bukkit.getPlayer(banEntry.getTarget());
            if (targetPlayer != null) {
                targetPlayer.kickPlayer(banResult);
            }
            Bukkit.broadcastMessage(banResult);
        } else {
            String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
            Bukkit.getLogger().warning("通过模型 [" + modelName + "] 封禁玩家 [" + banEntry.getTarget() + "] 失败！");
        }
    }

    // 封禁 IP（修复编译错误）
    public void banIp(BanIpEntry banIpEntry) {
        Model currentModel = Lengbanlist.getInstance().getModelManager().getCurrentModel();
        String banIpResult = currentModel.addBanIp(
            banIpEntry.getIp(), 
            (int)((banIpEntry.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)), 
            banIpEntry.getReason()
        );
        
        if (banIpResult != null && !banIpResult.isEmpty()) {
            String banIp = banIpEntry.toString();
            List<String> banIpList = Lengbanlist.getInstance().getBanIpFC().getStringList("banip-list");
            banIpList.add(banIp);
            Lengbanlist.getInstance().getBanIpFC().set("banip-list", banIpList);
            Lengbanlist.getInstance().saveBanIpConfig();
            Bukkit.broadcastMessage(banIpResult);
        } else {
            String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
            Bukkit.getLogger().warning("通过模型 [" + modelName + "] 封禁 IP [" + banIpEntry.getIp() + "] 失败！");
        }
    }

    // 以下是原有代码保持不变
    public void unbanPlayer(String target) {
        Model currentModel = Lengbanlist.getInstance().getModelManager().getCurrentModel();
        String unbanResult = currentModel.removeBan(target);
        
        if (unbanResult != null && !unbanResult.isEmpty()) {
            List<String> banList = Lengbanlist.getInstance().getBanFC().getStringList("ban-list");
            for (int i = 0; i < banList.size(); i++) {
                String entry = banList.get(i);
                String[] parts = entry.split(":");
                if (parts[0].equals(target)) {
                    banList.remove(i);
                    break;
                }
            }
            Lengbanlist.getInstance().getBanFC().set("ban-list", banList);
            Lengbanlist.getInstance().saveBanConfig();
            Bukkit.broadcastMessage(unbanResult);
        } else {
            String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
            Bukkit.getLogger().warning("通过模型 [" + modelName + "] 解封玩家 [" + target + "] 失败！");
        }
    }

    public void unbanIp(String ip) {
        Model currentModel = Lengbanlist.getInstance().getModelManager().getCurrentModel();
        String unbanIpResult = currentModel.removeBanIp(ip);
        
        if (unbanIpResult != null && !unbanIpResult.isEmpty()) {
            List<String> banIpList = Lengbanlist.getInstance().getBanIpFC().getStringList("banip-list");
            for (int i = 0; i < banIpList.size(); i++) {
                String entry = banIpList.get(i);
                String[] parts = entry.split(":");
                if (parts[0].equals(ip)) {
                    banIpList.remove(i);
                    break;
                }
            }
            Lengbanlist.getInstance().getBanIpFC().set("banip-list", banIpList);
            Lengbanlist.getInstance().saveBanIpConfig();
            Bukkit.broadcastMessage(unbanIpResult);
        } else {
            String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
            Bukkit.getLogger().warning("通过模型 [" + modelName + "] 解封 IP [" + ip + "] 失败！");
        }
    }

    public boolean isPlayerBanned(String target) {
        List<String> banList = Lengbanlist.getInstance().getBanFC().getStringList("ban-list");
        for (String entry : banList) {
            String[] parts = entry.split(":");
            if (parts[0].equals(target)) {
                return true;
            }
        }
        return false;
    }

    public boolean isIpBanned(String ip) {
        List<String> banIpList = Lengbanlist.getInstance().getBanIpFC().getStringList("banip-list");
        for (String entry : banIpList) {
            String[] parts = entry.split(":");
            if (parts[0].equals(ip)) {
                return true;
            }
        }
        return false;
    }

    public List<BanEntry> getBanList() {
        List<String> banListStrings = Lengbanlist.getInstance().getBanFC().getStringList("ban-list");
        List<BanEntry> banList = new ArrayList<>();
        for (String entry : banListStrings) {
            String[] parts = entry.split(":");
            if (parts.length == 4) {
                String target = parts[0];
                String staff = parts[1];
                long time = Long.parseLong(parts[2]);
                String reason = parts[3];
                banList.add(new BanEntry(target, staff, time, reason, false));
            }
        }
        return banList;
    }

    public List<BanIpEntry> getBanIpList() {
        List<String> banIpListStrings = Lengbanlist.getInstance().getBanIpFC().getStringList("banip-list");
        List<BanIpEntry> banIpList = new ArrayList<>();
        for (String entry : banIpListStrings) {
            String[] parts = entry.split(":");
            if (parts.length == 4) {
                String ip = parts[0];
                String staff = parts[1];
                long time = Long.parseLong(parts[2]);
                String reason = parts[3];
                banIpList.add(new BanIpEntry(ip, staff, time, reason, false));
            }
        }
        return banIpList;
    }

    public void checkBanOnJoin(Player player) {
        BanEntry ban = getBanEntry(player.getName());
        if (ban != null) {
            long currentTime = System.currentTimeMillis();
            if (ban.getTime() <= currentTime) {
                unbanPlayer(player.getName());
            } else {
                player.kickPlayer("您仍处于封禁状态，原因：" + ban.getReason() + "，封禁到：" + TimeUtils.timestampToReadable(ban.getTime()));
            }
        }

        String ip = player.getAddress().getAddress().getHostAddress();
        BanIpEntry banIp = getBanIpEntry(ip);
        if (banIp != null) {
            long currentTime = System.currentTimeMillis();
            if (banIp.getTime() <= currentTime) {
                unbanIp(ip);
            } else {
                player.kickPlayer("您的 IP 仍处于封禁状态，原因：" + banIp.getReason() + "，封禁到：" + TimeUtils.timestampToReadable(banIp.getTime()));
            }
        }
    }

    public BanEntry getBanEntry(String target) {
        List<String> banList = Lengbanlist.getInstance().getBanFC().getStringList("ban-list");
        for (String entry : banList) {
            String[] parts = entry.split(":");
            if (parts[0].equals(target)) {
                String staff = parts[1];
                long time = Long.parseLong(parts[2]);
                String reason = parts[3];
                return new BanEntry(target, staff, time, reason, false);
            }
        }
        return null;
    }

    public BanIpEntry getBanIpEntry(String ip) {
        List<String> banIpList = Lengbanlist.getInstance().getBanIpFC().getStringList("banip-list");
        for (String entry : banIpList) {
            String[] parts = entry.split(":");
            if (parts[0].equals(ip)) {
                String staff = parts[1];
                long time = Long.parseLong(parts[2]);
                String reason = parts[3];
                return new BanIpEntry(ip, staff, time, reason, false);
            }
        }
        return null;
    }

    public boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public boolean isBanned(String player, String reason) {
        BanEntry banEntry = getBanEntry(player);
        return banEntry != null && banEntry.getReason().contains(reason);
    }
    
    public void saveBanList() {
        Lengbanlist.getInstance().saveBanConfig();
    }

    public void saveBanIpConfig() {
        Lengbanlist.getInstance().saveBanIpConfig();
    }
}