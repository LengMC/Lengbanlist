package org.leng.manager;

import org.bukkit.Bukkit;
import org.leng.Lengbanlist;
import org.leng.object.MuteEntry;

import java.util.ArrayList;
import java.util.List;

public class MuteManager {
    // 禁言玩家
    public void mutePlayer(MuteEntry muteEntry) {
        String mute = muteEntry.toString();
        List<String> muteList = Lengbanlist.getInstance().getMuteFC().getStringList("mute-list");
        muteList.add(mute);
        Lengbanlist.getInstance().getMuteFC().set("mute-list", muteList);
        Lengbanlist.getInstance().saveMuteConfig();
        Bukkit.broadcastMessage("§a玩家 " + muteEntry.getTarget() + " 已被禁言，原因：" + muteEntry.getReason());
    }

    // 解除禁言
  public void unmutePlayer(String target) {
    List<String> muteList = Lengbanlist.getInstance().getMuteFC().getStringList("mute-list");
    for (int i = 0; i < muteList.size(); i++) {
        String entry = muteList.get(i);
        String[] parts = entry.split(":");
        if (parts.length >= 1 && parts[0].equals(target)) { 
            muteList.remove(i);
            break;
        }
    }
    Lengbanlist.getInstance().getMuteFC().set("mute-list", muteList);
    Lengbanlist.getInstance().saveMuteConfig(); 
    Bukkit.broadcastMessage("§a玩家 " + target + " 已解除禁言");
}

    // 获取禁言列表
  public List<MuteEntry> getMuteList() {
    List<String> muteListStrings = Lengbanlist.getInstance().getMuteFC().getStringList("mute-list");
    List<MuteEntry> muteList = new ArrayList<>();
    for (String entry : muteListStrings) {
        String[] parts = entry.split(":");
        if (parts.length == 4) { // 确保 parts 数组长度为 4
            String target = parts[0];
            String staff = parts[1];
            long time = Long.parseLong(parts[2]);
            String reason = parts[3];
            muteList.add(new MuteEntry(target, staff, time, reason));
        }
    }
    return muteList;
}

    // 检查玩家是否被禁言
    public boolean isPlayerMuted(String playerName) {
        for (MuteEntry entry : getMuteList()) {
            if (entry.getTarget().equals(playerName)) {
                return true;
            }
        }
        return false;
    }
}