package io.fireflyest.emberlib.task;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nonnull;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import io.fireflyest.emberlib.Print;

/**
 * 任务执行类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class TaskWorker {

    private final String name;
    private boolean enable;

    private BukkitTask bukkitTask;
    private final JavaPlugin plugin;
    private final ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<>();

    /**
     * 创建任务工作者
     * 
     * @param name 名称
     * @param plugin 插件
     */
    public TaskWorker(@Nonnull String name, @Nonnull JavaPlugin plugin) {
        this.name = name;
        this.plugin = plugin;

        // 开始工作
        this.start();
    }

    /**
     * 添加任务
     * 
     * @param tasks 任务
     */
    public void add(@Nonnull Task... tasks) {
        if (!enable) {
            this.start();
        }
        synchronized (taskQueue) {
            taskQueue.addAll(Arrays.asList(tasks));
            taskQueue.notifyAll();
        }
    }

    /**
     * 开始工作
     */
    public void start() {
        this.stop();

        Print.EMBER_LIB.info("The task worker '{0}' start to work!", name);
        this.enable = true;
        this.bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                loop();
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * 停止工作
     */
    public void stop() {
        // 停止循环
        enable = false;
        // 解锁
        synchronized (taskQueue) {
            taskQueue.notifyAll();
        }
        Print.EMBER_LIB.info("The task worker '{}' stop!", name);
        // 关闭线程
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
    }

    /**
     * 循环执行，执行完队列后进入等待状态
     * 
     * @see io.fireflyest.emberlib.task.Task#execute()
     */
    private void loop() {
        while (enable) {
            String taskName = "?";
            try {
                // 如果执行完，就锁住
                if (taskQueue.isEmpty()) {
                    synchronized (taskQueue) {
                        taskQueue.wait();
                    }
                }
                final Task task = taskQueue.poll();
                if (task == null) {
                    Print.EMBER_LIB.info("Task is null, Queue size: {0}", taskQueue.size());
                    continue;
                }
                taskName = task.getClass().getSimpleName();
                task.execute();
                if (task.hasFollowTasks()) {
                    taskQueue.addAll(task.followTasks());
                }
            } catch (InterruptedException e) {
                Print.EMBER_LIB.error("Error on taskQueue take, '{}' stop working!", name);
                e.printStackTrace();
                // Restore interrupted state...
                Thread.currentThread().interrupt();
                this.stop();
            } catch (Exception e) {
                Print.EMBER_LIB.error("Error on '{}' execute, '{}' stop working!", taskName, name);
                e.printStackTrace();
                this.stop();
            }
        }
    }

}
