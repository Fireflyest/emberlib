package io.fireflyest.emberlib.inventory;

import javax.annotation.Nullable;

public class TestView extends View {

    @Override
    @Nullable
    public Page getHomePage(@Nullable String target) {
        return pagesMap.computeIfAbsent(target, k -> new TestPage(target, 0, 54));
    }
    
}
