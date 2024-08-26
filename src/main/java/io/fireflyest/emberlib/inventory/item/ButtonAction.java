package io.fireflyest.emberlib.inventory.item;

import org.bukkit.NamespacedKey;
import io.fireflyest.emberlib.EmberLib;

/**
 * 物品数据标签
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class ButtonAction {

    /**
     * 点击行为
     */
    public static final NamespacedKey NBT_VALUE_KEY = 
        new NamespacedKey(EmberLib.getPlugin(), "gui-action");
    
    /**
     * 点击行为数据
     */
    public static final NamespacedKey NBT_ACTION_KEY = 
        new NamespacedKey(EmberLib.getPlugin(), "gui-value");

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
     * 界面编辑
     */
    public static final int ACTION_EDIT = 7;

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
     * 打开某个界面
     */
    public static final int ACTION_OPEN = 3;

    /**
     * 执行指令，值一般是要执行的指令
     */
    public static final int ACTION_PLAYER_COMMAND = 4;

    /**
     * 控制台指令
     */
    public static final int ACTION_CONSOLE_COMMAND = 5;

    /**
     * Shift点击指令，分号前后分别为普通指令和附加指令
     */
    public static final int ACTION_SHIFT_COMMAND = 17;

    /**
     * 插件自定义行为，由插件识别value自己判别
     */
    public static final int ACTION_PLUGIN = 8;
    
    /**
     * 未知
     */
    public static final int ACTION_UNKNOWN = -1;

    private ButtonAction() {
        //
    }

}
