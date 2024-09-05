package io.fireflyest.emberlib.inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import io.fireflyest.emberlib.Print;

/**
 * 一个页面节点
 * 
 * @author Fireflyest
 * @since 1.0
 */
public abstract class Page extends BukkitRunnable implements InventoryHolder {

    /**
     * 槽位
     */
    protected final Map<Integer, Slot> slotMap;

    /**
     * 页面所属名称
     */
    protected final String target;
    
    /**
     * 当前页面页码
     */
    protected final int pageNumber;

    /**
     * 容器大小
     */
    protected final int size;

    /**
     * 容器类型
     */
    protected final InventoryType inventoryType;

    /**
     * 界面载体
     */
    protected Inventory inventory;

    /**
     * 标题
     */
    protected String title = "";

    /**
     * 下一页
     */
    protected Page next = null;

    /**
     * 上一页
     */
    protected Page pre = null;

    /**
     * 是否需要刷新
     */
    protected boolean refresh = true;

    /**
     * 一个页面节点
     * 
     * @param target 页面所属
     * @param pageNumber 页码
     * @param size 容器大小
     */
    protected Page(@Nullable String target, int pageNumber, int size) {
        this.target = target;
        this.pageNumber = pageNumber;
        this.size = size;
        this.slotMap = new ConcurrentHashMap<>(size);
        this.inventoryType = InventoryType.CHEST;
    }

    /**
     * 一个页面节点
     * 
     * @param target 页面所属
     * @param pageNumber 页码
     * @param inventoryType 容器类型
     */
    protected Page(@Nullable String target, int pageNumber, InventoryType inventoryType) {
        this.target = target;
        this.pageNumber = pageNumber;
        this.size = inventoryType.getDefaultSize();
        this.slotMap = new ConcurrentHashMap<>(size);
        this.inventoryType = inventoryType;
    }

    /**
     * 获取该页面的标签
     * 
     * @return 页面标签
     */
    @Nullable
    public String getTarget() {
        return target;
    }

    /**
     * 获取该页面的页码
     * 
     * @return 页码
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * 获取页面链表的下一个对象
     * 
     * @return 下一页
     */
    @Nullable
    public Page getNext() {
        return next;
    }

    /**
     * 获取页面链表的上一个对象
     * 
     * @return 上一页
     */
    @Nullable
    public Page getPre() {
        return pre;
    }

    /**
     * 设置下一页
     * 
     * @param next 下一页
     */
    public void setNext(@Nullable Page next) {
        this.next = next;
    }

    /**
     * 设置上一页
     * 
     * @param pre 上一页
     */
    public void setPre(@Nullable Page pre) {
        this.pre = pre;
    }

    /**
     * 更新容器标题
     * 
     * @param title 标题
     */
    public void updateTitle(String title) {
        this.title = title;
        this.refresh = true;
        if (inventoryType == InventoryType.CHEST) {
            inventory = Bukkit.createInventory(this, size, title);
        } else {
            inventory = Bukkit.createInventory(this, inventoryType, title);
        }
    }

    /**
     * 是否需要刷新容器内容
     * 
     * @return 是否需要刷新
     */
    public boolean needRefresh() {
        return refresh;
    }

    /**
     * 标记为需要刷新
     */
    public void markRefresh() {
        this.refresh = true;
        if (next != null) {
            next.markRefresh();
        }
    }

    /**
     * 容器操作的结果
     * 
     * @param index 点击的位置
     * @param inventoryAction 容器操作事件
     * @param player 玩家
     * @param currentItem 点击的物品
     * @param cursor 指针上的物品
     * @return 事件处理结果
     */
    @Nullable
    public ActionResult action(int index, @Nonnull InventoryAction inventoryAction, 
            @Nonnull Player player, @Nullable ItemStack currentItem, @Nullable ItemStack cursor) {
        Print.VIEW_GUIDE.debug("Player {} {} on {} currentItem:{} cursor:{}", 
            player.getName(), 
            inventoryAction,
            index,
            currentItem == null ? "null" : currentItem.getType().name(),
            cursor == null ? "null" : cursor.getType().name());
        final Slot slot = slotMap.computeIfAbsent(index, k -> new Slot());
        return slot.getResult(inventoryAction);
    }

    /**
     * 使用 {@link org.bukkit.event.inventory.InventoryAction#MOVE_TO_OTHER_INVENTORY} 移动物品入容器，
     * 如果容器已满，传入的 {@code index} 为 -1
     * 
     * @param index 位置
     * @param player 玩家
     * @param currentItem 物品
     * @return 事件处理结果
     */
    public ActionResult moveIn(int index, @Nonnull Player player, @Nullable ItemStack currentItem) {
        Print.VIEW_GUIDE.debug("Player {} move item:{} on {}", 
            player.getName(), 
            currentItem,
            index);
        return new ActionResult(false, ActionResult.ACTION_NONE);
    }

    /**
     * 使用拖拽把物品放入容器 
     * 
     * @param index 位置
     * @param player 玩家
     * @param item 物品
     * @see org.bukkit.event.inventory.InventoryDragEvent
     * @return 事件处理结果
     */
    public ActionResult dragIn(int index, @Nonnull Player player, @Nullable ItemStack item) {
        Print.VIEW_GUIDE.debug("Player {} drag item:{} on {}", 
            player.getName(), 
            item,
            index);
        return new ActionResult(false, ActionResult.ACTION_NONE);
    }

    /**
     * 刷新页面，放置物品及其视图操作到容器中 {@link #slot(int, ItemStack, Slot)}。
     * 
     * @see #run()
     */
    public abstract void refreshPage();

    @Override
    public Inventory getInventory() {
        if (inventory == null) {
            this.updateTitle(title);
        }
        return inventory;
    }

    @Override
    public void run() {
        this.refresh = false;
        this.refreshPage();
    }

    /**
     * 设置槽位物品及其行为
     * 
     * @param index 位置
     * @param item 槽位展示的物品
     * @param slot 槽位的操作行为
     * @see #refreshPage()
     */
    protected void slot(int index, @Nullable ItemStack item, @Nullable Slot slot) {
        if (index < 0 || index >= size) {
            index = inventory.firstEmpty();
        }
        inventory.setItem(index, item);
        if (slot != null) {
            slotMap.put(index, slot);
        }
    }

    /**
     * 设置槽位物品及其行为，一般在 {@link #refreshPage()} 中调用
     * 
     * @param index 位置
     * @param item 槽位展示的物品
     * @see #refreshPage()
     */
    protected void slot(int index, @Nullable ItemStack item) {
        this.slot(index, item, new Slot());
    }

}
