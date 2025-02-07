package io.fireflyest.emberlib.inventory.material;

import org.bukkit.Material;

/**
 * 把材料名称翻译成各种语言
 * @author Fireflyest
 * @since 2022/7/31
 */
public interface MaterialName {

    boolean isEnable();

    void enable();

    String translate(Material material);
}
