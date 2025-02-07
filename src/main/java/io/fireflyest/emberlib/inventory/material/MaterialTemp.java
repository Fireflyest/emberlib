package io.fireflyest.emberlib.inventory.material;

import org.bukkit.Material;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板材料名称
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class MaterialTemp implements MaterialName {

    private boolean enable = false;

    private final Map<String, String> translateMap = new HashMap<>();

    private static MaterialTemp instance = null;

    private MaterialTemp() {
    }

    /**
     * 获取实例
     * 
     * @return MaterialTemp
     */
    public static MaterialTemp getInstance() {
        if (instance == null) {
            instance = new MaterialTemp();
        }
        return instance;
    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void enable() {
        if (enable) {
            return;
        }
        //<editor-fold defaultstate="collapsed" desc="translate">

        //</editor-fold>
        enable = true;
    }

    @Override
    public String translate(Material material) {
        final String type = material.name();
        if (translateMap.containsKey(type)) {
            return translateMap.get(type);
        } else {
            return type.toLowerCase();
        }
    }

}
