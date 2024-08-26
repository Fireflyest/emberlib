package io.fireflyest.emberlib.inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * 一个页面节点
 * 
 * @author Fireflyest
 * @since 1.0
 */
public abstract class ViewPage {
    
    /**
     * 固定按钮，快速显示
     */
    protected final Map<Integer, ItemStack> buttonMap = new ConcurrentHashMap<>(54);

    /**
     * 全部按钮的缓存，点击时返回被点击物品
     */
    protected final Map<Integer, ItemStack> asyncButtonMap = new ConcurrentHashMap<>(54);

    /**
     * 页面所属名称
     */
    protected final String target;
    
    /**
     * 当前页面页码
     */
    protected final int page;

    /**
     * 容器大小
     */
    protected final int size;

    /**
     * 界面载体
     */
    protected Inventory inventory;

    /**
     * 标题
     */
    protected String title;

    /**
     * 下一页
     */
    protected ViewPage next = null;

    /**
     * 上一页
     */
    protected ViewPage pre = null;

    /**
     * 一个页面节点
     * 
     * @param target 页面所属
     * @param page 页码
     * @param size 容器大小
     */
    protected ViewPage(String target, int page, int size) {
        this.target = target;
        this.page = page;
        this.size = size;
    }

    /**
     * 获取全部按钮，异步展示
     * 
     * @return 动态按钮
     */
    @Nonnull
    public Map<Integer, ItemStack> getItemMap() {
        return buttonMap;
    }

    /**
     * 获取固定物品按钮，直接展示
     * 
     * @return 固定按钮
     */
    @Nonnull
    public Map<Integer, ItemStack> getButtonMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);
        return asyncButtonMap;
    }

    /**
     * 点击监听获取用户所点击物品
     * 
     * @param slot 格子
     * @return 物品
     */
    @Nullable
    public ItemStack getItem(int slot) {
        return asyncButtonMap.get(slot);
    }

    /**
     * 获取页面容器，用于玩家打开
     * 
     * @return 页面容器
     */
    @Nonnull
    public Inventory getInventory() {
        if (inventory == null) {
            inventory = Bukkit.createInventory(null, size, title);
        }
        return inventory;
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
    public int getPage() {
        return page;
    }

    /**
     * 获取页面链表的下一个对象
     * 
     * @return 下一页
     */
    @Nullable
    public ViewPage getNext() {
        return next;
    }

    /**
     * 获取页面链表的上一个对象
     * 
     * @return 上一页
     */
    @Nullable
    public ViewPage getPre() {
        return pre;
    }

    /**
     * 设置下一页
     * 
     * @param next 下一页
     */
    public void setNext(@Nullable ViewPage next) {
        this.next = next;
    }

    /**
     * 设置上一页
     * 
     * @param pre 上一页
     */
    public void setPre(@Nullable ViewPage pre) {
        this.pre = pre;
    }

    /**
     * 刷新，可能异步
     */
    public abstract void refreshPage();

    /**
     * 更新容器标题
     * 
     * @param title 标题
     */
    public void updateTitle(String title) {
        this.title = title;
        this.inventory = Bukkit.createInventory(null, size, title);
    }

}
