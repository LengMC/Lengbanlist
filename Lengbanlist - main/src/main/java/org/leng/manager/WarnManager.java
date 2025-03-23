package org.leng.manager;

import org.leng.object.WarnEntry;

import java.util.ArrayList;
import java.util.List;

public class WarnManager {
    private final List<WarnEntry> warnList = new ArrayList<>();

    public void warnPlayer(String player, String staff, long time, String reason) {
        warnList.add(new WarnEntry(player, staff, time, reason));
    }

    public List<WarnEntry> getPlayerWarnings(String playerName) {
        List<WarnEntry> warnings = new ArrayList<>();
        for (WarnEntry entry : warnList) {
            if (entry.getPlayer().equalsIgnoreCase(playerName)) {
                warnings.add(entry);
            }
        }
        return warnings;
    }

    public void unwarnPlayer(String playerName) {
        warnList.removeIf(entry -> entry.getPlayer().equalsIgnoreCase(playerName));
    }

    public boolean isPlayerWarned(String playerName) {
        return warnList.stream().anyMatch(entry -> entry.getPlayer().equalsIgnoreCase(playerName));
    }
}