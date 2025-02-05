package io.fireflyest.emberlib.inventory.item;

import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * 头颅物品构建
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class SkullItemBuilder extends ItemBuilder {

    private OfflinePlayer player;

    public SkullItemBuilder(Material material) {
        super(material);
    }

    public SkullItemBuilder(String material) {
        super(material);
    }
    
    public SkullItemBuilder(@Nonnull OfflinePlayer player) {
        super(XMaterial.PLAYER_HEAD.parseMaterial());
        this.player = player;
    }

    /**
     * 设置头颅玩家
     * 
     * @param player 玩家
     * @return {@link SkullItemBuilder}
     */
    public SkullItemBuilder setPlayer(@Nonnull OfflinePlayer player) {
        update = true;
        this.player = player;
        return this;
    }

    @Override
    protected void meta(@Nonnull ItemMeta meta) {
        final SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
        }
        item.setItemMeta(meta);
    }

}
