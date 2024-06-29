package io.fireflyest.spigot.emberlib.cache;

import java.time.Instant;
import java.util.Set;

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

}
