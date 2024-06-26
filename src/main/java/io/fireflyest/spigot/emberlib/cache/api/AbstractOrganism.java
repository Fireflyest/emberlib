package io.fireflyest.spigot.emberlib.cache.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 数据缓存组织抽象类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public abstract class AbstractOrganism<K, V> implements Organism<K, V> {

    protected final String name;
    protected final Map<K, AbstractCell<V>> cacheMap;
    protected final Random random = new Random();

    /**
     * 数据组织构造函数，使用普通的HashMap
     * @param name 作为保存时的文件名称
     */
    protected AbstractOrganism(String name) {
        this(name, false);
    }

    /**
     * 数据组织构造函数
     * @param name 作为保存时的文件名称
     * @param concurrent 是否使用线程安全的Map
     */
    protected AbstractOrganism(String name, boolean concurrent) {
        this.name = name;
        this.cacheMap = concurrent ? new ConcurrentHashMap<>() : new HashMap<>();
    }

    @Override
    public void del(@Nonnull K key) {
        cacheMap.remove(key);
    }

    @Override
    public void expire(@Nonnull K key, int ms) {
        final AbstractCell<V> cell = cacheMap.get(key);
        if (cell != null && cell.get() != null) {
            cell.expire(ms);
        }
    }

    @Override
    public boolean exist(@Nonnull K key) {
        return cacheMap.containsKey(key) && cacheMap.get(key) != null;
    }

    @Override
    public void persist(@Nonnull K key) {
        final AbstractCell<V> cell = cacheMap.get(key);
        if (cell != null && cell.get() != null) {
            cell.persist();
        }
    }

    @Override
    public long ttl(@Nonnull K key) {
        final AbstractCell<V> cell = cacheMap.get(key);
        long ms = 0;
        if (cell != null && cell.get() != null) {
            ms = cell.ttl();
        }
        return ms;
    }

    @Override
    @Nullable
    public V get(@Nonnull K key) {
        return cacheMap.containsKey(key) ? cacheMap.get(key).get() : null;
    }

    @Override
    public long age(@Nonnull K key) {
        final AbstractCell<V> cell = cacheMap.get(key);
        long age = 0;
        if (cell != null && cell.get() != null) {
            age = cell.age();
        }
        return age;
    }

    @Override
    @Nullable
    public Set<V> smembers(@Nonnull K key) {
        return cacheMap.containsKey(key) ? cacheMap.get(key).getAll() : null;
    }

    @Override
    public void srem(@Nonnull K key, V value) {
        final AbstractCell<V> cell = cacheMap.get(key);
        Set<V> valueSet = null;
        if (cell != null && (valueSet = cell.getAll()) != null) {
            valueSet.remove(value);
        }
    }

    @Override
    @Nullable
    public V spop(@Nonnull K key) {
        final AbstractCell<V> cell = cacheMap.get(key);
        Set<V> valueSet = null;
        V value = null;
        if (cell != null && (valueSet = cell.getAll()) != null) {
            final int size;
            if ((size = valueSet.size()) == 0) {
                return value;
            }
            final Iterator<V> iterator = valueSet.iterator();
            int randomInt = random.nextInt(size);
            while (iterator.hasNext()) {
                value = iterator.next();
                if (randomInt-- == 0) {
                    iterator.remove();
                    break;
                }
            }
        }
        return value;
    }

    @Override
    public int scard(@Nonnull K key) {
        final AbstractCell<V> cell = cacheMap.get(key);
        Set<V> valueSet = null;
        int size = 0;
        if (cell != null && (valueSet = cell.getAll()) != null) {
            size = valueSet.size();
        }
        return size;
    }

    @Override
    public Set<K> keySet() {
        return cacheMap.keySet();
    }

}
