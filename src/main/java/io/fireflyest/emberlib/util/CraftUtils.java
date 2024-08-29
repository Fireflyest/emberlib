package io.fireflyest.emberlib.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 服务端核心
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class CraftUtils {
    
    /**
     * 版本号
     * E.g. {@code 1_17_R1}
     */
    public static final String VERSION;

    static {
        // 遍历所有包找到对应版本路径，以此获取版本号
        String found = null;
        for (Package pack : Package.getPackages()) {
            final String name = pack.getName();
            if (name.startsWith("org.bukkit.craftbukkit.v")) {
                found = pack.getName().split("\\.")[3];
                try {
                    Class.forName("org.bukkit.craftbukkit." + found + ".entity.CraftPlayer");
                    break;
                } catch (ClassNotFoundException e) {
                    found = null;
                }
            }
        }
        if (found == null) {
            throw new IllegalArgumentException("Failed to parse server version.");
        }
        VERSION = found;
    }

    /**
     * 版本号
     * E.g. {@code v1_17_R1} to {@code 17}
     *
     * @since 4.0.0
     */
    public static final int VER = Integer.parseInt(VERSION.substring(1).split("_")[1]);

    /**
     * 类路径
     */
    public static final String CRAFTBUKKIT = "org.bukkit.craftbukkit." + VERSION + '.';

    private CraftUtils() {

    }

    /**
     * 获取各种接口的实现类
     *
     * @param name the name of the class to load.
     *
     * @return the CraftBukkit class or null if not found.
     * @since 1.0
     */
    @Nullable
    public static Class<?> getCraftClass(@Nonnull String name) {
        try {
            return Class.forName(CRAFTBUKKIT + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
