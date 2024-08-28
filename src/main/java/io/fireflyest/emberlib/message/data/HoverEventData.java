package io.fireflyest.emberlib.message.data;

import com.google.gson.annotations.SerializedName;

/**
 * 悬浮事件
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class HoverEventData {

    @SerializedName("action")
    private String action;

    @SerializedName("value")
    private String value;

    @SerializedName("contents")
    private HoverEventContentsData contents;

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

    public HoverEventContentsData getContents() {
        return contents;
    }

    public void setContents(HoverEventContentsData contents) {
        this.contents = contents;
    }
    
}
