package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.leng.Lengbanlist;
import org.leng.utils.Utils;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

public class InfoCommand implements CommandExecutor {
    private final Lengbanlist plugin;
    private final Logger logger;

    public InfoCommand(Lengbanlist plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lengbanlist.info")) {
            Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
            return true;
        }

        // 获取服务器信息
        String serverVersion = plugin.getServer().getVersion();
        String serverCore = "Unknown";
        try {
            serverCore = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.warning("无法获取服务端核心名称");
        }

        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        int onlinePlayers = plugin.getServer().getOnlinePlayers().size();
        double cpuLoad = getSystemCpuLoad();

        // 构建信息字符串
        StringBuilder infoMessage = new StringBuilder();
        infoMessage.append("§b§lLengbanlist 插件信息 §b§l").append(plugin.getDescription().getVersion()).append("\n");
        infoMessage.append("§7当前运行在：§b").append(serverVersion).append("\n");
        infoMessage.append("§7当前服务端核心：§b").append(serverCore).append("\n");
        infoMessage.append("§7当前内存占用：§b").append(usedMemory / (1024 * 1024)).append("MB / ").append(totalMemory / (1024 * 1024)).append("MB\n");
        infoMessage.append("§7当前在线玩家：§b").append(onlinePlayers).append("\n");
        infoMessage.append("§7当前CPU占用：§b").append(String.format("%.2f", cpuLoad)).append("%\n");

        Utils.sendMessage(sender, infoMessage.toString());
        return true;
    }

    private double getSystemCpuLoad() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{"SystemCpuLoad"});
            if (list.isEmpty()) return 0.0;
            Attribute att = (Attribute) list.get(0);
            Double value = (Double) att.getValue();
            return value * 100;
        } catch (Exception e) {
            logger.warning("获取CPU负载失败: " + e.getMessage());
            return 0.0;
        }
    }
}