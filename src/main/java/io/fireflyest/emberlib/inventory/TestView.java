package io.fireflyest.emberlib.inventory;

import javax.annotation.Nullable;

/**
 * 测试视图
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class TestView extends View {

    @Override
    @Nullable
    public Page getHomePage(@Nullable Object target) {
        return pagesMap.computeIfAbsent(target, k -> new TestPage(target, 0, 9));
    }
    
}
