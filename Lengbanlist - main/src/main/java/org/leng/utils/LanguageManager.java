package org.leng.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.leng.Lengbanlist;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import org.json.JSONObject;
import java.util.List;
import java.util.Arrays;

public class LanguageManager {
    private final Lengbanlist plugin;

    public LanguageManager(Lengbanlist plugin) {
        this.plugin = plugin;
    }

    /**
     * 切换语言
     *
     * @param player       玩家
     * @param languageCode 语言代码
     */
    public void switchLanguage(Player player, String languageCode) {
        // 检查语言文件是否存在
        File languageFile = new File(plugin.getDataFolder(), "language/" + languageCode + ".json");
        if (!languageFile.exists()) {
            plugin.getLogger().warning("语言文件不存在: " + languageFile.getName());
            // 回退到默认语言
            languageCode = "default";
            languageFile = new File(plugin.getDataFolder(), "language/default.json");
        }

        // 加载语言文件
        loadLanguageFile(languageFile);

        // 发送语言切换成功的消息
        String languageChangedMessage = plugin.getConfig().getString("language_changed", "§a语言已切换为: %language%");
        player.sendMessage(languageChangedMessage.replace("%language%", languageCode));
    }

    /**
     * 加载语言文件
     *
     * @param languageFile 语言文件
     */
    private void loadLanguageFile(File languageFile) {
        try {
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(languageFile));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            // 解析 JSON 数据
            JSONObject languageConfig = new JSONObject(content.toString());

            // 更新配置文件
            plugin.getConfig().set("prefix", languageConfig.optString("prefix", "§7[Lengbanlist] "));
            plugin.getConfig().set("language_changed", languageConfig.optString("language_changed", "§a语言已切换为: %language%"));
            plugin.getConfig().set("help_header", languageConfig.optString("help_header", "§bLengbanlist §2§o帮助信息 - 默认风格:"));
            plugin.getConfig().set("help_commands", languageConfig.optJSONObject("help_commands"));
            plugin.getConfig().set("version_info", languageConfig.optString("version_info", "§6当前版本: %version%"));
            plugin.getConfig().set("hitokoto", languageConfig.optString("hitokoto", "§d%hitokoto%"));
            plugin.saveConfig();
        } catch (Exception e) {
            plugin.getLogger().warning("无法加载语言文件: " + languageFile.getName());
            e.printStackTrace();
        }
    }

    /**
     * 重新加载语言设置
     */
    public void reloadLanguage() {
        FileConfiguration config = plugin.getConfig();
        String defaultLanguage = config.getString("language", "default");

        // 加载默认语言文件
        File languageFile = new File(plugin.getDataFolder(), "language/" + defaultLanguage + ".json");
        if (!languageFile.exists()) {
            plugin.getLogger().warning("默认语言文件不存在: " + languageFile.getName());
            defaultLanguage = "default";
            languageFile = new File(plugin.getDataFolder(), "language/default.json");
        }

        loadLanguageFile(languageFile);

        // 通知所有玩家语言已重新加载
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(plugin.getConfig().getString("language_changed", "§a语言已切换为: %language%").replace("%language%", defaultLanguage));
        }
    }

    /**
     * 打开语言选择界面
     *
     * @param player 玩家
     */
    public void openLanguageSelectionUI(Player player) {
        Inventory languageSelectionUI = Bukkit.createInventory(player, 9, plugin.prefix() + "语言选择");

        // 添加语言选项
        List<String> languages = Arrays.asList("default", "en_US");
        for (String language : languages) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6" + language);
            item.setItemMeta(meta);
            languageSelectionUI.addItem(item);
        }

        player.openInventory(languageSelectionUI);
    }
}