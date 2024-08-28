package io.fireflyest.emberlib.message.data;

import com.google.gson.annotations.SerializedName;

/**
 * 点击事件
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class InteractExtraData extends ExtraData {

    @SerializedName("clickEvent")
    private ClickEventData clickEvent;

    @SerializedName("hoverEvent")
    private HoverEventData hoverEvent;

    public ClickEventData getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEventData clickEvent) {
        this.clickEvent = clickEvent;
    }

    public HoverEventData getHoverEvent() {
        return hoverEvent;
    }

    public void setHoverEvent(HoverEventData hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

}
