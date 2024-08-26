package io.fireflyest.emberlib.inventory.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建自定义物品
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class ItemBuilder {

    protected boolean update = true;

    protected Material material;
    protected String displayName;
    protected ItemFlag[] itemFlags;
    protected final List<String> lore = new ArrayList<>();
    protected final Map<NamespacedKey, Object> nbt = new LinkedHashMap<>();
    protected boolean colorful = false;
    protected int amount = 1;
    protected int model = -1;

    protected ItemStack item;

    /**
     * 使用材料快速物品构建
     * 
     * @param material 材料
     */
    public ItemBuilder(@Nullable Material material) {
        this.material = material;
    }

    /**
     * 使用材料名称快速物品构建
     * 
     * @param material 材料名称
     */
    public ItemBuilder(@Nonnull String material) {
        // Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(material);
        // xMaterial.ifPresent(value -> this.material = value.parseMaterial());
    }

    /**
     * 命名
     * 
     * @param displayName 名称
     * @return 本身
     */
    public ItemBuilder name(@Nonnull String displayName) {
        update = true;
        this.displayName = displayName.replace("&", "§");
        return this;
    }

    /**
     * 物品lore
     * 
     * @param lore lore
     * @return 本身
     */
    public ItemBuilder lore(@Nonnull String lore) {
        update = true;
        this.lore.add(lore.replace("&", "§"));
        return this;
    }

    /**
     * 底层nbt数据
     * 
     * @param key 键
     * @param value 值
     * @return 本身
     */
    public ItemBuilder nbt(@Nonnull NamespacedKey key, Object value) {
        update = true;
        nbt.put(key, value);
        return this;
    }

    /**
     * 物品flag
     * 
     * @param itemFlags flag
     * @return 本身
     */
    public ItemBuilder flags(@Nonnull ItemFlag... itemFlags) {
        update = true;
        this.itemFlags = itemFlags;
        return this;
    }

    /**
     * 物品显示更多颜色1.16之后支持
     * 
     * @return 本身
     */
    public ItemBuilder colorful()  {
        update = true;
        this.colorful = true;
        return this;
    }

    /**
     * 物品数量
     * 
     * @param amount 数量
     * @return 本身
     */
    public ItemBuilder amount(int amount) {
        update = true;
        this.amount = amount;
        return this;
    }

    /**
     * 材质模型
     * 
     * @param model 模型值
     * @return 本身
     */
    public ItemBuilder model(int model) {
        update = true;
        this.model = model;
        return this;
    }

    /**
     * 完成构建
     * @return 物品
     */
    public ItemStack build() {
        // 缓存
        if (!update) {
            return item.clone();
        }

        update = false;
        // 堆
        item = new ItemStack(material == null ? Material.STONE : material);
        item.setAmount(amount);
        // 元数据
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (displayName != null) {
            meta.setDisplayName(displayName);
        }
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }
        if (itemFlags != null && itemFlags.length > 0) {
            meta.addItemFlags(itemFlags);
        }
        if (model != -1) {
            meta.setCustomModelData(model);
        }

        // 颜色
        if (colorful) {
            System.out.println("meta.getPersistentDataContainer() = " + meta.getPersistentDataContainer().toString());
            // this.colorData(nbtItem);
        }
        if (nbt.size() > 0) {
            for (Map.Entry<NamespacedKey, Object> entry : nbt.entrySet()) {
                final NamespacedKey key = entry.getKey();
                final Object value = entry.getValue();
                if (value instanceof String) {
                    meta.getPersistentDataContainer()
                        .set(key, PersistentDataType.STRING, (String) value);
                } else if (value instanceof Integer) {
                    meta.getPersistentDataContainer()
                        .set(key, PersistentDataType.INTEGER, (Integer) value);
                }
            }
        }

        item.setItemMeta(meta);

        return item;
    }

    // /**
    //  * 颜色数据
    //  * @param nbtItem 物品
    //  */
    // private void colorData(NBTItem nbtItem) {
    //     NBTCompound display = nbtItem.getCompound("display");
    //     if (display != null) {
    //         // name
    //         display.setString("Name", new TextColorFormal(display.getString("Name")).toString());
    //         // lore
    //         NBTList<String> loreList = display.getStringList("Lore");
    //         if (loreList != null) {
    //             int lorePos = 0;
    //             for (String loreString : loreList) {
    //                 loreList.set(lorePos++, new TextColorFormal(loreString).toString());
    //             }
    //         }
    //     }
    // }

}
