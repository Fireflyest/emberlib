package io.fireflyest.emberlib.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.Print;

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
        // 如果没有找到，可能是paper端
        VERSION = found == null ? "v1_0_R0" : found;
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
    public static final String SPIGOT_CRAFTBUKKIT_PATH = "org.bukkit.craftbukkit." + VERSION + '.';

    private static final Field STACK_HANDLE;
    private static final Method AS_NMS_COPY;
    private static final Method AS_CRAFT_COPY;

    private static final Method GET_TAG_CLONE;

    static {
        Class<?> mcStackClass = null;

        Field handle = null;
        Method asNmsCopy = null;
        Method asCraftCopy = null;

        Method getTagClone = null;
        try {
            // 获取物品nbt
            mcStackClass = Class.forName("net.minecraft.world.item.ItemStack");
            if (mcStackClass != null) {
                getTagClone = mcStackClass.getDeclaredMethod("getTagClone");
                ReflectionUtils.makeAccessible(getTagClone);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            //
        }
        try {
            final Class<?> craftStackClass = getCraftClass("inventory.CraftItemStack");
            if (craftStackClass != null) {
                handle = craftStackClass.getDeclaredField("handle");
                ReflectionUtils.makeAccessible(handle);
                asNmsCopy = craftStackClass.getDeclaredMethod("asNMSCopy", ItemStack.class);
                ReflectionUtils.makeAccessible(asNmsCopy);
                asCraftCopy = craftStackClass.getDeclaredMethod("asCraftCopy", ItemStack.class);
                ReflectionUtils.makeAccessible(asCraftCopy);
            }
        } catch (NoSuchFieldException | NoSuchMethodException | SecurityException e) {
            //
        }
        STACK_HANDLE = handle;
        AS_NMS_COPY = asNmsCopy;
        AS_CRAFT_COPY = asCraftCopy;

        GET_TAG_CLONE = getTagClone;
    }

    private CraftUtils() {

    }

    /**
     * 获取各种接口的实现类
     *
     * @param name the name of the class to load.
     * @return the CraftBukkit class or null if not found.
     */
    @Nullable
    public static Class<?> getCraftClass(@Nonnull String name) {
        try {
            return Class.forName(SPIGOT_CRAFTBUKKIT_PATH + name);
        } catch (ClassNotFoundException e) {
            Print.EMBER_LIB.debug("Craft class not found: " + name);
        }
        return null;
    }

    /**
     * Gets the nms handle ItemStack from a CraftItemStack. Passing Spigot
     * ItemStacks will cause an error!
     * 
     * @param item 物品
     * @return 物品处理
     */
    @Nullable
    public static Object getCraftItemHandle(@Nonnull Object item) {
        try {
            if (STACK_HANDLE != null) {
                return STACK_HANDLE.get(item);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            // ignore
        }
        return null;
    }

    /**
     * ItemStack转为nms
     * 
     * @param item 物品
     * @return nms
     */
    @Nullable
    public static Object asNmsCopy(@Nonnull ItemStack item) {
        Object nms = null;
        try {
            if (AS_NMS_COPY != null) {
                nms = AS_NMS_COPY.invoke(null, item);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return nms;
    }

    /**
     * ItemStack转为CraftItemStack
     * 
     * @param item 物品
     * @return CraftItemStack
     */
    @Nullable
    public static Object asCraftCopy(@Nonnull ItemStack item) {
        Object craftItem = null;
        try {
            if (AS_CRAFT_COPY != null) {
                craftItem = AS_CRAFT_COPY.invoke(null, item);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return craftItem;
    }

    /**
     * 物品转为nbt
     * 
     * @param item 物品
     * @return nbt
     */
    @Nullable
    public static String toTagString(@Nonnull ItemStack item) {
        String nbt = "{}";
        Object handle = getCraftItemHandle(item);
        if (handle == null) {
            handle = asNmsCopy(item);
        }
        if (handle != null && GET_TAG_CLONE != null) {
            try {
                final Object tag = GET_TAG_CLONE.invoke(handle);
                if (tag != null) {
                    nbt = tag.toString();
                }
            } catch (IllegalAccessException | IllegalArgumentException 
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return nbt;
    }

}
