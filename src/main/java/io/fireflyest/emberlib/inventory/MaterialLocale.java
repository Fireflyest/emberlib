package io.fireflyest.emberlib.inventory;

import org.bukkit.Material;
import io.fireflyest.emberlib.inventory.material.MaterialChinese;
import io.fireflyest.emberlib.inventory.material.MaterialChineseFan;
import io.fireflyest.emberlib.inventory.material.MaterialFrench;
import io.fireflyest.emberlib.inventory.material.MaterialGerman;
import io.fireflyest.emberlib.inventory.material.MaterialJapanese;
import io.fireflyest.emberlib.inventory.material.MaterialName;
import io.fireflyest.emberlib.inventory.material.MaterialRussian;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 材料本地化名称
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class MaterialLocale {

    private static final Map<String, MaterialName> langMap = new HashMap<>();
    private static String language = Locale.getDefault().toLanguageTag();

    static {
        langMap.put("zh", MaterialChinese.getInstance()); // 简体中文
        langMap.put("zh-CN", MaterialChinese.getInstance()); // 简体中文
        langMap.put("zh-TW", MaterialChineseFan.getInstance()); // 繁体中文
        langMap.put("zh-HK", MaterialChineseFan.getInstance()); // 繁体中文
        langMap.put("de", MaterialGerman.getInstance()); // 德语 German
        langMap.put("de-DE", MaterialGerman.getInstance()); // 德语 German
        langMap.put("fr", MaterialFrench.getInstance()); // 法语 French
        langMap.put("fr-FR", MaterialFrench.getInstance()); // 法语 French
        langMap.put("ja", MaterialJapanese.getInstance()); // 日语 Japanese
        langMap.put("ja-JA", MaterialJapanese.getInstance()); // 日语 Japanese
        langMap.put("ru", MaterialRussian.getInstance()); // 俄语 Russian
        langMap.put("ru-RU", MaterialRussian.getInstance()); // 俄语 Russian
    }

    private MaterialLocale() {
        // 工具类
    }

    /**
     * 设置语言
     * @param language 语言
     */
    public static void setLanguage(String language) {
        MaterialLocale.language = language;
        // 初始化
        
    }

    /**
     * 获取材料本地名称
     * @param material 材料
     * @return 名称
     */
    public static String translate(Material material) {
        return translate(material, language);
    }

    /**
     * 获取材料本地名称
     * @param material 材料
     * @return 名称
     */
    public static String translate(Material material, String lang) {
        if (langMap.containsKey(lang)) {
            final MaterialName materialName = langMap.get(lang);
            if (!materialName.isEnable()) {
                materialName.enable();
            }
            return materialName.translate(material);
        }
        return material.name().toLowerCase();
    }

}
