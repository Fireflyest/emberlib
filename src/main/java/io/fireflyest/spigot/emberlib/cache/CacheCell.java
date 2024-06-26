package io.fireflyest.spigot.emberlib.cache;

import java.time.Instant;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * 缓存数据存储实现类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class CacheCell extends AbstractCell<String> {

    public CacheCell(Instant born, Instant deadline, Set<String> valueSet) {
        super(born, deadline, valueSet);
    }

    public CacheCell(long expire, String value) {
        super(expire, value);
    }

    public CacheCell(long expire, Set<String> valueSet) {
        super(expire, valueSet);
    }

    /**
     * 获取起始时间
     * @return 起始时间
     */
    public Instant getBorn() {
        return born;
    }

    /**
     * 获取失效时间
     * @return 失效时间
     */
    @Nullable
    public Instant getDeadline() {
        return deadline;
    }
    
}
