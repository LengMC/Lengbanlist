package org.leng.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class YamlLoaderUtil {
    /**
     * 加载自定义 YAML 配置文件
     *
     * @param plugin       Bukkit 插件实例
     * @param configFileName 自定义 YAML 配置文件的名称
     * @return 加载后的 FileConfiguration 对象，如果加载失败则返回 null
     */
    public static FileConfiguration loadCustomYamlConfig(JavaPlugin plugin, String configFileName) {
        File customConfigFile = new File(plugin.getDataFolder(), configFileName);
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource(configFileName, false);
        }
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        return customConfig;
    }

    /**
     * 保存自定义 YAML 配置文件
     *
     * @param plugin       Bukkit 插件实例
     * @param configFileName 自定义 YAML 配置文件的名称
     * @param config       要保存的 FileConfiguration 对象
     */
    public static void saveCustomYamlConfig(JavaPlugin plugin, String configFileName, FileConfiguration config) {
        File customConfigFile = new File(plugin.getDataFolder(), configFileName);
        try {
            config.save(customConfigFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save custom YAML config: " + configFileName);
            e.printStackTrace();
        }
    }
}
