package io.fireflyest.emberlib.message;

import java.util.Collection;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * 通知中心，发布各种消息
 * 
 * @author Fireflyest
 * @since 1.0
 */
public interface Notification {

    /**
     * 添加计分榜
     * 
     * @param name 名称
     * @param boardMessage 计分榜
     */
    void addBoard(@Nonnull String name, @Nonnull BoardMessage boardMessage);

    /**
     * 删除计分榜
     * 
     * @param name 名称
     */
    void removeBoard(@Nonnull String name);

    /**
     * 推送全计分榜消息
     * 
     * @param entry 条目
     * @param score 数值
     * @param decay 递减
     */
    void pushAllBoard(@Nonnull String entry, int score, boolean decay);

    /**
     * 推送多个计分榜消息
     * 
     * @param entry 条目
     * @param score 数值
     * @param decay 递减
     * @param names 多个计分榜名称
     */
    void pushSomeBoard(@Nonnull String entry, int score, boolean decay, 
                       @Nonnull Collection<String> names);

    /**
     * 推送单个计分榜消息
     * 
     * @param entry 条目
     * @param score 数值
     * @param decay 递减
     * @param name 计分榜名称
     */
    void pushOneBoard(@Nonnull String entry, int score, boolean decay, 
                      @Nonnull String name);

    /**
     * 所有玩家弹出消息
     * 
     * @param message 消息
     * @param exist 存在时间
     */
    void popAll(@Nonnull String message, int exist);

    /**
     * 多个玩家弹出消息
     * 
     * @param message 消息
     * @param exist 存在时间
     * @param players 多个玩家
     */
    void popSome(@Nonnull String message, int exist, 
                 @Nonnull Collection<? extends Player> players);

    /**
     * 单个玩家弹出消息
     * 
     * @param message 消息
     * @param exist 存在时间
     * @param player 玩家
     */
    void popOne(@Nonnull String message, int exist, 
                @Nonnull Player player);

    /**
     * 所有玩家弹出消息
     * 
     * @param title 大标题
     * @param subtitle 小标题
     * @param exist 存在时间
     */
    void titleAll(@Nonnull String title, @Nonnull String subtitle, int exist);

    /**
     * 多个玩家弹出消息
     * 
     * @param title 大标题
     * @param subtitle 小标题
     * @param exist 存在时间
     * @param players 多个玩家
     */
    void titleSome(@Nonnull String title, 
                   @Nonnull String subtitle, int exist, 
                   @Nonnull Collection<? extends Player> players);

    /**
     * 单个玩家弹出消息
     * 
     * @param title 大标题
     * @param subtitle 小标题
     * @param exist 存在时间
     * @param player 玩家
     */
    void titleOne(@Nonnull String title, 
                  @Nonnull String subtitle, int exist, 
                  @Nonnull Player player);

    /**
     * 发送消息给所有玩家
     * 
     * @param message 消息
     */
    void sendAll(@Nonnull BaseComponent[] message);

    /**
     * 发消息给多个玩家
     * 
     * @param message 消息
     * @param players 多个玩家
     */
    void sendSome(@Nonnull BaseComponent[] message, @Nonnull Collection<? extends Player> players);

    /**
     * 发送消息给单个玩家
     * 
     * @param message 消息
     * @param player 玩家
     */
    void sendOne(@Nonnull BaseComponent[] message, @Nonnull Player player);

}
