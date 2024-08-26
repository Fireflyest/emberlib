package io.fireflyest.emberlib.inventory;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * 视图
 * 
 * @author Fireflyest
 * @since 1.0
 */
public abstract class View {
    
    protected final Map<String, ViewPage> pagesMap = new HashMap<>();

    /**
     * 获取某个页面链表的表头，即首页
     * 
     * @param target 目标页面名称
     * @return 展示页面
     */
    @Nullable
    public abstract ViewPage getHomePage(@Nullable String target);

    /**
     * 删除某个页面链表
     * 
     * @param target 目标页面名称
     */
    public void removePages(@Nullable String target) {
        pagesMap.remove(target);
    }

}
