package org.leng;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.leng.commands.*;
import org.leng.listeners.*;
import org.leng.manager.*;
import org.leng.object.ReportEntry;
import org.leng.utils.GitHubUpdateChecker;
import org.leng.utils.LanguageManager;
import org.leng.utils.Utils;
import org.leng.utils.YamlLoaderUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;

public class Lengbanlist extends JavaPlugin {
    private static Lengbanlist instance;
    public BanManager banManager;
    public MuteManager muteManager;
    public WarnManager warnManager;
    public ReportManager reportManager;
    public BukkitTask task;
    private boolean isBroadcast;
    public FileConfiguration ipFC;
    private FileConfiguration banFC;
    private FileConfiguration banIpFC;
    private FileConfiguration muteFC;
    private FileConfiguration broadcastFC;
    private FileConfiguration warnFC;
    private FileConfiguration reportFC; 
    private FileConfiguration chatConfig;
    private ModelChoiceListener modelChoiceListener;
    private String hitokoto;
    private ModelManager modelManager;
    private LanguageManager languageManager;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        instance = this;
        banManager = new BanManager();
        muteManager = new MuteManager();
        warnManager = new WarnManager();
        reportManager = new ReportManager(this); 
        isBroadcast = getConfig().getBoolean("opensendtime");
        modelManager = ModelManager.getInstance();
        languageManager = new LanguageManager(this);

        // 初始化 ipFC
        File ipFile = new File(getDataFolder(), "ip.yml");
        if (!ipFile.exists()) {
            ipFile.getParentFile().mkdirs();
            saveResource("ip.yml", false);
        }
        ipFC = YamlConfiguration.loadConfiguration(ipFile);

        // 初始化其他配置文件
        File banFile = new File(getDataFolder(), "ban-list.yml");
        File banIpFile = new File(getDataFolder(), "banip-list.yml");
        File muteFile = new File(getDataFolder(), "mute-list.yml");
        File warnFile = new File(getDataFolder(), "warn-list.yml");
        File reportFile = new File(getDataFolder(), "reports.yml"); 
        if (!banFile.exists()) {
            banFile.getParentFile().mkdirs();
            saveResource("ban-list.yml", false);
        }
        if (!banIpFile.exists()) {
            banIpFile.getParentFile().mkdirs();
            saveResource("banip-list.yml", false);
        }
        if (!muteFile.exists()) {
            muteFile.getParentFile().mkdirs();
            saveResource("mute-list.yml", false);
        }
        if (!warnFile.exists()) {
            warnFile.getParentFile().mkdirs();
            saveResource("warn-list.yml", false);
        }
        if (!reportFile.exists()) { 
            reportFile.getParentFile().mkdirs();
            saveResource("reports.yml", false);
        }
        banFC = YamlConfiguration.loadConfiguration(banFile);
        banIpFC = YamlConfiguration.loadConfiguration(banIpFile);
        muteFC = YamlConfiguration.loadConfiguration(muteFile);
        warnFC = YamlConfiguration.loadConfiguration(warnFile);
        reportFC = YamlConfiguration.loadConfiguration(reportFile); 

         // 初始化 chatConfig
        File chatConfigFile = new File(getDataFolder(), "chatconfig.yml");
        if (!chatConfigFile.exists()) {
            chatConfigFile.getParentFile().mkdirs();
            saveResource("chatconfig.yml", false);
        }
        chatConfig = YamlConfiguration.loadConfiguration(chatConfigFile);
        // 获取一言并存储到成员变量
        hitokoto = getHitokoto();

        // 初始化 broadcastFC
        File broadcastFile = new File(getDataFolder(), "broadcast.yml");
        if (!broadcastFile.exists()) {
            broadcastFile.getParentFile().mkdirs();
            saveResource("broadcast.yml", false);
        }
        broadcastFC = YamlConfiguration.loadConfiguration(broadcastFile);
    }

    @Override
