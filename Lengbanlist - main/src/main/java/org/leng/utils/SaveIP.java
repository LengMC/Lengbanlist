package org.leng.utils;

import org.bukkit.entity.Player;
import org.leng.Lengbanlist;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class SaveIP {
    // 检查IP是否为真实IP（非私有地址和非本地回环地址）
    private static boolean isRealIP(String ip) {
        // 检查是否为 IPv4 私有地址或本地回环地址
        if (ip != null) {
            // IPv4 私有地址范围
            if (ip.startsWith("10.") || ip.startsWith("172.") || ip.startsWith("192.168.") || ip.startsWith("127.")) {
                return false;
            }
            // IPv6 本地回环地址
            if (ip.equalsIgnoreCase("::1")) {
                return false;
            }
            // IPv6 私有地址范围（例如，以 "fd" 开头的 ULA 地址）
            if (ip.startsWith("fd")) {
                return false;
            }
            // 其他情况视为真实 IP
            return true;
        }
        return false;
    }

    public static void saveIP(Player player) {
        List<String> ipList = Lengbanlist.getInstance().ipFC.getStringList("ip");
        String newIP = player.getAddress().getAddress().getHostAddress();

        // 检查是否已经存在该玩家的IP记录
        boolean found = false;
        for (int i = 0; i < ipList.size(); i++) {
            String entry = ipList.get(i);
            String[] parts = entry.split(":");
            if (parts.length == 2 && parts[0].equals(player.getName())) {
                found = true;
                // 如果新IP是真实IP，则替换为新的IP
                if (isRealIP(newIP)) {
                    ipList.set(i, player.getName() + ":" + newIP);
                }
                // 如果新IP不是真实IP，保留原来的IP
                break;
            }
        }

        // 如果没有找到该玩家的IP记录，并且新IP是真实IP，则添加新的记录
        if (!found && isRealIP(newIP)) {
            ipList.add(player.getName() + ":" + newIP);
        }

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