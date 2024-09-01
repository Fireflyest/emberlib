package io.fireflyest.emberlib.message.text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.apache.commons.lang.StringUtils;
import org.bukkit.util.NumberConversions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.fireflyest.emberlib.util.ColorUtils;
import io.fireflyest.emberlib.util.TextUtils;

/**
 * 文本反序列化
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class TextJsonDeserializer implements JsonDeserializer<TextData> {

    private final Gson gson = new Gson();

    private static final String DATA_REGEX = "\\$<([^>]*)>";
    private static final Pattern DATA_PATTERN = Pattern.compile(DATA_REGEX);

    /**
     * 点击事件
     */
    public static final String CLICK_EVENT_KEY = "ce";

    /**
     * 悬浮事件
     */
    public static final String HOVER_EVENT_KEY = "he";

    /**
     * 颜色 c=#FFFFFF
     */
    public static final String COLOR_KEY = "c";

    /**
     * 渐变 g=#FFFFFF:#000000:5:4 g=#FFFFFF:#000000
     */
    public static final String GRADIENT_KEY = "g";

    @Override
    public TextData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final TextData textData = new TextData();
        final JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("text")) {
            textData.setText(jsonObject.get("text").getAsString());
        }

        if (jsonObject.has("extra")) {
            final List<Object> extraList = new ArrayList<>();
            final JsonArray jsonArray = jsonObject.getAsJsonArray("extra");
            final Iterator<JsonElement> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                final JsonElement next = iterator.next();
                if (next.isJsonObject()) {
                    extraList.addAll(this.deserializeExtra(next.getAsJsonObject()));
                } else {
                    extraList.add(next.getAsString());
                }
            }
            textData.setExtra(extraList);
        }

        return textData;
    }

    /**
     * 解析句子
     * 
     * @param jsonObject 数据对象
     * @return 解析完的句子数据列表
     */
    private List<Object> deserializeExtra(@Nonnull JsonObject jsonObject) {
        final List<Object> dataList = new ArrayList<>();
        if (jsonObject.has("text")) {
            final String text = jsonObject.get("text").getAsString();
            final String[] prefixArray = TextUtils.find(DATA_PATTERN, text);
            final String[] splitArray = text.split(DATA_REGEX);
            if (prefixArray.length <= 0 || splitArray.length <= 0) {
                dataList.add(jsonObject);
            } else {
                dataList.addAll(
                    this.processText(
                        jsonObject, 
                        prefixArray, 
                        splitArray
                    )
                );
            }
        } else if (jsonObject.has("translate")) {
            // 翻译类型不做处理
            dataList.add(jsonObject);
        }
        return dataList;
    }
    
    private List<Object> processText(@Nonnull JsonObject textJsonObject,
            @Nonnull String[] prefixArray, @Nonnull String[] splitArray) {
        final List<Object> dataList = new ArrayList<>();
        final boolean bold = textJsonObject.get("bold").getAsBoolean();
        final boolean italic = textJsonObject.get("italic").getAsBoolean();
        final boolean underlined = textJsonObject.get("underlined").getAsBoolean();
        final boolean strikethrough = textJsonObject.get("strikethrough").getAsBoolean();
        final boolean obfuscated = textJsonObject.get("obfuscated").getAsBoolean();
        final String color = textJsonObject.get("color").getAsString();
        final Object clickEvent = textJsonObject.getAsJsonObject("clickEvent");
        final Object hoverEvent = textJsonObject.getAsJsonObject("hoverEvent");
        // 头部
        final ExtraData extraData = new ExtraData(
            bold, italic, underlined, strikethrough, obfuscated, 
            color, 
            splitArray[0]
        );
        extraData.setClickEvent(clickEvent);
        extraData.setHoverEvent(hoverEvent);
        // 头部不是空文本的话添加回去
        if (!"".equals(splitArray[0])) {
            dataList.add(extraData);
        }
        // 遍历所有尖括号及其后置文本
        for (int i = 1; i < splitArray.length; i++) {
            final String prefix = prefixArray[i - 1];
            final String split = splitArray[i];
            dataList.addAll(
                this.processPrefix(
                    extraData, 
                    StringUtils.substring(prefix, 2, prefix.length() - 1), 
                    split
                )
            );
        }
        return dataList;
    }

    private List<Object> processPrefix(@Nonnull ExtraData extraData,
            @Nonnull String prefix, @Nonnull String split) {
        final List<Object> dataList = new ArrayList<>();
        Object clickEvent = extraData.getClickEvent();
        Object hoverEvent = extraData.getHoverEvent();
        // 尖括号里面的键值对
        final String[] kvArray = StringUtils.split(prefix, ',');
        final HashMap<String, String> kvMap = new HashMap<>();
        for (String kvString : kvArray) {
            if (!kvString.contains("=")) {
                continue;
            }
            final String[] kv = StringUtils.split(kvString, '=');
            kvMap.put(kv[0], kv[1]);
        }
        // 点击事件
        if (kvMap.containsKey(CLICK_EVENT_KEY)) {
            clickEvent = gson.fromJson(
                TextUtils.base64Decode(kvMap.get(CLICK_EVENT_KEY)), 
                ClickEventData.class
            );
        }
        // 悬浮事件
        if (kvMap.containsKey(HOVER_EVENT_KEY)) {
            hoverEvent = gson.fromJson(
                TextUtils.base64Decode(kvMap.get(HOVER_EVENT_KEY)), 
                HoverEventData.class
            );
        }
        // 颜色
        if (kvMap.containsKey(GRADIENT_KEY)) {
            final String[] values = StringUtils.split(kvMap.get(GRADIENT_KEY), ':');
            if (values.length == 2) { // 水平渐变
                dataList.addAll(
                    this.horizontalGradient(split, values, extraData, clickEvent, hoverEvent)
                );
            } else if (values.length == 4) { // 垂直渐变
                dataList.addAll(
                    this.verticalGradient(split, values, extraData, clickEvent, hoverEvent)
                );
            }
        } else {
            final String color = kvMap.containsKey(COLOR_KEY) 
                ? kvMap.get(COLOR_KEY) : extraData.getColor();
            final ExtraData newExtraData = new ExtraData(
                extraData.getBold(), 
                extraData.getItalic(), 
                extraData.getUnderlined(), 
                extraData.getStrikethrough(), 
                extraData.getObfuscated(), 
                color, 
                split
            );
            newExtraData.setClickEvent(clickEvent);
            newExtraData.setHoverEvent(hoverEvent);
            dataList.add(newExtraData);
        }

        return dataList;
    }

    private List<Object> verticalGradient(@Nonnull String split, @Nonnull String[] values, 
            @Nonnull ExtraData extraData, @Nonnull Object clickEvent, @Nonnull Object hoverEvent) {
        final List<Object> dataList = new ArrayList<>();
        final String startColor = values[0];
        final String endColor = values[1];
        final int num = NumberConversions.toInt(values[2]);
        final String[] colors = ColorUtils.gradient(startColor, endColor, num);
        // 获取对应的颜色
        int phase = NumberConversions.toInt(values[3]);
        if (phase >= colors.length) {
            phase = colors.length - 1;
        }
        if (phase < 0) {
            phase = 0;
        }
        final ExtraData newExtraData = new ExtraData(
            extraData.getBold(), 
            extraData.getItalic(), 
            extraData.getUnderlined(), 
            extraData.getStrikethrough(), 
            extraData.getObfuscated(), 
            colors[phase], 
            split
        );
        newExtraData.setClickEvent(clickEvent);
        newExtraData.setHoverEvent(hoverEvent);
        dataList.add(newExtraData);
        return dataList;
    }

    private List<Object> horizontalGradient(@Nonnull String split, @Nonnull String[] values, 
            @Nonnull ExtraData extraData, @Nonnull Object clickEvent, @Nonnull Object hoverEvent) {
        final List<Object> dataList = new ArrayList<>();
        final String startColor = values[0];
        final String endColor = values[1];
        int charPos = 0;
        for (String color : ColorUtils.gradient(startColor, endColor, split.length())) {
            final ExtraData newExtraData = new ExtraData(
                extraData.getBold(), 
                extraData.getItalic(), 
                extraData.getUnderlined(), 
                extraData.getStrikethrough(), 
                extraData.getObfuscated(), 
                color, 
                String.valueOf(split.charAt(charPos++))
            );
            newExtraData.setClickEvent(clickEvent);
            newExtraData.setHoverEvent(hoverEvent);
            dataList.add(newExtraData);
        }
        return dataList;
    }

}
