package io.fireflyest.emberlib.inventory.item;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.fireflyest.emberlib.message.text.TextData;
import io.fireflyest.emberlib.message.text.TextJsonDeserializer;
import io.fireflyest.emberlib.util.TextUtils;
import io.fireflyest.emberlib.util.YamlUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 构建自定义物品
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class ItemBuilder {

    private final Gson gson = new GsonBuilder()
        .registerTypeAdapter(TextData.class, new TextJsonDeserializer())
        .create();

    private static final String DISPLAY_NAME_KEY = YamlUtils.DATA_PATH + '.' + "display-name";
    private static final String LORE_KEY = YamlUtils.DATA_PATH + '.' + "lore";

    protected Map<String, Object> replaces = new HashMap<>();

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
        final Optional<XMaterial> optional = XMaterial.matchXMaterial(material);
        optional.ifPresent(value -> this.material = value.parseMaterial());
    }

    /**
     * 文本变量
     * 
     * @param key 变量名
     * @param value 变量值
     * @return 本身
     */
    public ItemBuilder replace(String key, Object value) {
        update = true;
        replaces.put(key, value);
        return this;
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
     * @param line lore
     * @return 本身
     */
    public ItemBuilder lore(@Nonnull String line) {
        update = true;
        this.lore.add(line.replace("&", "§"));
        return this;
    }

    /**
     * 物品lore
     * 
     * @param lore lore
     * @param line 行数
     * @return 本身
     */
    public ItemBuilder lore(@Nonnull String lore, int line) {
        update = true;
        if (line > 31) {
            line = 31;
        }
        while (this.lore.size() <= line) {
            this.lore.add("");
        }
        this.lore.set(line, lore.replace("&", "§"));
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
    public ItemBuilder colorful(boolean colorful) {
        update = true;
        this.colorful = colorful;
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
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (!replaces.isEmpty()) {
            displayName = TextUtils.varReplace(
                TextUtils.PERCENT_PATTERN, 
                displayName, 1, 1, 
                k -> String.valueOf(replaces.getOrDefault(k, k)));
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, TextUtils.varReplace(
                    TextUtils.PERCENT_PATTERN, 
                    lore.get(i), 1, 1, 
                    k -> String.valueOf(replaces.getOrDefault(k, k))
                ));
            }
        }
        if (displayName != null) {
            meta.setDisplayName(displayName);
        }
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }
        // 颜色
        if (colorful) {
            meta = this.colorData(meta);
        }
        // 自定义数据
        if (nbt.size() > 0) {
            this.nbtData(meta);
        }
        if (itemFlags != null && itemFlags.length > 0) {
            meta.addItemFlags(itemFlags);
        }
        if (model != -1) {
            meta.setCustomModelData(model);
        }
        this.meta(meta);
        return item;
    }

    /**
     * 颜色数据
     * 
     * @param meta 元数据
     */
    public ItemMeta colorData(ItemMeta meta) {
        final YamlConfiguration yamlContainer = new YamlConfiguration();
        String metaYaml = StringUtils.replaceOnce(YamlUtils.serialize(meta), "==", "temp");
        try {
            yamlContainer.loadFromString(metaYaml);
            if (yamlContainer.contains(DISPLAY_NAME_KEY)) {
                final String rawName = yamlContainer.getString(DISPLAY_NAME_KEY);
                yamlContainer.set(
                    DISPLAY_NAME_KEY, 
                    gson.toJson(gson.fromJson(rawName, TextData.class))
                );
            }
            if (yamlContainer.contains(LORE_KEY)) {
                final List<String> rawLore = yamlContainer.getStringList(LORE_KEY);
                int linePos = 0;
                for (String line : rawLore) {
                    rawLore.set(
                        linePos++, 
                        gson.toJson(gson.fromJson(line, TextData.class))
                    );
                }
                yamlContainer.set(LORE_KEY, rawLore);
            }
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        metaYaml = StringUtils.replaceOnce(yamlContainer.saveToString(), "temp", "==");
        return YamlUtils.deserialize(metaYaml, meta.getClass());
    }

    /**
     * 物品元数据
     * 
     * @param meta 元数据
     */
    protected void meta(@Nonnull ItemMeta meta) {
        item.setItemMeta(meta);
    }

    /**
     * 自定义数据
     * 
     * @param meta 元数据
     */
    private void nbtData(ItemMeta meta) {
        for (Map.Entry<NamespacedKey, Object> entry : nbt.entrySet()) {
            final NamespacedKey key = entry.getKey();
            final Object value = entry.getValue();
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

}
