package io.fireflyest.emberlib.message.data;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fireflyest
 * @since 1.2
 */
public class InteractText {

    @SerializedName("extra")
    private List<InteractExtraData> extra;

    @SerializedName("text")
    private String text;

    public InteractText() {
        this("");
    }

    public InteractText(String text) {
        this.extra = new ArrayList<>();
        this.text = text;
    }

    public List<InteractExtraData> getExtra() {
        return extra;
    }

    public void setExtra(List<InteractExtraData> extra) {
        this.extra = extra;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
