package io.fireflyest.emberlib.message;

import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 用血条消息通知
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class BossBarMessage {
    
    private final JavaPlugin plugin;
    private final BossBar bar;

    public BossBarMessage(JavaPlugin plugin, BossBar bar) {
        this.plugin = plugin;
        this.bar = bar;
    }

}
