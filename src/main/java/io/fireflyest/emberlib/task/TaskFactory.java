package io.fireflyest.emberlib.task;

import javax.annotation.Nonnull;

/**
 * 任务工厂，由其他插件继承实现
 * 
 * @author Fireflyest
 * @since 1.0
 */
public abstract class TaskFactory<T extends Task> {
    
    /**
     * 创建一个任务等待执行
     * 
     * @see TaskHandler#putFactory(String, TaskFactory)
     * @param targetName 执行对象名称
     * @param bundles 任务数据数组
     * @return 任务
     */
    public abstract T create(@Nonnull String targetName, Bundle... bundles);

}
