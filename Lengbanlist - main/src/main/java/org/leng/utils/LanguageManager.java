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
import org.leng.manager.ModelManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Arrays;
import java.util.List;

public class LanguageManager {
    private final Lengbanlist plugin;
    private final ModelManager modelManager;

    public LanguageManager(Lengbanlist plugin) {
        this.plugin = plugin;
        this.modelManager = ModelManager.getInstance();
    }

    /**
     * 切换语言
     *
     * @param player       玩家
     * @param languageCode 语言代码
     */
    public void switchLanguage(Player player, String languageCode) {
        // 保存玩家选择的语言
        plugin.getConfig().set("player_language." + player.getUniqueId(), languageCode);
        plugin.saveConfig();

        // 如果切换到非默认语言，禁用 ModelManager
        if (!languageCode.equals("default")) {
            modelManager.setEnabled(false);
            player.sendMessage(plugin.prefix() + "§a已禁用模型切换功能，切换到 JSON 文件显示帮助信息。");

            // 加载 JSON 文件中的帮助信息
            loadHelpFromJson(languageCode);
        } else {
            modelManager.setEnabled(true);
            player.sendMessage(plugin.prefix() + "§a已启用模型切换功能。");
        }

        // 发送语言切换成功的消息
        player.sendMessage(plugin.prefix() + "§a语言已切换为: " + languageCode);
    }

    /**
     * 从 JSON 文件加载帮助信息
     *
     * @param languageCode 语言代码
     */
    private void loadHelpFromJson(String languageCode) {
        File languageFile = new File(plugin.getDataFolder(), "language/" + languageCode + ".json");
        if (!languageFile.exists()) {
            plugin.getLogger().warning("语言文件不存在: " + languageFile.getName());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(languageFile))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            // 解析 JSON 内容
            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            String helpHeader = jsonObject.getString("help_header");
            JSONObject helpCommands = jsonObject.getJSONObject("help_commands");
            String versionInfo = jsonObject.getString("version_info");
            String hitokoto = jsonObject.getString("hitokoto"); // 获取一言内容

            // 构建完整的帮助信息
            StringBuilder helpMessage = new StringBuilder();
            helpMessage.append(helpHeader).append("\n");
            for (String command : helpCommands.keySet()) {
                helpMessage.append(helpCommands.getString(command)).append("\n");
            }
            helpMessage.append(versionInfo).append("\n");
            helpMessage.append(hitokoto); // 添加一言内容

            // 更新帮助信息到配置文件
            plugin.getConfig().set("help_header", helpHeader);
            plugin.getConfig().set("help_commands", helpCommands);
            plugin.getConfig().set("version_info", versionInfo);
            plugin.getConfig().set("hitokoto", hitokoto); // 保存一言内容
            plugin.saveConfig();

            // 发送帮助信息到玩家
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(helpMessage.toString());
            }
        } catch (IOException e) {
            plugin.getLogger().warning("无法读取语言文件: " + languageFile.getName());
            e.printStackTrace();
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