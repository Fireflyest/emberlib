package io.fireflyest.emberlib.task.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 工作线程管理和任务分发
 * 
 * @author Fireflyest
 * @since 1.0
 */
public interface TaskHandler {

    /**
     * 新建一个工作线程
     * 
     * @param name 名称
     * @param plugin 插件
     */
    void createWorker(@Nonnull String name, @Nonnull JavaPlugin plugin);

    /**
     * 关闭某个工作线程
     * 
     * @param name 名称
     */
    void removeWorker(@Nonnull String name);

    /**
     * 布置任务到工作线程
     * 
     * @param worker 工作线程名称
     * @param tasks 任务
     */
    void putTasks(@Nonnull String worker, @Nonnull Task... tasks);

    /**
     * 添加任务工厂
     * 
     * @param key 工厂名称
     * @param factory 任务工厂
     */
    void putFactory(@Nonnull String key, @Nonnull TaskFactory<?> factory);

    /**
     * 创建任务
     * 
     * @param key 工厂名称
     * @param targetName 执行对象名称
     * @param bundles 任务数据
     * @return 任务
     */
    @Nullable
    Task createTask(@Nonnull String key, @Nonnull String targetName, Bundle... bundles);

    /**
     * 关闭
     */
    void disable();

}
