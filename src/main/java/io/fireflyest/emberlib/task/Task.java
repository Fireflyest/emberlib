package io.fireflyest.emberlib.task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.fireflyest.emberlib.util.TextUtils;

/**
 * 任务
 * 
 * @author Fireflyest
 * @since 1.0
 */
public abstract class Task {
    
    /**
     * 该任务执行后产生的新任务
     */
    protected final List<Task> follows = new ArrayList<>();

    protected final Bundle[] bundles;

    protected UUID uid;
    protected OfflinePlayer offlinePlayer;
    protected CommandSender sender;

    /**
     * 构造任务
     * 
     * @param bundles 任务数据数组
     */
    protected Task(Bundle... bundles) {
        this.bundles = bundles;
    }

    /**
     * 构造任务
     * 
     * @param playerId 执行对象id
     * @param bundles 任务数据数组
     */
    protected Task(@Nonnull String playerId, Bundle... bundles) {
        this.bundles = bundles;

        if (TextUtils.match(TextUtils.UUID_PATTERN, playerId)) {
            uid = UUID.fromString(playerId);
            offlinePlayer = Bukkit.getOfflinePlayer(uid);
            sender = offlinePlayer.getPlayer();
        } else {
            // 向下兼容id传playerName的情况
            final Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                uid = player.getUniqueId();
                offlinePlayer = player;
                sender = player;
            }
        }

    }

    /**
     * 构造任务
     * 
     * @param uid 执行对象uid
     * @param bundles 任务数据数组
     */
    protected Task(@Nonnull UUID uid, Bundle... bundles) {
        this.bundles = bundles;
        this.uid = uid;
        offlinePlayer = Bukkit.getOfflinePlayer(uid);
        sender = offlinePlayer.getPlayer();    
    }

    /**
     * 执行任务
     */
    public abstract void execute();

    /**
     * 设置任务执行的通知对象
     * 
     * @param sender 通知对象
     */
    public void infoTarget(CommandSender sender) {
        this.sender = sender;
    }

    /**
     * 执行通知
     * 
     * @param info 执行信息
     */
    public void info(String info) {
        if (sender != null) {
            sender.sendMessage(info);
        }
    }

    /**
     * 获取执行者名称
     * 
     * @return 名称
     */
    public String getPlayerName() {
        return offlinePlayer.getName();
    }

    /**
     * 获取是否有后续的任务需要处理
     * 
     * @return 是否有后续任务
     */
    public boolean hasFollowTasks() {
        return !follows.isEmpty();
    }

    /**
     * 获取后续任务
     * 
     * @return 后续任务列表
     */
    @Nonnull
    public List<Task> followTasks() {
        return follows;
    }

}
