package io.fireflyest.emberlib;

import org.bukkit.plugin.java.JavaPlugin;

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
    }

    @Override
    public void onDisable() {
        
    }

}
