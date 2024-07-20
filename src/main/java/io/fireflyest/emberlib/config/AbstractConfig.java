package io.fireflyest.emberlib.config;

import javax.annotation.Nonnull;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 配置抽象类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public abstract class AbstractConfig {

    /**
     * 抽象配置类构造函数
     */
    protected AbstractConfig() {
        
    }

    /**
     * 加载数据到类
     * 
     * @param plugin 插件
     */
    public static void loadFromYaml(@Nonnull JavaPlugin plugin) {

    }

}
