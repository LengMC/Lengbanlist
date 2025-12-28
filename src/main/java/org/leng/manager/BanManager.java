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
    // 封禁玩家（修复时间计算和保存逻辑）
    public void banPlayer(BanEntry banEntry) {
        // 计算封禁天数（用于模型接口）
    long durationMillis = banEntry.getEndTime() - System.currentTimeMillis();
    int durationDays = (int) Math.max(1, durationMillis / (1000 * 60 * 60 * 24));
    
    Model currentModel = Lengbanlist.getInstance().getModelManager().getCurrentModel();
    String banResult = currentModel.addBan(
        banEntry.getTarget(), 
        durationDays, 
        banEntry.getReason()
    );
        
        // 无论模型返回什么结果，都执行封禁逻辑
        // 添加到封禁列表
        List<String> banList = Lengbanlist.getInstance().getBanFC().getStringList("ban-list");
        banList.removeIf(e -> e.startsWith(banEntry.getTarget() + ":")); // 移除旧记录
        banList.add(banEntry.toString());
        
        Lengbanlist.getInstance().getBanFC().set("ban-list", banList);
        Lengbanlist.getInstance().saveBanConfig();
        
        // 踢出玩家
        Player targetPlayer = Bukkit.getPlayer(banEntry.getTarget());
        if (targetPlayer != null) {
            String kickMessage = String.format(
                "§c您已被封禁!\n" +
                "§f原因: §e%s\n" +
                "§f封禁时长: §a%s\n" +
                "§f解封时间: §b%s",
                banEntry.getReason(),
                TimeUtils.formatDuration(durationMillis),
                TimeUtils.timestampToReadable(banEntry.getEndTime())
            );
            targetPlayer.kickPlayer(kickMessage);
        }
        
        // 广播消息 - 使用模型返回的消息
        if (banResult != null && !banResult.isEmpty()) {
            Bukkit.broadcastMessage(banResult);
        } else {
            // 如果模型没有返回消息，使用默认消息并记录警告
            String defaultMessage = String.format(
                "§c玩家 %s 已被封禁！原因：%s，时长：%s",
                banEntry.getTarget(),
                banEntry.getReason(),
                TimeUtils.formatDuration(durationMillis)
            );
            Bukkit.broadcastMessage(defaultMessage);
            
            String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
            Bukkit.getLogger().warning("模型 [" + modelName + "] 封禁玩家 [" + banEntry.getTarget() + "] 时未返回消息，使用默认消息");
        }
    }

