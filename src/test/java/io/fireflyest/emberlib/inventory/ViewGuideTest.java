package io.fireflyest.emberlib.inventory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * 视图导航测试
 * 
 * @since 1.0
 */
public class ViewGuideTest {
    
    @Test
    public void performanceComparison() {
        // 生成一个包含1000000个整数的列表
        final List<Integer> numbers = IntStream.rangeClosed(1, 100)
            .boxed().collect(Collectors.toList());

        // 使用for循环
        long startTime = System.nanoTime();
        for (Integer number : numbers) {
            // 模拟一些处理
            int result = number * 2;
        }
        long endTime = System.nanoTime();
        System.out.println("For loop: " + (endTime - startTime) / 1_000_000.0 + " ms");

        // 使用parallelStream().forEach
        startTime = System.nanoTime();
        numbers.forEach(number -> {
            // 模拟一些处理
            int result = number * 2;
        });
        endTime = System.nanoTime();
        System.out.println("forEach: " + (endTime - startTime) / 1_000_000.0 + " ms");

        assertTrue(true);
    }

}
