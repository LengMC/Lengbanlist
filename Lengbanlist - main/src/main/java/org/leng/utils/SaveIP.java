package org.leng.utils;

import org.bukkit.entity.Player;
import org.leng.Lengbanlist;

import java.io.File; 
import java.io.IOException; 
import java.util.List;

public class SaveIP {
    public static void saveIP(Player player) {
        List<String> ipList = Lengbanlist.getInstance().ipFC.getStringList("ip");
        ipList.add(player.getName() + ":" + player.getAddress().getAddress().getHostAddress());
        Lengbanlist.getInstance().ipFC.set("ip", ipList);

        // 保存到文件
        try {
            Lengbanlist.getInstance().ipFC.save(new File(Lengbanlist.getInstance().getDataFolder(), "ip.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getIP(String player) {
        List<String> ipList = Lengbanlist.getInstance().ipFC.getStringList("ip");
        if (ipList.isEmpty()) {
            return null;
        }
        for (String entry : ipList) {
            String[] parts = entry.split(":");
            if (parts.length == 2 && parts[0].equals(player)) {
                return parts[1];
            }
        }
        return null;
    }
}