// 封禁IP（修复时间处理）
public void banIp(BanIpEntry banIpEntry) {
    long durationMillis = banIpEntry.getEndTime() - System.currentTimeMillis();
    int durationDays = (int) Math.max(1, durationMillis / (1000 * 60 * 60 * 24));
    
    Model currentModel = Lengbanlist.getInstance().getModelManager().getCurrentModel();
    String banIpResult = currentModel.addBanIp(
        banIpEntry.getIp(), 
        durationDays, 
        banIpEntry.getReason()
    );
    
    // 无论模型返回什么结果，都执行封禁逻辑
    List<String> banIpList = Lengbanlist.getInstance().getBanIpFC().getStringList("banip-list");
    banIpList.removeIf(e -> e.startsWith(banIpEntry.getIp() + ":"));
    banIpList.add(banIpEntry.toString());
    
    Lengbanlist.getInstance().getBanIpFC().set("banip-list", banIpList);
    Lengbanlist.getInstance().saveBanIpConfig();
    
    // 广播消息 - 使用模型返回的消息
    if (banIpResult != null && !banIpResult.isEmpty()) {
        Bukkit.broadcastMessage(banIpResult);
    } else {
        // 如果模型没有返回消息，使用默认消息并记录警告
        String defaultMessage = String.format(
            "§cIP %s 已被封禁！原因：%s，时长：%s",
            banIpEntry.getIp(),
            banIpEntry.getReason(),
            TimeUtils.formatDuration(durationMillis)
        );
        Bukkit.broadcastMessage(defaultMessage);
        
        String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
        Bukkit.getLogger().warning("模型 [" + modelName + "] 封禁 IP [" + banIpEntry.getIp() + "] 时未返回消息，使用默认消息");
    }
}
    
    public void unbanPlayer(String target) {
        Model currentModel = Lengbanlist.getInstance().getModelManager().getCurrentModel();
        String unbanResult = currentModel.removeBan(target);
        
        // 无论模型返回什么结果，都执行解封逻辑
        List<String> banList = Lengbanlist.getInstance().getBanFC().getStringList("ban-list");
        boolean removed = false;
        for (int i = 0; i < banList.size(); i++) {
            String entry = banList.get(i);
            String[] parts = entry.split(":");
            if (parts[0].equals(target)) {
                banList.remove(i);
                removed = true;
                break;
            }
        }
        
        if (removed) {
            Lengbanlist.getInstance().getBanFC().set("ban-list", banList);
            Lengbanlist.getInstance().saveBanConfig();
            
            // 广播消息 - 使用模型返回的消息
            if (unbanResult != null && !unbanResult.isEmpty()) {
                Bukkit.broadcastMessage(unbanResult);
            } else {
                // 如果模型没有返回消息，使用默认消息
                String defaultMessage = String.format("§a玩家 %s 已被解封", target);
                Bukkit.broadcastMessage(defaultMessage);
                
                String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
                Bukkit.getLogger().warning("模型 [" + modelName + "] 解封玩家 [" + target + "] 时未返回消息，使用默认消息");
            }
        } else {
            String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
            Bukkit.getLogger().warning("通过模型 [" + modelName + "] 解封玩家 [" + target + "] 失败：玩家不在封禁列表中");
        }
    }

    public void unbanIp(String ip) {
        Model currentModel = Lengbanlist.getInstance().getModelManager().getCurrentModel();
        String unbanIpResult = currentModel.removeBanIp(ip);
        
        // 无论模型返回什么结果，都执行解封逻辑
        List<String> banIpList = Lengbanlist.getInstance().getBanIpFC().getStringList("banip-list");
        boolean removed = false;
        for (int i = 0; i < banIpList.size(); i++) {
            String entry = banIpList.get(i);
            String[] parts = entry.split(":");
            if (parts[0].equals(ip)) {
                banIpList.remove(i);
                removed = true;
                break;
            }
        }
        
        if (removed) {
            Lengbanlist.getInstance().getBanIpFC().set("banip-list", banIpList);
            Lengbanlist.getInstance().saveBanIpConfig();
            
            // 广播消息 - 使用模型返回的消息
            if (unbanIpResult != null && !unbanIpResult.isEmpty()) {
                Bukkit.broadcastMessage(unbanIpResult);
            } else {
                // 如果模型没有返回消息，使用默认消息
                String defaultMessage = String.format("§aIP %s 已被解封", ip);
                Bukkit.broadcastMessage(defaultMessage);
                
                String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
                Bukkit.getLogger().warning("模型 [" + modelName + "] 解封 IP [" + ip + "] 时未返回消息，使用默认消息");
            }
        } else {
            String modelName = Lengbanlist.getInstance().getModelManager().getCurrentModelName();
            Bukkit.getLogger().warning("通过模型 [" + modelName + "] 解封 IP [" + ip + "] 失败：IP不在封禁列表中");
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
        if (parts.length >= 4) {  
            String target = parts[0];
            String staff = parts[1];
            long time = Long.parseLong(parts[2]);
            String reason = parts[3];
            boolean isAuto = parts.length > 4 ? Boolean.parseBoolean(parts[4]) : false;
            banList.add(new BanEntry(target, staff, time, reason, isAuto));
        }
    }
    return banList;
}

public List<BanIpEntry> getBanIpList() {
    List<String> banIpListStrings = Lengbanlist.getInstance().getBanIpFC().getStringList("banip-list");
    List<BanIpEntry> banIpList = new ArrayList<>();
    for (String entry : banIpListStrings) {
        String[] parts = entry.split(":");
        if (parts.length >= 4) {  
            String ip = parts[0];
            String staff = parts[1];
            long time = Long.parseLong(parts[2]);
            String reason = parts[3];
            boolean isAuto = parts.length > 4 ? Boolean.parseBoolean(parts[4]) : false;
            banIpList.add(new BanIpEntry(ip, staff, time, reason, isAuto));
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