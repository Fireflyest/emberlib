package io.fireflyest.emberlib.inventory;

import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.event.inventory.InventoryAction;

/**
 * 页面中的一个槽位
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class Slot {

    /**
     * 容器操作对应的界面行为
     * 
     * @see org.bukkit.event.inventory.InventoryAction
     */
    private Map<InventoryAction, ActionResult> viewActionMap = new EnumMap<>(InventoryAction.class);

    /**
     * 页面中的一个槽位
     */
    public Slot() {
        // 
    }

    /**
     * 指定某容器操作所执行的界面行为
     * 
     * @param inventoryAction 容器操作
     * @param viewAction 界面行为
     * @return 本身
     */
    public Slot result(@Nonnull InventoryAction inventoryAction, @Nonnull ActionResult viewAction) {
        viewActionMap.put(inventoryAction, viewAction);
        return this;
    }

    /**
     * 指定某容器操作所执行的界面行为
     * 
     * @param inventoryAction 容器操作
     * @param allow 是否允许
     * @param type 返回结果类型
     * @return 本身
     */
    public Slot result(@Nonnull InventoryAction inventoryAction, boolean allow, int type) {
        viewActionMap.put(inventoryAction, new ActionResult(allow, type));
        return this;
    }

    /**
     * 指定某容器操作所执行的界面行为
     * 
     * @param inventoryAction 容器操作
     * @param allow 是否允许
     * @param type 返回结果类型
     * @param value 返回结果值
     * @return 本身
     */
    public Slot result(@Nonnull InventoryAction inventoryAction, boolean allow, int type, 
                       @Nullable String value) {
        viewActionMap.put(inventoryAction, new ActionResult(allow, type, value));
        return this;
    }

    /**
     * 获取容器操作所执行的界面行为
     * 
     * @param inventoryAction 容器操作
     * @return 界面行为
     */
    @Nullable
    public ActionResult getResult(@Nonnull InventoryAction inventoryAction) {
        return viewActionMap.get(inventoryAction);
    }

}
