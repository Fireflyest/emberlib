package io.fireflyest.emberlib.message.text;

import com.google.gson.annotations.SerializedName;

/**
 * 悬浮显示内容
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class HoverEventContentsData {
    @SerializedName("id")
    private String id;

    @SerializedName("tag")
    private String tag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
}