public void onEnable() {
    getServer().getConsoleSender().sendMessage(prefix() + "§f原神§2正在加载");
    getServer().getConsoleSender().sendMessage(prefix() + ModelManager.getInstance().getCurrentModelName() + "§6偷偷告诉你: §e" + hitokoto);
    getServer().getConsoleSender().sendMessage(prefix() + "§f哇！传送锚点已解锁，当前Model: " + ModelManager.getInstance().getCurrentModelName());

    // 初始化语言文件
    initLanguageFiles();

    // 注册监听器
    getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    getServer().getPluginManager().registerEvents(new ChatListener(this), this); 
    getServer().getPluginManager().registerEvents(new OpJoinListener(this), this);
    getServer().getPluginManager().registerEvents(new ChestUIListener(this), this);
    getServer().getPluginManager().registerEvents(new AnvilGUIListener(this), this);
    modelChoiceListener = new ModelChoiceListener(this);
    getServer().getPluginManager().registerEvents(modelChoiceListener, this);

    // 注册命令
    getCommand("lban").setExecutor(new LengbanlistCommand("lban", this));
    getCommand("ban").setExecutor(new BanCommand(this));
    getCommand("ban-ip").setExecutor(new BanIpCommand(this));
    getCommand("unban").setExecutor(new UnbanCommand(this));
    getCommand("warn").setExecutor(new WarnCommand(this));
    getCommand("unwarn").setExecutor(new UnwarnCommand(this));
    getCommand("check").setExecutor(new CheckCommand(this));
    getCommand("language").setExecutor(new LanguageCommand(this, languageManager));
    getCommand("report").setExecutor(new ReportCommand(this)); 
    getCommand("admin").setExecutor(new AdminReportCommand(this));
    getCommand("kick").setExecutor(new KickCommand(this));
    getCommand("info").setExecutor(new InfoCommand(this));
    getCommand("allowmsg").setExecutor(new AllowMsgCommand(this)); 
    getCommand("warnmsg").setExecutor(new WarnMsgCommand(this)); 

    getServer().getConsoleSender().sendMessage("§b  _                      ____              _      _     _   ");
    getServer().getConsoleSender().sendMessage("§6 | |                    |  _ \\            | |    (_)   | |  ");
    getServer().getConsoleSender().sendMessage("§b | |     ___ _ __   __ _| |_) | __ _ _ __ | |     _ ___| |_ ");
    getServer().getConsoleSender().sendMessage("§f | |    / _ \\ '_ \\ / _` |  _ < / _` | '_ \\| |    | / __| __|");
    getServer().getConsoleSender().sendMessage("§b | |___|  __/ | | | (_| | |_) | (_| | | | | |____| \\__ \\ |_ ");
    getServer().getConsoleSender().sendMessage("§6 |______\\___|_| |_|\\__,|_|___/ \\__,_|_| |_|______|_|___/\\__|");
    getServer().getConsoleSender().sendMessage("§b                   __/ |                                    ");
    getServer().getConsoleSender().sendMessage("§f                   |___/                                     ");
    getServer().getConsoleSender().sendMessage("§6当前运行版本：v" + getPluginVersion());
    getServer().getConsoleSender().sendMessage("§3当前运行在：" + Bukkit.getServer().getVersion());

    new Metrics(this, 24495);
    GitHubUpdateChecker.checkUpdate();

    if (isBroadcast) {
        task = new BroadCastBanCountMessage().runTaskTimer(Lengbanlist.getInstance(), 0L, getConfig().getInt("sendtime") * 1200L);
    }
}

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(prefix() + "§k§4正在卸载");
        if (task != null) {
            task.cancel();
        }

        // 注销所有监听器
        org.bukkit.event.HandlerList.unregisterAll(this);

        // 保存配置文件
        saveBanConfig();
        saveBanIpConfig();
        saveMuteConfig();
        saveBroadcastConfig();
        saveWarnConfig();
        reportManager.saveReports(); // 保存举报记录

        // 保存语言文件
        saveLanguageFiles();

        getServer().getConsoleSender().sendMessage(prefix() + "§f期待我们的下一次相遇！");
    }

    // 初始化语言文件
    private void initLanguageFiles() {
        File languageFolder = new File(getDataFolder(), "language");
        if (!languageFolder.exists()) {
            languageFolder.mkdirs(); // 创建 language 文件夹
        }

        // 直接写入默认语言文件内容
        writeDefaultLanguageFile("default.json", getDefaultLanguageContent());
        writeDefaultLanguageFile("en_US.json", getEnUSLanguageContent());
    }

    // 写入默认语言文件
    private void writeDefaultLanguageFile(String fileName, String content) {
        File languageFile = new File(getDataFolder(), "language/" + fileName);
        if (!languageFile.exists()) {
            try {
                languageFile.createNewFile();
                FileWriter writer = new FileWriter(languageFile);
                writer.write(content);
                writer.close();
            } catch (IOException e) {
                getLogger().warning("无法创建语言文件: " + fileName);
                e.printStackTrace();
            }
        }
    }

    // 获取默认语言文件内容
    private String getDefaultLanguageContent() {
    return "prefix: \"§7[Lengbanlist] \"\n" +
           "language_changed: \"§a语言已切换为: %language%\"\n" +
           "help_header: \"§bLengbanlist §2§o帮助信息 - 默认风格:\"\n" +
           "help_commands:\n" +
           "  list: \"§b§l/lban list - §3§o查看被封禁的名单\"\n" +
           "  broadcast: \"§b§l/lban a - §3§o广播当前封禁人数\"\n" +
           "  toggle: \"§b§l/lban toggle - §3§o开启/关闭自动广播\"\n" +
           "  model: \"§b§l/lban model <模型名称> - §3§o切换模型\"\n" +
           "  reload: \"§b§l/lban reload - §3§o重新加载配置\"\n" +
           "  add: \"§b§l/lban add <玩家名> <天数> <原因> - §3§o添加封禁\"\n" +
           "  remove: \"§b§l/lban remove <玩家名> - §3§o移除封禁\"\n" +
           "  mute: \"§b§l/lban mute <玩家名> <原因> - §3§o禁言玩家\"\n" +
           "  unmute: \"§b§l/lban unmute <玩家名> - §3§o解除禁言\"\n" +
           "  list_mute: \"§b§l/lban list-mute - §3§o查看禁言列表\"\n" +
           "  help: \"§b§l/lban help - §3§o显示帮助信息\"\n" +
           "  open: \"§b§l/lban open - §3§o打开可视化操作界面\"\n" +
           "  getIP: \"§b§l/lban getIP <玩家名> - §3§o查询玩家的 IP 地址\"\n" +
           "  ban_ip: \"§b§l/ban-ip <IP地址> <天数> <原因> - §3§o封禁 IP 地址\"\n" +
           "  unban_ip: \"§b§l/unban-ip <IP地址> - §3§o解除 IP 封禁\"\n" +
           "  warn: \"§b§l/lban warn <玩家名> <原因> - §3§o警告玩家，三次警告将自动封禁！\"\n" +
           "  unwarn: \"§b§l/lban unwarn <玩家名> - §3§o移除玩家的警告记录。\"\n" +
           "  check: \"§b§l/lban check <玩家名/IP> - §3§o检查玩家或IP的封禁状态\"\n" +
           "  report: \"§b§l/report <玩家名> <原因> - §3§o举报玩家\"\n" +
           "version_info: \"§6当前版本: %version%\"\n" +
           "hitokoto: \"§d%hitokoto%\"";
}

    // 获取英文语言文件内容
    private String getEnUSLanguageContent() {
    return "prefix: \"§7[Lengbanlist] \"\n" +
           "language_changed: \"§aLanguage changed to: %language%\"\n" +
           "help_header: \"§bLengbanlist §2§oHelp Information - Default Style:\"\n" +
           "help_commands:\n" +
           "  list: \"§b§l/lban list - §3§oView banned players\"\n" +
           "  broadcast: \"§b§l/lban a - §3§oBroadcast current ban count\"\n" +
           "  toggle: \"§b§l/lban toggle - §3§oToggle automatic broadcast\"\n" +
           "  model: \"§b§l/lban model <model name> - §3§oSwitch model\"\n" +
           "  reload: \"§b§l/lban reload - §3§oReload configuration\"\n" +
           "  add: \"§b§l/lban add <player> <days> <reason> - §3§oAdd a ban\"\n" +
           "  remove: \"§b§l/lban remove <player> - §3§oRemove a ban\"\n" +
           "  mute: \"§b§l/lban mute <player> <reason> - §3§oMute a player\"\n" +
           "  unmute: \"§b§l/lban unmute <player> - §3§oUnmute a player\"\n" +
           "  list_mute: \"§b§l/lban list-mute - §3§oView muted players\"\n" +
           "  help: \"§b§l/lban help - §3§oShow help information\"\n" +
           "  open: \"§b§l/lban open - §3§oOpen the visual interface\"\n" +
           "  getIP: \"§b§l/lban getIP <player> - §3§oGet player's IP address\"\n" +
           "  ban_ip: \"§b§l/ban-ip <IP address> <days> <reason> - §3§oBan an IP address\"\n" +
           "  unban_ip: \"§b§l/unban-ip <IP address> - §3§oUnban an IP address\"\n" +
           "  warn: \"§b§l/lban warn <player> <reason> - §3§oWarn a player (3 warnings will auto-ban)\"\n" +
           "  unwarn: \"§b§l/lban unwarn <player> - §3§oRemove a warning\"\n" +
           "  check: \"§b§l/lban check <player/IP> - §3§oCheck ban status\"\n" +
           "  report: \"§b§l/report <player> <reason> - §3§oReport a player\"\n" +
           "version_info: \"§6Current version: %version%\"\n" +
           "hitokoto: \"§d%hitokoto%\"";
}

    // 保存语言文件
    private void saveLanguageFiles() {
        File languageFolder = new File(getDataFolder(), "language");
        if (languageFolder.exists()) {
            for (File languageFile : languageFolder.listFiles()) {
                if (languageFile.getName().endsWith(".yml")) {
                    FileConfiguration config = YamlConfiguration.loadConfiguration(languageFile);
                    try {
                        config.save(languageFile); // 保存文件
                    } catch (IOException e) {
                        getLogger().warning("无法保存语言文件: " + languageFile.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String prefix() {
        return getConfig().getString("prefix");
    }

    public static Lengbanlist getInstance() {
        return instance;
    }

    public static CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commandMap;
    }

    public boolean isBroadcastEnabled() {
        return isBroadcast;
    }

    public void setBroadcastEnabled(boolean broadcastEnabled) {
        this.isBroadcast = broadcastEnabled;
        if (isBroadcast) {
            task = new BroadCastBanCountMessage().runTaskTimer(Lengbanlist.getInstance(), 0L, getConfig().getInt("sendtime") * 1200L);
        } else {
            if (task != null) {
                task.cancel();
            }
        }
    }

    public String toggleBroadcast() {
        setBroadcastEnabled(!isBroadcastEnabled());
        return isBroadcastEnabled() ? "§a已开启" : "§c已关闭";
    }

    public ModelManager getModelManager() {
        return ModelManager.getInstance();
    }

    public String getPluginVersion() {
        return getDescription().getVersion();
    }

    public BanManager getBanManager() {
        return banManager;
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    public WarnManager getWarnManager() {
        return warnManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public ModelChoiceListener getModelChoiceListener() {
        return modelChoiceListener;
    }

    public FileConfiguration getBanFC() {
        return banFC;
    }

    public FileConfiguration getBanIpFC() {
        return banIpFC;
    }

    public FileConfiguration getMuteFC() {
        return muteFC;
    }

    public FileConfiguration getBroadcastFC() {
        return broadcastFC;
    }

    public FileConfiguration getWarnFC() {
        return warnFC;
    }

    public FileConfiguration getIpFC() {
        return ipFC;
    }

    public FileConfiguration getReportFC() {
        return reportFC;
    }

    public void saveBanConfig() {
        try {
            banFC.save(new File(getDataFolder(), "ban-list.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBanIpConfig() {
        try {
            banIpFC.save(new File(getDataFolder(), "banip-list.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMuteConfig() {
        try {
            muteFC.save(new File(getDataFolder(), "mute-list.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBroadcastConfig() {
        try {
            broadcastFC.save(new File(getDataFolder(), "broadcast.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWarnConfig() {
        try {
            warnFC.save(new File(getDataFolder(), "warn-list.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChestUIListener getChestUIListener() {
        return new ChestUIListener(this);
    }

    // 获取一言的方法
    public String getHitokoto() {
        try {
            URL url = new URL("https://v1.hitokoto.cn/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                getLogger().warning("一言 API 请求失败，状态码: " + responseCode);
                return "§c无法获取一言";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 解析 JSON 响应
            String jsonResponse = response.toString();
            String hitokoto = jsonResponse.split("\"hitokoto\":\"")[1].split("\"")[0];
            String from = jsonResponse.split("\"from\":\"")[1].split("\"")[0];
            return hitokoto + " —— " + from;
        } catch (Exception e) {
            getLogger().warning("获取一言时出错: " + e.getMessage());
            return "§c无法获取一言";
        }
    }

    // 添加 getLanguageManager 方法
    public LanguageManager getLanguageManager() {
        return languageManager;
    }
    public FileConfiguration getChatConfig() {
    return chatConfig;
}
}