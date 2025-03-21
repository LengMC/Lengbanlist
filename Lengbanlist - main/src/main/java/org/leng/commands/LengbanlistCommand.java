package org.leng.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.leng.Lengbanlist;
import org.leng.object.BanEntry;
import org.leng.object.BanIpEntry;
import org.leng.object.MuteEntry;
import org.leng.manager.ModelManager;
import org.leng.models.Model;
import org.leng.utils.TimeUtils;
import org.leng.utils.Utils;
import org.leng.utils.SaveIP;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class LengbanlistCommand extends Command implements CommandExecutor, Listener {
    private final Lengbanlist plugin;

    public LengbanlistCommand(String name, Lengbanlist plugin) {
        super(name);
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Model currentModel = ModelManager.getInstance().getCurrentModel();
        if (args.length == 0) {
            currentModel.showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "toggle":
                if (!sender.hasPermission("lengbanlist.toggle")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                boolean enabled = !plugin.isBroadcastEnabled();
                plugin.setBroadcastEnabled(enabled);
                Utils.sendMessage(sender, currentModel.toggleBroadcast(enabled));
                break;
            case "a":
    if (!sender.hasPermission("lengbanlist.broadcast")) {
        Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
        return true;
    }
    String defaultMessage = plugin.getBroadcastFC().getString("default-message");
    int banCount = plugin.getBanManager().getBanList().size();
    int banIpCount = plugin.getBanManager().getBanIpList().size();

    // 计算总封禁数量
    int totalBans = banCount + banIpCount;

    // 替换占位符
    String replacedMessage = defaultMessage
            .replace("%s", String.valueOf(banCount)) // 替换封禁玩家数量
            .replace("%i", String.valueOf(banIpCount)) // 替换封禁 IP 数量
            .replace("%t", String.valueOf(totalBans)); // 替换总封禁数量

    plugin.getServer().broadcastMessage(replacedMessage);
    break;
            case "list":
                if (!sender.hasPermission("lengbanlist.list")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                showBanList(sender);
                break;
            case "reload":
                if (!sender.hasPermission("lengbanlist.reload")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                plugin.reloadConfig();
                ModelManager.getInstance().reloadModel();
                File broadcastFile = new File(plugin.getDataFolder(), "broadcast.yml");
                plugin.getServer().broadcastMessage(plugin.getConfig().getString("default-message").replace("%s", String.valueOf(plugin.getBanManager().getBanList().size())));
                Utils.sendMessage(sender, currentModel.reloadConfig());
                break;
            case "add":
                if (!sender.hasPermission("lengbanlist.ban")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                if (args.length < 4) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban add <玩家名/IP> <时间> <原因>");
                    return true;
                }
                if (args[1].contains(".")) {
                    plugin.getBanManager().banIp(new BanIpEntry(args[1], sender.getName(), TimeUtils.generateTimestampFromDays(Integer.parseInt(args[2])), args[3]));
                    Utils.sendMessage(sender, currentModel.addBanIp(args[1], Integer.parseInt(args[2]), args[3]));
                } else {
                    plugin.getBanManager().banPlayer(new BanEntry(args[1], sender.getName(), TimeUtils.generateTimestampFromDays(Integer.parseInt(args[2])), args[3]));
                    Utils.sendMessage(sender, currentModel.addBan(args[1], Integer.parseInt(args[2]), args[3]));
                }
                break;
            case "remove":
                if (!sender.hasPermission("lengbanlist.unban")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban remove <玩家名/IP>");
                    return true;
                }
                if (args[1].contains(".")) {
                    plugin.getBanManager().unbanIp(args[1]);
                    Utils.sendMessage(sender, currentModel.removeBanIp(args[1]));
                } else {
                    plugin.getBanManager().unbanPlayer(args[1]);
                    Utils.sendMessage(sender, currentModel.removeBan(args[1]));
                }
                break;
            case "help":
                if (!sender.hasPermission("lengbanlist.help")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                currentModel.showHelp(sender);
                break;
            case "open":
                if (!sender.hasPermission("lengbanlist.open")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    openChestUI(player);
                } else {
                    Utils.sendMessage(sender, plugin.prefix() + "§c此命令只能由玩家执行。");
                }
                break;
            case "getip":
                if (!sender.hasPermission("lengbanlist.getIP")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式 /lban getip <玩家名>");
                    return false;
                }
                String target = args[1];
                String ip = SaveIP.getIP(target);
                if (ip == null) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l查询不到玩家 " + target + " 的 IP 地址");
                } else {
                    String location = getIPLocation(ip);
                    if (location != null) {
                        Utils.sendMessage(sender, plugin.prefix() + "§a查询到玩家 " + target + " 的 IP 地址为 " + ip + "，地理位置：" + location);
                    } else {
                        Utils.sendMessage(sender, plugin.prefix() + "§a查询到玩家 " + target + " 的 IP 地址为 " + ip + "，但无法解析地理位置");
                    }
                }
                break;
            case "model":
                if (!sender.hasPermission("lengbanlist.model")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式/lban model <模型名称>");
                    StringBuilder availableModels = new StringBuilder("§6§l可用模型： §b");
                    for (String modelName : ModelManager.getInstance().getModels().keySet()) {
                        availableModels.append(modelName).append(" ");
                    }
                    Utils.sendMessage(sender, availableModels.toString());
                    return true;
                }
                String modelName = args[1].toLowerCase();
                boolean found = false;
                for (String name : ModelManager.getInstance().getModels().keySet()) {
                    if (name.equalsIgnoreCase(modelName)) {
                        ModelManager.switchModel(name);
                        Utils.sendMessage(sender, plugin.prefix() + "§a已切换到模型: " + name);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不支持的模型名称。");
                    StringBuilder availableModels = new StringBuilder("§6§l可用模型： §b");
                    for (String name : ModelManager.getInstance().getModels().keySet()) {
                        availableModels.append(name).append(" ");
                    }
                    Utils.sendMessage(sender, availableModels.toString());
                }
                break;
            case "mute":
                if (!sender.hasPermission("lengbanlist.mute")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                if (args.length < 3) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式 /lban mute <玩家名> <原因>");
                    return true;
                }
                String muteTarget = args[1];
                String muteReason = args[2];
                MuteEntry muteEntry = new MuteEntry(muteTarget, sender.getName(), System.currentTimeMillis(), muteReason);
                plugin.getMuteManager().mutePlayer(muteEntry);
                Utils.sendMessage(sender, currentModel.addMute(muteTarget, muteReason));
                break;
            case "unmute":
                if (!sender.hasPermission("lengbanlist.mute")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式 /lban unmute <玩家名>");
                    return true;
                }
                String unmuteTarget = args[1];
                plugin.getMuteManager().unmutePlayer(unmuteTarget);
                Utils.sendMessage(sender, currentModel.removeMute(unmuteTarget));
                break;
            case "list-mute":
                if (!sender.hasPermission("lengbanlist.listmute")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                showMuteList(sender);
                break;
            case "warn":
                if (!sender.hasPermission("lengbanlist.warn")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                if (args.length < 3) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式：/lban warn <玩家名> <原因>");
                    return true;
                }
                String warnTarget = args[1];
                String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                // 修复 warnPlayer 方法的调用，传递正确的参数
                plugin.getWarnManager().warnPlayer(warnTarget, sender.getName(), System.currentTimeMillis(), reason);
                Utils.sendMessage(sender, currentModel.addWarn(warnTarget, reason));
                break;
            case "unwarn":
                if (!sender.hasPermission("lengbanlist.unwarn")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式：/lban unwarn <玩家名>");
                    return true;
                }
                String unwarnTarget = args[1];
                plugin.getWarnManager().unwarnPlayer(unwarnTarget);
                Utils.sendMessage(sender, currentModel.removeWarn(unwarnTarget));
                break;
            default:
                Utils.sendMessage(sender, plugin.prefix() + "§c你说的啥啊喵？喵喵看不懂~");
                break;
            case "check":
                if (!sender.hasPermission("lengbanlist.check")) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c不是你的工作喵！");
                    return true;
                }
                if (args.length < 2) {
                    Utils.sendMessage(sender, plugin.prefix() + "§c§l错误的命令格式，正确格式：/lban check <玩家名/IP>");
                    return true;
                }
                String checkTarget = args[1];
                CheckCommand checkCommand = new CheckCommand(plugin);
                checkCommand.execute(sender, "check", new String[]{checkTarget});
                break;
            case "language":
    if (!sender.hasPermission("lengbanlist.language")) {
        Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
        return true;
    }
    if (sender instanceof Player) {
        Player player = (Player) sender;
        // 打开语言选择界面
        plugin.getLanguageManager().openLanguageSelectionUI(player);
    } else {
        Utils.sendMessage(sender, plugin.prefix() + "§c此命令只能由玩家执行。");
    }
    break;
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, label, args);
    }

    private void showBanList(CommandSender sender) {
        Utils.sendMessage(sender, "§7--§bLengbanlist 封禁名单§7--");
        for (BanEntry entry : plugin.getBanManager().getBanList()) {
            Utils.sendMessage(sender, "§9§o被封禁者: " + entry.getTarget() + " §6处理人: " + entry.getStaff() + " §d封禁时间: " + TimeUtils.timestampToReadable(entry.getTime()) + " §l§n封禁原因: " + entry.getReason());
        }
        for (BanIpEntry entry : plugin.getBanManager().getBanIpList()) {
            Utils.sendMessage(sender, "§9§o被封禁 IP: " + entry.getIp() + " §6处理人: " + entry.getStaff() + " §d封禁时间: " + TimeUtils.timestampToReadable(entry.getTime()) + " §l§n封禁原因: " + entry.getReason());
        }
    }

    private void showMuteList(CommandSender sender) {
        Utils.sendMessage(sender, "§7--§bLengbanlist 禁言名单§7--");
        for (MuteEntry entry : plugin.getMuteManager().getMuteList()) {
            Utils.sendMessage(sender, "§9§o被禁言者: " + entry.getTarget() + " §6处理人: " + entry.getStaff() + " §d禁言时间: " + TimeUtils.timestampToReadable(entry.getTime()) + " §l§n禁言原因: " + entry.getReason());
        }
    }

private void openChestUI(Player player) {
        Inventory chest = Bukkit.createInventory(null, 54, "§bLengbanlist");

    // 设置背景玻璃板
    ItemStack glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
    ItemMeta glassMeta = glass.getItemMeta();
    glassMeta.setDisplayName("§7我只是个装饰物");
    glass.setItemMeta(glassMeta);
    for (int i = 0; i < 54; i++) {
        if (i < 9 || i >= 45 || i % 9 == 0 || i % 9 == 8) {
            chest.setItem(i, glass);
        }
    }

    // 添加功能按钮
    ItemStack toggleBroadcast = createItem(
            "§a切换自动广播 (" + (plugin.isBroadcastEnabled() ? "开启" : "关闭") + ")",
            "§7/lban toggle",
            "§7开启或关闭自动广播",
            Sound.BLOCK_LEVER_CLICK
    );
    ItemStack broadcast = createItem("§a广播封禁人数", "§7/lban a", "§7广播当前封禁人数", Sound.BLOCK_NOTE_BLOCK_PLING);
    ItemStack list = createItem("§a查看封禁名单", "§7/lban list", "§7查看被封禁的玩家列表", Sound.BLOCK_NOTE_BLOCK_HARP);
    ItemStack reload = createItem("§a重新加载配置", "§7/lban reload", "§7重新加载插件配置", Sound.BLOCK_NOTE_BLOCK_BELL);
    ItemStack addBan = createItem("§a添加封禁", "§7/lban add", "§7添加一个玩家到封禁名单", Sound.BLOCK_NOTE_BLOCK_BASS);
    ItemStack removeBan = createItem("§a解除封禁", "§7/lban remove", "§7从封禁名单中移除一个玩家", Sound.BLOCK_NOTE_BLOCK_SNARE);
    ItemStack help = createItem("§a帮助信息", "§7/lban help", "§7显示帮助信息", Sound.BLOCK_NOTE_BLOCK_FLUTE);
    ItemStack model = createItem(
            "§a切换模型 (" + ModelManager.getInstance().getCurrentModelName() + ")",
            "§7/lban model",
            "§7当前模型: " + ModelManager.getInstance().getCurrentModelName(),
            Sound.BLOCK_NOTE_BLOCK_CHIME
    );
    ItemStack sponsor = createItem("§6赞助作者", "§7点击打开赞助链接", "§7https://afdian.com/a/lengbanlist", Sound.BLOCK_NOTE_BLOCK_PLING);
    ItemStack mute = createItem("§a禁言玩家", "§7/lban mute", "§7禁言一个玩家", Sound.BLOCK_NOTE_BLOCK_BASS);
    ItemStack unmute = createItem("§a解除禁言", "§7/lban unmute", "§7解除一个玩家的禁言", Sound.BLOCK_NOTE_BLOCK_SNARE);
    ItemStack listMute = createItem("§a查看禁言列表", "§7/lban list-mute", "§7查看被禁言的玩家列表", Sound.BLOCK_NOTE_BLOCK_HARP);
    ItemStack language = createItem("§a语言选择", "§7/lban language", "§7选择语言", Sound.BLOCK_NOTE_BLOCK_PLING);

    // 设置按钮位置
    chest.setItem(10, toggleBroadcast);
    chest.setItem(12, broadcast);
    chest.setItem(14, list);
    chest.setItem(16, reload);
    chest.setItem(20, addBan);
    chest.setItem(22, removeBan);
    chest.setItem(24, help);
    chest.setItem(28, model);
    chest.setItem(30, mute);
    chest.setItem(32, unmute);
    chest.setItem(34, listMute);
    chest.setItem(40, sponsor);
    chest.setItem(42, language); 

    player.openInventory(chest);
}

    private ItemStack createItem(String displayName, String command, String description, Sound sound) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        List<String> lore = new ArrayList<>();
        lore.add(command);
        lore.add(description);
        meta.setLore(lore);
        item.setItemMeta(meta);

        if (sound != null) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Player player = Bukkit.getPlayer(meta.getDisplayName());
                if (player != null) {
                    player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                }
            }, 1L);
        }

        return item;
    }

/**
 * 调用 API 解析 IP 地址的地理位置
 *
 * @param ip 需要解析的 IP 地址
 * @return 解析后的地理位置信息，如果解析失败则返回 null
 */
private String getIPLocation(String ip) {
    try {
        String apiUrl = "https://ipapi.co/" + ip + "/json/"; // 使用更可靠的API
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // 添加User-Agent避免403

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            plugin.getLogger().warning("IP API请求失败，状态码: " + responseCode);
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        String jsonResponse = response.toString();
        // 使用更健壮的解析逻辑
        if (jsonResponse.contains("\"country_name\"")) {
            String country = jsonResponse.split("\"country_name\":\"")[1].split("\"")[0];
            String region = jsonResponse.split("\"region\":\"")[1].split("\"")[0];
            String city = jsonResponse.split("\"city\":\"")[1].split("\"")[0];
            return country + ", " + region + ", " + city;
        } else {
            plugin.getLogger().warning("API响应格式异常: " + jsonResponse);
            return null;
        }
    } catch (Exception e) {
        plugin.getLogger().warning("解析IP地理位置时出错: " + e.getMessage());
        return null;
    }
}

@EventHandler
public void onInventoryClick(InventoryClickEvent event) {
    if (event.getView().getTitle().equals("§bLengbanlist")) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        String command = clickedItem.getItemMeta().getLore().get(0).replace("§7", "");

        switch (command) {
            case "/lban toggle":
                player.performCommand("lban toggle");
                break;
            case "/lban a":
                player.performCommand("lban a");
                break;
            case "/lban list":
                player.performCommand("lban list");
                break;
            case "/lban reload":
                player.performCommand("lban reload");
                break;
            case "/lban add":
                plugin.getChestUIListener().openAnvilForBan(player, "playerID");
                break;
            case "/lban remove":
                plugin.getChestUIListener().openAnvilForUnban(player);
                break;
            case "/lban help":
                player.performCommand("lban help");
                break;
            case "/lban model":
                ModelManager.getInstance().openModelSelectionUI(player);
                break;
            case "/lban mute":
                plugin.getChestUIListener().openAnvilForMute(player, "playerID");
                break;
            case "/lban unmute":
                plugin.getChestUIListener().openAnvilForUnmute(player);
                break;
            case "/lban list-mute":
                player.performCommand("lban list-mute");
                break;
            default:
                player.performCommand(command);
                break;
        }
    }

    // 处理语言选择界面的点击事件
    if (event.getView().getTitle().equals("§a选择语言")) {
        event.setCancelled(true); // 取消点击事件，防止玩家移动物品

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        // 获取点击的物品的语言代码
        String languageCode = clickedItem.getItemMeta().getLore().get(1).replace("§7语言代码: ", "");

        // 切换语言
        plugin.getLanguageManager().switchLanguage(player, languageCode);

        // 关闭界面
        player.closeInventory();
    }
}
}