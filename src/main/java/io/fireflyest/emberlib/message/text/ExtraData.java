package io.fireflyest.emberlib.message.text;

import com.google.gson.annotations.SerializedName;

/**
 * 文本数据
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class ExtraData {
    
    @SerializedName("text")
    private String text;

    @SerializedName("translate")
    private String translate;

    @SerializedName("obfuscated")
    private Boolean obfuscated;

    @SerializedName("italic")
    private Boolean italic;

    @SerializedName("underlined")
    private Boolean underlined;

    @SerializedName("strikethrough")
    private Boolean strikethrough;

    @SerializedName("color")
    private String color;

    @SerializedName("bold")
    private Boolean bold;

    @SerializedName("clickEvent")
    private Object clickEvent;

    @SerializedName("hoverEvent")
    private Object hoverEvent;

    public ExtraData() {
    }

    /**
     * 额外文本数据
     * 
     * @param bold 粗
     * @param italic 斜
     * @param underlined 下划线
     * @param strikethrough 中划线
     * @param obfuscated 混乱
     * @param color 颜色
     * @param text 文本
     */
    public ExtraData(Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough,
            Boolean obfuscated, String color, String text) {
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
        this.color = color;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public Boolean getBold() {
        return bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Boolean getItalic() {
        return italic;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public Boolean getUnderlined() {
        return underlined;
    }

    public void setUnderlined(Boolean underlined) {
        this.underlined = underlined;
    }

    public Boolean getStrikethrough() {
        return strikethrough;
    }

    public void setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

    public Boolean getObfuscated() {
        return obfuscated;
    }

    public void setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Object getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(Object clickEvent) {
        this.clickEvent = clickEvent;
    }

    public Object getHoverEvent() {
        return hoverEvent;
    }

    public void setHoverEvent(Object hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

}
