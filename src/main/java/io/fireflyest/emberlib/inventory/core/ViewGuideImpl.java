package io.fireflyest.emberlib.inventory.core;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.Print;
import io.fireflyest.emberlib.data.Pair;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.ActionResult;
import io.fireflyest.emberlib.inventory.View;
import io.fireflyest.emberlib.inventory.ViewGuide;

/**
 * 视图导航实现类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class ViewGuideImpl implements ViewGuide, Listener {

    /**
     * 所有视图 viewName -> view
     */
    private final Map<String, View> viewMap = new ConcurrentHashMap<>();

    /**
     * 玩家正在浏览的界面名称 playerName -> viewName
     */
    public final Map<String, String> viewingMap = new ConcurrentHashMap<>();
    /**
     * 正在被浏览的页面 viewName -> playerNameSet
     */
    public final Map<String, Set<String>> viewerMap = new ConcurrentHashMap<>();

    /**
     * 跳转页面，玩家会先打开页面，再关闭原有页面，为了防止取消使用记录，这里记录重定向
     */
    private final Set<String> viewRedirect = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * 浏览记录，先进后出
     */
    private final Map<String, Deque<Page>> viewUsdMap = new ConcurrentHashMap<>();

    /**
     * 刷新任务
     */
    private final Map<Page, BukkitTask> refreshTaskMap = new ConcurrentHashMap<>();

    private final JavaPlugin plugin;

    public ViewGuideImpl(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void addView(@Nonnull String viewName, @Nonnull View view) {
        viewMap.put(viewName, view);
        Print.VIEW_GUIDE.debug("Add view {} class {}", viewName, view.getClass().getName());
    }

    @Override
    public void removeView(@Nonnull String viewName) {
        viewerMap.remove(viewName);
        final Iterator<Entry<String, String>> iterator = viewingMap.entrySet().iterator();
        // 关闭所有正在浏览该视图的玩家所打开的页面
        while (iterator.hasNext()) {
            final Entry<String, String> entry = iterator.next();
            if (viewName.equals(entry.getValue())) {
                iterator.remove();
                final Player player = Bukkit.getPlayerExact(entry.getKey());
                if (player != null) {
                    player.closeInventory();
                }
            }
        }
        viewMap.remove(viewName);
    }

    @Override
    public void closeView(@Nonnull String playerName) {
        if (!viewingMap.containsKey(playerName)) {
            return;
        }
        if (viewRedirect.contains(playerName)) {
            Print.VIEW_GUIDE.debug("Player {} redirect view", playerName);
            viewRedirect.remove(playerName);
        } else {
            Print.VIEW_GUIDE.debug("Player {} close view", playerName);
            final String viewName = viewingMap.get(playerName);
            final Set<String> viewers = viewerMap.computeIfAbsent(viewName, k -> new HashSet<>());
            viewers.remove(playerName);
            viewingMap.remove(playerName);
            viewUsdMap.remove(playerName);
        }
    }

    @Override
    public void openView(@Nonnull Player player, 
                         @Nonnull String viewName, 
                         @Nullable String target) {
        final String playerName = player.getName();
        final Deque<Page> backStack = 
            viewUsdMap.computeIfAbsent(playerName, k -> new ArrayDeque<>());
        // 已有打开的界面，存在浏览记录
        if (viewingMap.containsKey(playerName)) {
            final InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
            if (holder instanceof Page) {
                final Page page = (Page) holder;
                Print.VIEW_GUIDE.debug("Player {} store page", playerName);
                viewRedirect.add(playerName);
                backStack.push(page);
            }
        }
        // 判断视图是否存在
        if (!viewMap.containsKey(viewName)) {
            Print.VIEW_GUIDE.warn("View {} does not exist.", viewName);
            return;
        }
        // 获取视图的首页
        final Page page = viewMap.get(viewName).getHomePage(target);
        // 首页是否存在
        if (page == null) {
            Print.VIEW_GUIDE.warn("The first page of the {} does not exist.", viewName);
            return;
        }
        // 设置玩家正在浏览的视图
        Print.VIEW_GUIDE.debug("Player {} open page {}.{}", playerName, viewName, target);
        viewingMap.put(playerName, viewName);
        viewerMap.computeIfAbsent(viewName, k -> new HashSet<>()).add(playerName);
        // 打开容器
        if (page.needRefresh()) {
            this.refreshPageTask(page);
        }
        player.openInventory(page.getInventory());
    }

    @Override
    @Nullable
    public Page getUsingPage(String playerName) {
        Page page = null;
        final Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            return page;
        }
        final InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
        if (holder instanceof Page) {
            page = (Page) holder;
        }
        return page;
    }

    @Override
    @Nullable
    public String getUsingView(String playerName) {
        return viewingMap.get(playerName);
    }

    @Override
    public void nextPage(@Nonnull Player player) {
        final String playerName = player.getName();
        if (!viewingMap.containsKey(playerName)) {
            return;
        }
        final InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
        if (holder instanceof Page) {
            final Page nextPage = ((Page) holder).getNext();
            if (nextPage != null) {
                Print.VIEW_GUIDE.debug("Player {} page next", playerName);
                viewRedirect.add(playerName);
                if (nextPage.needRefresh()) {
                    this.refreshPageTask(nextPage);
                }
                player.openInventory(nextPage.getInventory());
            }
        }
    }

    @Override
    public void prePage(@Nonnull Player player) {
        final String playerName = player.getName();
        if (!viewingMap.containsKey(playerName)) {
            return;
        }
        final InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
        if (holder instanceof Page) {
            final Page prePage = ((Page) holder).getPre();
            if (prePage != null) {
                Print.VIEW_GUIDE.debug("Player {} page pre", playerName);
                viewRedirect.add(playerName);
                if (prePage.needRefresh()) {
                    this.refreshPageTask(prePage);
                }
                player.openInventory(prePage.getInventory());
            }
        }
    }

    @Override
    public void back(@Nonnull Player player) {
        final String playerName = player.getName();
        if (!viewingMap.containsKey(playerName)) {
            return;
        }
        final Deque<Page> backStack = 
            viewUsdMap.computeIfAbsent(playerName, k -> new ArrayDeque<>());
        if (backStack.isEmpty()) {
            player.closeInventory();
            return;
        }
        // 取出
        final Page backPage = backStack.pop();
        Print.VIEW_GUIDE.debug("Player {} page back", playerName);
        viewRedirect.add(playerName);
        if (backPage.needRefresh()) {
            this.refreshPageTask(backPage);
        }
        player.openInventory(backPage.getInventory());
    }

    @Override
    public void jump(@Nonnull Player player, int pageNumber) {
        final String playerName = player.getName();
        if (!viewingMap.containsKey(playerName)) {
            return;
        }
        final InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
        if (holder instanceof Page) {
            final Page usingPage = (Page) holder;
            Page jumpPage = usingPage;
            final int delta = pageNumber - usingPage.getPageNumber();
            for (int i = 0; i < Math.abs(delta); i++) {
                final Page tempPage = delta > 0 ? jumpPage.getNext() : jumpPage.getPre();
                if (tempPage != null) {
                    jumpPage = tempPage;
                } else {
                    break;
                }
            }
            Print.VIEW_GUIDE.debug("Player {} page jump", playerName);
            viewRedirect.add(playerName);
            if (jumpPage.needRefresh()) {
                this.refreshPageTask(jumpPage);
            }
            player.openInventory(jumpPage.getInventory());
        }
    }

    @Override
    public void refreshPage(boolean singlePage, @Nonnull String... playerNames) {
        for (String playerName : playerNames) {
            // 是否浏览者
            if (!viewingMap.containsKey(playerName)) {
                continue;
            }
            Print.VIEW_GUIDE.debug("Player {} refresh", playerName);
            final Page usingPage = this.getUsingPage(playerName);
            if (usingPage != null) {
                if (!singlePage) {
                    usingPage.markRefresh();
                }
                this.refreshPageTask(usingPage);
            }
        }
    }

    @Override
    public void refreshPages(@Nonnull String viewName, @Nonnull String... targets) {
        // 判断视图是否存在
        if (!viewMap.containsKey(viewName)) {
            Print.VIEW_GUIDE.warn("View {} does not exist.", viewName);
            return;
        }
        // 获取视图的首页，标记需要刷新，当有玩家打开时就触发刷新
        if (targets.length == 0) {
            this.refreshView(viewName);
        } else {
            final Set<String> targetSet = new HashSet<>(Arrays.asList(targets));
            Print.VIEW_GUIDE.debug("Pages {}.{} refresh", viewName, targets);
            // 标记需要刷新
            for (String target : targets) {
                final Page homePage = viewMap.get(viewName).getHomePage(target);
                if (homePage != null) {
                    homePage.markRefresh();
                }
            }
            // 刷新正在浏览的玩家
            for (String playerName : viewerMap.computeIfAbsent(viewName, k -> new HashSet<>())) {
                final Page usingPage = this.getUsingPage(playerName);
                if (usingPage != null && targetSet.contains(usingPage.getTarget())) {
                    this.refreshPageTask(usingPage);
                }
            }
        }
    }

    @Override
    public void updateButton(@Nonnull Player player, int slot, @Nonnull ItemStack buttonItem) {
        final String playerName = player.getName();
        if (!viewingMap.containsKey(playerName)) {
            return;
        }
        final Page usingPage = this.getUsingPage(playerName);
        if (usingPage != null) {
            Print.VIEW_GUIDE.debug("Player {} update button at {}", playerName, slot);
            final Inventory inventory = usingPage.getInventory();
            inventory.setItem(slot, buttonItem);
        }
    }

    @Override
    public Set<String> getViewers() {
        return viewingMap.keySet();
    }

    /**
     * 关闭
     */
    public void disable() {
        // 关闭所有正在浏览，防止容器内物品被操作
        final Iterator<Entry<String, String>> iterator = viewingMap.entrySet().iterator();
        while (iterator.hasNext()) {
            final Entry<String, String> entry = iterator.next();
            iterator.remove();
            final Player player = Bukkit.getPlayerExact(entry.getKey());
            if (player != null) {
                player.closeInventory();
            }
        }
        // 清空视图
        viewMap.clear();
        viewerMap.clear();
        // 取消所有刷新任务
        for (BukkitTask refreshTask : refreshTaskMap.values()) {
            refreshTask.cancel();
        }
    }

    /**
     * 玩家关闭容器，说明一次浏览结束
     * 
     * @param event 容器关闭事件
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        this.closeView(event.getPlayer().getName());
    }

    /**
     * 玩家离线
     * 
     * @param event 离线事件
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.closeView(event.getPlayer().getName());
    }

    /**
     * 容器点击事件
     * 
     * @param event 容器点击事件
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = ((Player) event.getWhoClicked());
        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !viewingMap.containsKey(player.getName())) {
            return;
        }
        final InventoryAction inventoryAction = event.getAction();
        final ItemStack currentItem = event.getCurrentItem();
        final ItemStack cursor = event.getCursor();
        final InventoryHolder holder = clickedInventory.getHolder();
        ActionResult result = new ActionResult(false, ActionResult.ACTION_NONE);
        Page page = null;
        if (holder instanceof Page) {
            // 点击的是视图
            page = (Page) holder;
            try {
                result = page.action(event.getSlot(), inventoryAction, player, currentItem, cursor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (inventoryAction == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            // 玩家点击的是自己的背包，并把背包物品移动到视图容器内
            final Inventory inventory = player.getOpenInventory().getTopInventory();
            final InventoryHolder inventoryHolder = inventory.getHolder();
            Validate.notNull(currentItem);
            page = (Page) inventoryHolder;
            result = this.processMove(inventory, player, page, currentItem);
        } else if (inventoryAction == InventoryAction.COLLECT_TO_CURSOR) {
            // 玩家点击的是自己的背包，并双击收集物品
            event.setCancelled(true);
        }
        
        if (page != null) {
            if (!result.isAllow()) {
                event.setCancelled(true);
            }
            this.processResult(result, player, page);
        }
    }

    /**
     * 监听玩家在容器中拖动物品，将物品分散到各个格的时候的事件
     * 
     * @param event 物品拖动事件
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        final Player player = ((Player) event.getWhoClicked());
        final Inventory inventory = event.getInventory();
        if (inventory == null || !viewingMap.containsKey(player.getName())) {
            return;
        }
        final InventoryHolder holder = inventory.getHolder();
        if (holder instanceof Page) {
            // 点击的是视图
            final Set<Integer> slotSet = new HashSet<>();
            for (Integer key : event.getNewItems().keySet()) {
                if (key >= 0 && key < inventory.getSize()) {
                    slotSet.add(key);
                }
            }
            this.processDrag(slotSet, inventory, player);
        }
    }

    /**
     * 处理移动物品
     * 
     * @param inventory 容器
     * @param player 玩家
     * @param page 页面
     * @param currentItem 物品
     * @return 处理结果
     */
    @Nonnull
    private ActionResult processMove(@Nonnull Inventory inventory, 
                                     @Nonnull Player player,
                                     @Nonnull Page page,
                                     @Nonnull ItemStack currentItem) {
        final int maxStackSize = currentItem.getMaxStackSize();
        ActionResult result = new ActionResult(false, ActionResult.ACTION_NONE);
        int slot = inventory.first(currentItem.getType());
        if (slot == -1 || inventory.getItem(slot).getAmount() >= maxStackSize
                        || !inventory.getItem(slot).isSimilar(currentItem)) {
            slot = inventory.firstEmpty();
        }
        try {
            result = page.moveIn(slot, player, currentItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 处理拖拽结果
     * 
     * @param slotSet 涉及的槽位集合
     * @param inventory 容器
     * @param player 玩家
     */
    private void processDrag(@Nonnull Set<Integer> slotSet, 
                             @Nonnull Inventory inventory, 
                             @Nonnull Player player) {
        if (slotSet.isEmpty()) {
            return;
        }
        final Page page = (Page) inventory.getHolder();
        new BukkitRunnable() {
            @Override
            public void run() {
                slotSet.forEach(slot -> {
                    final ItemStack item = inventory.getItem(slot);
                    ActionResult result = new ActionResult(false, ActionResult.ACTION_NONE);
                    try {
                        result = page.dragIn(slot, player, item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (result == null || !result.isAllow()) {
                        inventory.setItem(slot, null);
                        player.getInventory().addItem(item);
                    }
                    processResult(result, player, page);
                });
            }
        }.runTask(plugin);
    }

    /**
     * 处理返回结果
     * 
     * @param result 结果
     * @param player 玩家
     * @param page 页面
     */
    private void processResult(@Nonnull ActionResult result, 
                               @Nonnull Player player, 
                               @Nonnull Page page) {

        final String playerName = player.getName();
        final Sound sound = result.getSound();
        if (sound != null) {
            player.playSound(player.getLocation(), sound, 1F, 1F);
        }
        // 处理结果中的所有行为
        for (Pair<Integer, String> action : result.getActions()) {
            final String value = action.second();
            switch (action.first()) {
                case ActionResult.ACTION_BACK:
                    this.back(player);
                    break;
                case ActionResult.ACTION_REFRESH:
                    page.runnable().runTaskAsynchronously(plugin);
                    break;
                case ActionResult.ACTION_CLOSE:
                    player.closeInventory();
                    break;
                case ActionResult.ACTION_PAGE_NEXT:
                    this.nextPage(player);
                    break;
                case ActionResult.ACTION_PAGE_PRE:
                    this.prePage(player);
                    break;
                case ActionResult.ACTION_PAGE_JUMP:
                    this.jump(player, NumberConversions.toInt(value));
                    break;
                case ActionResult.ACTION_PAGE_OPEN:
                    if (value != null && value.contains(".")) {
                        final String view = value.substring(0, value.lastIndexOf("."));
                        final String pageTarget = value.substring(value.lastIndexOf(".") + 1);
                        this.openView(player, view, pageTarget);
                    }
                    break;
                case ActionResult.ACTION_CONSOLE_COMMAND:
                    if (StringUtils.isNotEmpty(value)) {
                        final String command = value.replace("%player%", playerName);
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                    break;
                case ActionResult.ACTION_PLAYER_COMMAND:
                    if (StringUtils.isNotEmpty(value)) {
                        final String playerCommand = value.replace("%player%", playerName);
                        player.performCommand(playerCommand);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 刷新视图
     * 
     * @param viewName 视图名称
     */
    private void refreshView(String viewName) {
        // 不传入目标，刷新所有页面
        for (Page homePage : viewMap.get(viewName).getHomePages()) {
            homePage.markRefresh();
        }
        // 刷新正在浏览的玩家
        for (String playerName : viewerMap.computeIfAbsent(viewName, k -> new HashSet<>())) {
            final Page usingPage = this.getUsingPage(playerName);
            if (usingPage != null) {
                this.refreshPageTask(usingPage);
            }
        }
    }

    /**
     * 刷新页面
     * 
     * @param page 页面
     */
    private void refreshPageTask(@Nonnull Page page) {
        // 获取旧的刷新任务
        BukkitTask refreshTask = refreshTaskMap.get(page);
        // 如果没有刷新任务或者刷新完毕，重新运行任务
        if (refreshTask == null || refreshTask.isCancelled()) {
            final int interval = page.getRefreshInterval();
            if (interval < 0) {
                page.runnable().runTaskAsynchronously(plugin);
            } else {
                refreshTask = page.runnable().runTaskTimerAsynchronously(plugin, 0, interval);
                refreshTaskMap.put(page, refreshTask);
            }
        }
    }

}
