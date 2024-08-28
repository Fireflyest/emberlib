package io.fireflyest.emberlib.message;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * 用计分榜消息通知
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class BoardMessage {

    /**
     * 通知
     */
    public static final String OBJECTIVE_NOTIFY = "notify";

    /**
     * 进度
     */
    public static final String OBJECTIVE_PROGRESS = "progress";

    private final Map<String, BukkitTask> decayMap = new HashMap<>();

    private final JavaPlugin plugin;
    private final Scoreboard scoreboard;

    public BoardMessage(JavaPlugin plugin, Scoreboard scoreboard) {
        this.plugin = plugin;
        this.scoreboard = scoreboard;
    }
    
    /**
     * 注册记分板上的记分项
     * 
     * @param name 名称
     * @param criteria 记分标准
     * @param displayName 显示名称
     * @param displaySlot 显示位置
     * @return 本身
     */
    public BoardMessage registerObjective(
        @Nonnull String name,
        @Nonnull String criteria,
        @Nonnull String displayName,
        @Nonnull DisplaySlot displaySlot
    ) {
        Objective objective = scoreboard.getObjective(name);
        if (objective != null) {
            objective.unregister();
        }
        objective = scoreboard.registerNewObjective(name, criteria, displayName);
        objective.setDisplaySlot(displaySlot);
        return this;
    }

    /**
     * 设置一个计分
     * 
     * @param objective 记分项名称
     * @param entry 条目
     * @param score 分数
     */
    public void set(String objective, String entry, int score) {
        this.set(objective, entry, score, false);
    }

    /**
     * 设置一个计分
     * 
     * @param objective 记分项名称
     * @param entry 条目
     * @param score 分数
     */
    public void set(String objective, String entry, int score, boolean decay) {
        final Objective obj = scoreboard.getObjective(objective);
        if (obj == null) {
            return;
        }
        final Score entryScore = scoreboard.getObjective(objective).getScore(entry);
        entryScore.setScore(score);
        // 计分衰减
        if (decay && (!decayMap.containsKey(entry) || decayMap.get(entry).isCancelled())) {
            final BukkitTask decayTask = new BukkitRunnable() {
                @Override
                public void run() {
                    entryScore.setScore(entryScore.getScore() - 1);
                    if (entryScore.getScore() <= 0) {
                        scoreboard.resetScores(entry);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 20L, 20L);
            decayMap.put(entry, decayTask);
        } 
    }

}
