package io.fireflyest.emberlib.task.core;

import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.plugin.java.JavaPlugin;
import io.fireflyest.emberlib.task.Bundle;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.task.TaskFactory;
import io.fireflyest.emberlib.task.TaskHandler;
import io.fireflyest.emberlib.task.TaskWorker;


/**
 * 工作线程处理和工作分配的实现
 * @author Fireflyest
 * @since 1.0
 */
public class TaskHandlerImpl implements TaskHandler {

    private final HashMap<String, TaskWorker> workerMap = new HashMap<>();
    private final HashMap<String, TaskFactory<?>> factoryMap = new HashMap<>();

    /**
     * 由本插件实例化后发放到服务
     */
    public TaskHandlerImpl() {
        // 
    }

    @Override
    public void createWorker(@Nonnull String name, @Nonnull JavaPlugin plugin) {
        if (workerMap.containsKey(name)) {
            workerMap.get(name).stop();
        }
        workerMap.put(name, new TaskWorker(name, plugin));        
    }

    @Override
    public void removeWorker(@Nonnull String name) {
        if (workerMap.containsKey(name)) {
            workerMap.get(name).stop();
        }
        workerMap.remove(name);
    }

    @Override
    public void putTasks(@Nonnull String worker, @Nonnull Task... tasks) {
        if (workerMap.containsKey(worker)) {
            workerMap.get(worker).add(tasks);
        }
    }

    @Override
    public void putFactory(@Nonnull String key, @Nonnull TaskFactory<?> factory) {
        factoryMap.put(key, factory);
    }

    @Override
    @Nullable
    public Task createTask(@Nonnull String key, @Nonnull String targetName, Bundle... bundles) {
        Task task = null;
        final TaskFactory<?> factory = factoryMap.get(key);
        if (factory != null) {
            task = factory.create(targetName, bundles);
        }
        return task;
    }

    @Override
    public void disable() {
        for (TaskWorker worker : workerMap.values()) {
            if (worker != null) {
                worker.stop();
            }
        }
    }

}
