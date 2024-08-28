package io.fireflyest.emberlib.inventory.item;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import io.fireflyest.emberlib.message.formal.TextColorFormal;
import io.fireflyest.emberlib.util.YamlUtils;

/**
 * 物品构建测试
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class ItemBuilderTest {

    @Test
    public void testColorData() {
        String raw = "{\"text\":\"\",\"extra\":[{\"text\":\"$<hg=#ffffff:#f6f6f5>aa11111aa\",\"obfuscated\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"color\":\"green\",\"bold\":false}]}";
        System.out.println(new TextColorFormal(raw).toString());

        System.out.println(2-1.0);
    }


}
