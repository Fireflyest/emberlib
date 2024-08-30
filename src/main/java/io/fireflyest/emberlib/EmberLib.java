package io.fireflyest.emberlib;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.fireflyest.emberlib.message.Notification;
import io.fireflyest.emberlib.message.NotificationImpl;
import io.fireflyest.emberlib.util.ChatUtils;

/**
 * Hello world!
 *
 */
public final class EmberLib extends JavaPlugin {

    /**
     * 获取这个插件
     * 
     * @return 插件
     */
    public static EmberLib getPlugin() {
        return getPlugin(EmberLib.class);
    }

    @Override
    public void onEnable() {
        Print.EMBER_LIB.info("enable");

        Notification notification = new NotificationImpl(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack itemStack = Bukkit.getItemFactory().createItemStack("minecraft:diamond_sword{Enchantments:[{id:\"minecraft:sharpness\", lvl:3}]}");
                notification.sendOne(ChatUtils.textItemStack(itemStack, null), Bukkit.getPlayer("Fireflyest"));

                notification.sendOne(ChatUtils.textEntity(Bukkit.getPlayer("Fireflyest"), null), Bukkit.getPlayer("Fireflyest"));
            }
        }.runTaskLater(this, 100);
    }

    @Override
    public void onDisable() {
        
    }

}
