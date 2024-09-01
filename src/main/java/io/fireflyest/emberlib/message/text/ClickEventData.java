package io.fireflyest.emberlib.message.text;

import com.google.gson.annotations.SerializedName;

/**
 * 文本点击事件
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class ClickEventData {

    @SerializedName("action")
    private String action;

    @SerializedName("value")
    private String value;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
