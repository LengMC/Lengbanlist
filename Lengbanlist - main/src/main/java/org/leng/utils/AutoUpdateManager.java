package org.leng.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.leng.Lengbanlist;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.logging.Logger;

public class AutoUpdateManager {
    private final Lengbanlist plugin;
    private final Logger logger;

    public AutoUpdateManager(Lengbanlist plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public void checkAndAutoUpdate() {
        try {
            String latestVersion = GitHubUpdateChecker.getLatestReleaseVersion();
            String currentVersion = plugin.getDescription().getVersion();
            if (GitHubUpdateChecker.isUpdateAvailable(currentVersion)) {
                logger.info("发现新版本：" + latestVersion + "，当前版本：" + currentVersion);
                downloadAndReplace(latestVersion);
            } else {
                logger.info("你正在使用最新版本：" + currentVersion);
            }
        } catch (Exception e) {
            logger.warning("检查更新时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void downloadAndReplace(String version) throws Exception {
        String downloadUrl = "https://github.com/LengMC/Lengbanlist/releases/download/" + version + "/Lengbanlist-" + version + ".jar";
        File pluginFile = new File(plugin.getDataFolder().getParentFile(), "Lengbanlist.jar");

        // 下载新版本
        try (InputStream in = new URL(downloadUrl).openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(pluginFile)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }

        // 禁用当前插件
        disableAndRemoveCurrentPlugin();

        // 加载新版本
        reloadPlugin();
    }

    private void disableAndRemoveCurrentPlugin() {
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        for (Plugin plugin : plugins) {
            if (plugin instanceof JavaPlugin && 
                plugin.getDescription().getName().equals(Lengbanlist.getInstance().getDescription().getName())) {
                
                try {
                    // 使用反射调用 setEnabled(false)
                    Method setEnabledMethod = JavaPlugin.class.getDeclaredMethod("setEnabled", boolean.class);
                    setEnabledMethod.setAccessible(true);
                    setEnabledMethod.invoke(plugin, false);

                    // 使用反射调用 getFile()
                    Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
                    getFileMethod.setAccessible(true);
                    File oldFile = (File) getFileMethod.invoke(plugin);

                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                } catch (Exception e) {
                    logger.warning("禁用和移除插件时出错: " + e.getMessage());
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void reloadPlugin() {
        try {
            Field field = Bukkit.class.getDeclaredField("pluginManager");
            field.setAccessible(true);
            Object pluginManager = field.get(Bukkit.getServer());

            Field pluginsField = pluginManager.getClass().getDeclaredField("plugins");
            pluginsField.setAccessible(true);
            List<?> plugins = (List<?>) pluginsField.get(pluginManager);

            // 找到当前插件并卸载
            for (Object plugin : plugins) {
                if (plugin instanceof JavaPlugin && 
                    ((JavaPlugin) plugin).getDescription().getName().equals(this.plugin.getDescription().getName())) {
                    ((JavaPlugin) plugin).onDisable();
                    plugins.remove(plugin);
                    break;
                }
            }

            // 加载新版本
            pluginManager.getClass().getMethod("loadPlugin", File.class)
                .invoke(pluginManager, new File(plugin.getDataFolder().getParentFile(), "Lengbanlist.jar"));
            logger.info("插件已重新加载。");
        } catch (Exception e) {
            logger.warning("重新加载插件时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}