package io.fireflyest.emberlib.message;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * 通知中心实现类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class NotificationImpl implements Notification {

    private final Map<String, BoardMessage> boardMap = new HashMap<>();
    private final Map<String, Instant> popMap = new HashMap<>();

    private final JavaPlugin plugin;

    public NotificationImpl(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void addBoard(@Nonnull String name, @Nonnull BoardMessage boardMessage) {
        boardMap.put(name, boardMessage);
    }

    @Override
    public void removeBoard(@Nonnull String name) {
        boardMap.remove(name);
    }

    @Override
    public void pushAllBoard(@Nonnull String entry, int score, boolean decay) {
        for (BoardMessage board : boardMap.values()) {
            board.set(BoardMessage.OBJECTIVE_NOTIFY, entry, score, decay);
        }
    }

    @Override
    public void pushSomeBoard(@Nonnull String entry, int score, boolean decay, 
                              @Nonnull Collection<String> names) {
        for (String name : names) {
            pushOneBoard(entry, score, decay, name);
        }
    }

    @Override
    public void pushOneBoard(@Nonnull String entry, int score, boolean decay, 
                             @Nonnull String name) {
        final BoardMessage board = boardMap.get(name);
        if (name != null) {
            board.set(BoardMessage.OBJECTIVE_NOTIFY, entry, score, decay);
        }
    }

    @Override
    public void popAll(@Nonnull String message, int exist) {
        this.popSome(message, exist, Bukkit.getOnlinePlayers());
    }

    @Override
    public void popSome(@Nonnull String message, int exist, 
                        @Nonnull Collection<? extends Player> players) {
        for (Player player : players) {
            this.popOne(message, exist, player);
        }
    }

    @Override
    public void popOne(@Nonnull String message, int exist, 
                       @Nonnull Player player) {
        final String playerName = player.getName();
        final TextComponent text = new TextComponent(message);
        // 下次可弹出时间
        if (!popMap.containsKey(playerName) || popMap.get(playerName).isBefore(Instant.now())) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
            popMap.put(playerName, Instant.now().plusMillis(exist));
        } else {
            // 上一个消息结束的时间点
            final long timePoint = 
                ChronoUnit.MILLIS.between(Instant.now(), popMap.get(playerName)) / 50;
            // 延迟到上一个消息结束后发送
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
                }
            }.runTaskLater(plugin, timePoint);
            // 可发送时间再推迟
            popMap.put(playerName, popMap.get(playerName).plusMillis(exist));
        }
    }

    @Override
    public void titleAll(@Nonnull String title, @Nonnull String subtitle, int exist) {
        this.titleSome(title, subtitle, exist, Bukkit.getOnlinePlayers());
    }

    @Override
    public void titleSome(@Nonnull String title, 
                          @Nonnull String subtitle, int exist, 
                          @Nonnull Collection<? extends Player> players) {
        for (Player player : players) {
            this.titleOne(title, subtitle, exist, player);
        }
    }

    @Override
    public void titleOne(@Nonnull String title, 
                         @Nonnull String subtitle, int exist, 
                         @Nonnull Player player) {
        player.sendTitle(title, subtitle, 10, exist, 20);
    }

    @Override
    public void sendAll(@Nonnull BaseComponent[] message) {
        Bukkit.spigot().broadcast(message);
    }

    @Override
    public void sendSome(@Nonnull BaseComponent[] message, 
                         @Nonnull Collection<? extends Player> players) {
        for (Player player : players) {
            this.sendOne(message, player);
        }
    }

    @Override
    public void sendOne(@Nonnull BaseComponent[] message, 
                        @Nonnull Player player) {
        player.spigot().sendMessage(message);
    }
    
}
