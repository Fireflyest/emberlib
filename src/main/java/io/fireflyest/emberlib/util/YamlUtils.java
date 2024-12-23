package io.fireflyest.emberlib.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import io.fireflyest.emberlib.Print;
import io.fireflyest.emberlib.config.annotation.Entry;
import io.fireflyest.emberlib.config.annotation.Yaml;
import io.fireflyest.emberlib.data.Box;

/**
 * 配置文件工具类
 * @author Fireflyest
 * @since 1.0
 */
public final class YamlUtils {
    
    /**
     * 数据存储键
     */
    public static final String DATA_PATH = "data";

    /**
     * 配置文件编辑器
     */
    private static final YamlConfiguration yaml = new YamlConfiguration();

    private static Method get = null;
    private static Method set = null;

    static {
        try {
            get = Box.class.getDeclaredMethod("get");
            set = Box.class.getDeclaredMethod("set", Object.class);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private YamlUtils() {
        //
    }

    /**
     * 序列化
     * @param configurationSerializable 可序列化对象
     * @return 文本数据
     */
    public static String serialize(ConfigurationSerializable configurationSerializable) {
        yaml.set(DATA_PATH, configurationSerializable);
        return yaml.saveToString();
    }

    /**
     * 反序列化
     * @param <T> 可序列化泛型
     * @param data 文本数据
     * @param clazz 可序列化对象的类
     * @return 可序列化对象
     */
    public static <T extends ConfigurationSerializable> T deserialize(String data, Class<T> clazz) {
        try {
            yaml.loadFromString(data);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return yaml.getSerializable(DATA_PATH, clazz);
    }

    /**
     * 序列化物品
     * @param itemStack 物品
     * @return 文本数据
     */
    public static String serializeItemStack(ItemStack itemStack) {
        return serialize(itemStack);
    }

    /**
     * 反序列化物品
     * @param stackData 物品数据
     * @return 物品
     */
    public static ItemStack deserializeItemStack(String stackData) {
        return deserialize(stackData, ItemStack.class);
    }

    /**
     * 获取配置文件
     * @param plugin 插件
     * @return 配置文件
     */
    public static FileConfiguration getConfig(@Nonnull JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        return plugin.getConfig();
    }

    /**
     * 更新配置的值
     * @param plugin 插件
     * @param key 键
     * @param obj 值
     */
    public static void setConfigData(@Nonnull JavaPlugin plugin, @Nonnull String key, Object obj) {
        plugin.getConfig().set(key, obj);
        plugin.saveConfig();
    }

    /**
     * 加载配置文件
     * @param plugin 插件
     * @param fileName 子路径
     * @return 配置文件
     */
    public static FileConfiguration loadYaml(@Nonnull JavaPlugin plugin, @Nonnull String fileName) {
        final File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 把数据加载到类
     * 
     * @param plugin 插件
     * @param theClass 配置类
     * @param fileName 配置文件名
     */
    public static void loadToClass(@Nonnull JavaPlugin plugin, @Nonnull Class<?> theClass, 
            @Nullable String fileName) {
        String yamlFileName = fileName;
        if (yamlFileName == null) {
            final Yaml yaml = theClass.getAnnotation(Yaml.class);
            if (yaml == null) {
                return;
            }
            yamlFileName = yaml.value();
        }
        final FileConfiguration yamlFile = loadYaml(plugin, yamlFileName);
        boolean saveYamlFile = false;
        Print.EMBER_LIB.debug("Plugin {} loading config file {} to class {}!", 
            plugin.getName(),
            yamlFileName, 
            theClass.getSimpleName()
        );
        for (Field field : theClass.getDeclaredFields()) {
            final Entry entry = field.getAnnotation(Entry.class);
            if (entry == null) { // 没注释的不管
                continue;
            }
            final String key = "".equals(entry.value()) 
                ? defaultKey(field.getName()) : entry.value();
            final Object value = yamlFile.get(key);
            try {
                if (value == null || value instanceof MemorySection) {
                    saveYamlFile = true;
                    yamlFile.set(key, get.invoke(field.get(null)));
                } else {
                    set.invoke(field.get(null), value);
                }
            } catch (SecurityException | IllegalAccessException 
                                       | IllegalArgumentException 
                                       | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        // 保存文件
        if (saveYamlFile) {
            saveYaml(plugin, yamlFile, yamlFileName);
        }
    }

    /**
     * 保存数据到yaml文件
     * 
     * @param plugin 插件
     * @param yamlFile 配置文件
     * @param fileName 文件名
     */
    public static void saveYaml(@Nonnull JavaPlugin plugin, @Nonnull FileConfiguration yamlFile, 
            @Nonnull String fileName) {
        try {
            yamlFile.save(new File(plugin.getDataFolder(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取变量的默认生成键
     * 
     * @param fieldName 变量元素
     * @return 默认生成键
     */
    public static String defaultKey(String fieldName) {
        return TextUtils.symbolSplit(
            fieldName.toLowerCase(), "."
        );
    }

}
