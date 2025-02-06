package io.fireflyest.emberlib;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.fireflyest.emberlib.inventory.TestView;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.inventory.core.ViewGuideImpl;
import io.fireflyest.emberlib.message.Notification;
import io.fireflyest.emberlib.message.NotificationImpl;
import io.fireflyest.emberlib.util.ChatUtils;

/**
 * Hello world!
 *
 */
public final class EmberLib extends JavaPlugin {

    private Server server;
    private ViewGuideImpl guide;

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
        Print.EMBER_LIB.info("   ____      __           __   _ __ ");
        Print.EMBER_LIB.info("  / __/_ _  / /  ___ ____/ /  (_) / ");
        Print.EMBER_LIB.info(" / _//  ' \\/ _ \\/ -_) __/ /__/ / _ \\");
        Print.EMBER_LIB.info("/___/_/_/_/_.__/\\__/_/ /____/_/_.__/");

        Player player = Bukkit.getPlayer("Fireflyest");
        Notification notification = new NotificationImpl(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player == null) {
                    return;
                }
                ItemStack itemStack = Bukkit.getItemFactory().createItemStack("minecraft:diamond_sword{Enchantments:[{id:\"minecraft:sharpness\", lvl:3}]}");
                notification.sendOne(ChatUtils.textItemStack(itemStack, null), player);

                notification.sendOne(ChatUtils.textEntity(player, null), player);
            }
        }.runTaskLater(this, 100);

        server = this.getServer();

        Print.EMBER_LIB.info("Registering service of guide.");
        Print.EMBER_LIB.onDebug();
        Print.VIEW_GUIDE.onDebug();
        guide = new ViewGuideImpl(this);
        server.getPluginManager().registerEvents(guide, this);
        server.getServicesManager().register(ViewGuide.class, guide, this, ServicePriority.Normal);

        guide.addView("test", new TestView());
        if (player != null) {
            guide.openView(player, "test", "aaa");
        }
    }

    @Override
    public void onDisable() {
        guide.disable();
        server.getServicesManager().unregisterAll(this);
    }

}
