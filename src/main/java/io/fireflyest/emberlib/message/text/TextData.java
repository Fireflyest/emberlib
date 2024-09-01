package io.fireflyest.emberlib.message.text;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * 文本数据
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class TextData {

    @SerializedName("text")
    private String text;

    @SerializedName("extra")
    private List<Object> extra;

    public TextData() {
        this("");
    }

    public TextData(String text) {
        this.extra = new ArrayList<>();
        this.text = text == null ? "" : text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Object> getExtra() {
        return extra;
    }

    public void setExtra(List<Object> extra) {
        this.extra = extra;
    }

}
