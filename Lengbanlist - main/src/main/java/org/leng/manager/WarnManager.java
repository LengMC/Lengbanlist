package org.leng.manager;

import org.leng.Lengbanlist;
import org.leng.object.BanEntry;
import org.leng.object.WarnEntry;
import org.leng.utils.TimeUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.bukkit.configuration.file.FileConfiguration;

public class WarnManager {
    private final List<WarnEntry> warnList = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, Integer> autoBanCounter = new ConcurrentHashMap<>();

    public void warnPlayer(String player, String staff, String reason) {
        WarnEntry entry = new WarnEntry(player, staff, System.currentTimeMillis(), reason);
        warnList.add(entry);
        checkAutoBan(player);
    }

    public List<WarnEntry> getAllWarnings(String playerName) {
        return warnList.stream()
                .filter(e -> e.getPlayer().equalsIgnoreCase(playerName))
                .collect(Collectors.toList());
    }

    public List<WarnEntry> getActiveWarnings(String playerName) {
        return warnList.stream()
                .filter(e -> e.getPlayer().equalsIgnoreCase(playerName) && !e.isRevoked())
                .collect(Collectors.toList());
    }

    public boolean unwarnPlayer(String playerName, int warnId) {
        List<WarnEntry> playerWarnings = getAllWarnings(playerName);
        if (warnId > 0 && warnId <= playerWarnings.size()) {
            WarnEntry entry = playerWarnings.get(warnId - 1);
            if (!entry.isRevoked()) {
                entry.revoke();
                return true;
            }
        }
        return false;
    }

    private void checkAutoBan(String player) {
        long now = System.currentTimeMillis();
        long timeWindow = 30L * 24 * 60 * 60 * 1000; // 30天时间窗口
        
        List<WarnEntry> validWarnings = warnList.stream()
                .filter(e -> e.getPlayer().equalsIgnoreCase(player))
                .filter(e -> !e.isRevoked())
                .filter(e -> (now - e.getTime()) <= timeWindow)
                .collect(Collectors.toList());

        if (validWarnings.size() >= 3) {
            int triggerCount = autoBanCounter.getOrDefault(player, 0) + 1;
            autoBanCounter.put(player, triggerCount);
            
            long banDuration = calculateBanDuration(triggerCount);
            String formattedDuration = TimeUtils.formatDuration(banDuration);
            
            BanEntry banEntry = new BanEntry(
                    player,
                    "System",
                    now + banDuration,
                    String.format("自动封禁（%d次警告，第%d次触发） <Lengbanlist-auto>", 
                            validWarnings.size(), triggerCount),
                    true
            );
            
            Lengbanlist.getInstance().getBanManager().banPlayer(banEntry);
            
            String message = String.format(
                    "§6[Lengbanlist-AutoBan] §e%s §c因30天内累计%d次警告被自动封禁§a%s §6<auto>",
                    player, validWarnings.size(), formattedDuration);
            Lengbanlist.getInstance().getServer().broadcastMessage(message);
        }
    }

    public long calculateBanDuration(int triggerCount) {
        switch (triggerCount) {
            case 1: return TimeUtils.daysToMillis(1);
            case 2: return TimeUtils.daysToMillis(7);
            case 3: return TimeUtils.daysToMillis(30);
            case 4: return TimeUtils.daysToMillis(90);
            case 5: return TimeUtils.daysToMillis(180);
            default: return TimeUtils.daysToMillis(365);
        }
    }

    public void loadFromConfig(FileConfiguration config) {
        warnList.clear();
        for (String entry : config.getStringList("warnings")) {
            String[] parts = entry.split(":");
            if (parts.length >= 5) {
                WarnEntry warn = new WarnEntry(parts[0], parts[1], Long.parseLong(parts[2]), parts[3]);
                if (Boolean.parseBoolean(parts[4])) {
                    warn.revoke();
                }
                warnList.add(warn);
            }
        }
    }

    public void saveToConfig(FileConfiguration config) {
        List<String> warnings = new ArrayList<>();
        for (WarnEntry entry : warnList) {
            warnings.add(entry.getPlayer() + ":" + entry.getStaff() + ":" + 
                        entry.getTime() + ":" + entry.getReason() + ":" + 
                        entry.isRevoked());
        }
        config.set("warnings", warnings);
    }
}