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

    // 警告玩家
    public void warnPlayer(String player, String staff, String reason) {
        WarnEntry entry = new WarnEntry(player, staff, System.currentTimeMillis(), reason);
        warnList.add(entry);
        checkAutoBan(player);
    }

    // 警告 IP
    public void warnIp(String ip, String staff, String reason) {
        WarnEntry entry = new WarnEntry(ip, staff, System.currentTimeMillis(), reason);
        warnList.add(entry);
        // IP 警告不触发自动封禁
    }

    // 获取某玩家的所有警告记录
    public List<WarnEntry> getAllWarnings(String target) {
        return warnList.stream()
                .filter(e -> e.getPlayer().equalsIgnoreCase(target))
                .collect(Collectors.toList());
    }

    // 获取某玩家的有效警告记录（未撤销的）
    public List<WarnEntry> getActiveWarnings(String target) {
        return warnList.stream()
                .filter(e -> e.getPlayer().equalsIgnoreCase(target) && !e.isRevoked())
                .collect(Collectors.toList());
    }

    // 撤销某玩家的某次警告
    public boolean unwarnPlayer(String target, int warnId) {
        List<WarnEntry> playerWarnings = getAllWarnings(target);
        if (warnId > 0 && warnId <= playerWarnings.size()) {
            WarnEntry entry = playerWarnings.get(warnId - 1);
            if (!entry.isRevoked()) {
                entry.revoke();
                return true;
            }
        }
        return false;
    }

    // 检查是否需要自动封禁
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
                    "LBAC",
                    now + banDuration,
                    String.format("自动封禁（%d次警告，第%d次触发） <LBAC>", 
                            validWarnings.size(), triggerCount),
                    true
            );

            Lengbanlist.getInstance().getBanManager().banPlayer(banEntry);

            String message = String.format(
                    "§6[LBAC] §e%s §c因30天内累计%d次警告被自动封禁§a%s §6<此封禁由系统决定>",
                    player, validWarnings.size(), formattedDuration);
            Lengbanlist.getInstance().getServer().broadcastMessage(message);
        }
    }

    // 计算封禁时长
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

    // 从配置文件加载警告记录
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

    // 保存警告记录到配置文件
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