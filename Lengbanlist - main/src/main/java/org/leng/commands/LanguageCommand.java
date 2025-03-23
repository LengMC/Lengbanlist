package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.Lengbanlist;
import org.leng.utils.LanguageManager;
import org.leng.utils.Utils;

public class LanguageCommand extends Command implements CommandExecutor {
    private final Lengbanlist plugin;
    private final LanguageManager languageManager;

    public LanguageCommand(Lengbanlist plugin, LanguageManager languageManager) {
        super("language");
        this.plugin = plugin;
        this.languageManager = languageManager;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        // 检查权限
        if (!sender.hasPermission("lengbanlist.language")) {
            Utils.sendMessage(sender, plugin.prefix() + "§c你没有权限使用此命令。");
            return true;
        }

        // 检查是否是玩家
        if (!(sender instanceof Player)) {
            Utils.sendMessage(sender, plugin.prefix() + "§c此命令只能由玩家执行。");
            return true;
        }

        Player player = (Player) sender;

        // 打开语言选择界面
        languageManager.openLanguageSelectionUI(player);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, label, args);
    }
}