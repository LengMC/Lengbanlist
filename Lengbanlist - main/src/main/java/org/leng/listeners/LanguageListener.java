package org.leng.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.leng.Lengbanlist;
import org.leng.manager.ModelManager;
import org.leng.utils.LanguageManager;

public class LanguageListener implements Listener {
    private final LanguageManager languageManager;
    private final ModelManager modelManager;

    public LanguageListener(LanguageManager languageManager, ModelManager modelManager) {
        this.languageManager = languageManager;
        this.modelManager = modelManager;
    }

  @EventHandler
public void onLanguageSelect(InventoryClickEvent event) {
    // 检查界面标题是否是语言选择界面
    if (!event.getView().getTitle().startsWith(Lengbanlist.getInstance().prefix() + "语言选择")) {
        return;
    }

    event.setCancelled(true); // 取消点击事件

    // 检查点击的物品是否有效
    ItemStack clickedItem = event.getCurrentItem();
    if (clickedItem == null || !(clickedItem.getItemMeta() instanceof SkullMeta)) {
        return;
    }

    // 获取点击的玩家
    Player player = (Player) event.getWhoClicked();

    // 获取语言代码
    SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
    String languageCode = meta.getDisplayName().replace("§6", "").trim();

    // 切换语言
    languageManager.switchLanguage(player, languageCode);

    // 根据语言启用或禁用 ModelManager
    if (languageCode.equals("default")) {
        modelManager.setEnabled(true);
        player.sendMessage(Lengbanlist.getInstance().prefix() + "§a已启用模型切换功能。");
    } else {
        modelManager.setEnabled(false);
        player.sendMessage(Lengbanlist.getInstance().prefix() + "§a已禁用模型切换功能，切换到 JSON 文件显示帮助信息。");
    }
}
}