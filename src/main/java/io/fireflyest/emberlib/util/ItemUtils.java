package io.fireflyest.emberlib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import io.fireflyest.emberlib.inventory.item.XMaterial;

/**
 * 物品工具类
 * 
 * @author Fireflyest
 */
public final class ItemUtils {

    private ItemUtils() {
    }

    /**
     * 设置物品NBT
     * 
     * @param item 物品
     * @param key 键
     * @param value 值
     */
    public static void setItemNbt(@Nonnull ItemStack item, 
                                  @Nonnull NamespacedKey key, 
                                  Object value) {
        if (item.getType() == Material.AIR) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (value instanceof String) {
                meta.getPersistentDataContainer()
                    .set(key, PersistentDataType.STRING, (String) value);
            } else if (value instanceof Integer) {
                meta.getPersistentDataContainer()
                    .set(key, PersistentDataType.INTEGER, (Integer) value);
            } else if (value instanceof Double) {
                meta.getPersistentDataContainer()
                    .set(key, PersistentDataType.DOUBLE, (Double) value);
            }
        }
    }

    /**
     * 获取物品NBT
     * 
     * @param item 物品
     * @return 值
     */
    public static PersistentDataContainer getItemNbt(@Nonnull ItemStack item) {
        PersistentDataContainer container = null;
        if (item.getType() == Material.AIR) {
            return container;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            container = meta.getPersistentDataContainer();
        }
        return container;
    }

    /**
     * 设置物品自定义模型
     * 
     * @param item 物品
     * @param model 模型
     */
    public static void setItemModel(@Nonnull ItemStack item, int model) {
        if (item.getType() == Material.AIR) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(model);
            item.setItemMeta(meta);
        }
    }

    /**
     * 设置物品名称
     * 
     * @param item 物品
     * @param name 名称
     */
    public static void setDisplayName(@Nonnull ItemStack item, String name) {
        if (item.getType() == Material.AIR) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name.replace("&", "§"));
            item.setItemMeta(meta);
        }
    }

    /**
     * 添加注释
     * 
     * @param item 物品
     * @param lore 注释
     */
    public static void addLore(@Nonnull ItemStack item, String... lore) {
        if (item.getType() == Material.AIR) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lores = meta.getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            lores.addAll(Arrays.asList(lore));
            meta.setLore(lores);
            item.setItemMeta(meta);
        }
    }

    /**
     * 获取物品名称
     * 
     * @param item 物品
     * @return 名称
     */
    public static String getDisplayName(@Nonnull ItemStack item) {
        String displayName = "";
        if (item.getType() == Material.AIR) {
            return displayName;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            displayName = meta.getDisplayName();
        }
        return displayName;
    }

    /**
     * 设置物品注释
     * 
     * @param item 物品
     * @param lore 注释
     * @param line 行
     */
    public static void setLore(@Nonnull ItemStack item, String lore, int line) {
        if (item.getType() == Material.AIR) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lores = item.getItemMeta().getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            if (line > 31) {
                line = 31;
            }
            while (lores.size() <= line) {
                lores.add("");
            }
            lores.set(line, lore);
            meta.setLore(lores);
            item.setItemMeta(meta);
        }
    }

    /**
     * 设置玩家头颅
     * 
     * @param item 物品
     * @param player 头颅
     */
    public static void setSkullOwner(@Nonnull ItemStack item, OfflinePlayer player) {
        if (item.getType() != XMaterial.PLAYER_HEAD.parseMaterial()) {
            return;
        }
        final SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            item.setItemMeta(meta);
        }
    }

}
