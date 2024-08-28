package io.fireflyest.emberlib.task;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

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

    protected final String targetName;
    protected final Bundle[] bundles;

    protected CommandSender sender;

    /**
     * 构造任务
     * 
     * @param targetName 执行对象名称
     * @param bundles 任务数据数组
     */
    protected Task(@Nonnull String targetName, Bundle... bundles) {
        this.targetName = targetName;
        this.bundles = bundles;

        this.sender = Bukkit.getPlayerExact(targetName);
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
    public String getTargetName() {
        return targetName;
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
