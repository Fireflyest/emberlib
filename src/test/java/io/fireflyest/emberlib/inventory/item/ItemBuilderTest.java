package io.fireflyest.emberlib.inventory.item;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.fireflyest.emberlib.message.text.TextData;
import io.fireflyest.emberlib.message.text.TextJsonDeserializer;
import io.fireflyest.emberlib.util.TextUtils;
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
        Gson gson = new GsonBuilder()
				.registerTypeAdapter(TextData.class, new TextJsonDeserializer())
				.create();
        String raw = "{\"text\":\"\",\"extra\":[\"aaaa\",{\"text\":\"$<g=#ffffff:#f6f6f5>aa11111aa\",\"obfuscated\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"color\":\"green\",\"bold\":false}]}";
        TextData textData = gson.fromJson(raw, TextData.class);

        System.out.println(gson.toJson(textData));

        System.out.println("---------------------------------");
        // for (Object extra : textData.getExtra()) {
        //     System.out.println(extra);
        //     System.out.println(extra.getClass());
        // }

        System.out.println(TextUtils.contains(Pattern.compile("\\$<([^>]*)>"), "$<hg=#ffffff:#f6f6f5>aa11111aa"));

        String[] splits = "aaa$<hg=#ffffff:#f6f6f5>bbb$<hg=#ffffff:#f6f6f5>$<hg=#ffffff:#f6f6f5>ccc".split("(\\$<([^>]*)>)+");
        System.out.println(splits.length);
        for (String split : splits) {
            System.out.println(split);
        }

        System.out.println(new Gson().toJsonTree(raw));

        System.out.println(2-1.0);
    }


}
