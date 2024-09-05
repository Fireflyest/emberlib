package io.fireflyest.emberlib.inventory;

import javax.annotation.Nullable;
import org.bukkit.Sound;

/**
 * 页面操作返回行为
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class ActionResult {
    
    /**
     * 无事发生
     */
    public static final int ACTION_NONE = 0;

    /**
     * 返回
     */
    public static final int ACTION_BACK = 1;

    /**
     * 关闭
     */
    public static final int ACTION_CLOSE = 2;

    /**
     * 打开某个界面
     */
    public static final int ACTION_OPEN = 3;

    /**
     * 刷新
     */
    public static final int ACTION_REFRESH = 3;

    /**
     * 上一页
     */
    public static final int ACTION_PAGE_PRE = 11;

    /**
     * 下一页
     */
    public static final int ACTION_PAGE_NEXT = 12;

    /**
     * 跳转页面
     */
    public static final int ACTION_PAGE_JUMP = 13;

    /**
     * 打开另一个页面
     */
    public static final int ACTION_PAGE_OPEN = 14;

    /**
     * 执行指令，值一般是要执行的指令
     */
    public static final int ACTION_PLAYER_COMMAND = 4;

    /**
     * 控制台指令
     */
    public static final int ACTION_CONSOLE_COMMAND = 5;
    
    /**
     * 未知
     */
    public static final int ACTION_UNKNOWN = -1;

    private final boolean allow;
    private final int type;
    private final String value;

    private Sound sound;

    /**
     * 页面操作返回行为
     * 
     * @param allow 容器操作是允许否
     * @param type 操作类型
     * @param value 返回值
     */
    protected ActionResult(boolean allow, int type, @Nullable String value) {
        this.allow = allow;
        this.type = type;
        this.value = value;
    }

    /**
     * 页面操作返回行为
     * 
     * @param allow 容器操作是允许否
     * @param type 操作类型
     */
    protected ActionResult(boolean allow, int type) {
        this(allow, type, null);
    }

    public boolean isAllow() {
        return allow;
    }

    public int getType() {
        return type;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    @Nullable
    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

}
