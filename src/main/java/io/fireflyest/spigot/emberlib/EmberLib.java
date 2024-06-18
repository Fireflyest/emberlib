package io.fireflyest.spigot.emberlib;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public final class EmberLib extends JavaPlugin {

    public static EmberLib getPlugin() {
        return getPlugin(EmberLib.class);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.getLogger().info("enable!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.getLogger().info("disable!");
    }

}
