package io.fireflyest.spigot.emberlib.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.plugin.Plugin;
import io.fireflyest.spigot.emberlib.cache.api.Organism;

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
        return cacheMap.containsKey(key) && cacheMap.get(key).get() != null;
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
    public void set(@Nonnull K key, V value) {
        cacheMap.put(key, new AbstractCell<V>(-1, value) {});
    }

    @Override
    public void set(@Nonnull K key, Set<V> valueSet) {
        cacheMap.put(key, new AbstractCell<V>(-1, valueSet) {});
    }

    @Override
    public void setex(@Nonnull K key, int ms, V value) {
        cacheMap.put(key, new AbstractCell<V>(ms, value) {});
    }

    @Override
    public void setex(@Nonnull K key, int ms, Set<V> valueSet) {
        cacheMap.put(key, new AbstractCell<V>(ms, valueSet) {});
    }

    @Override
    public void sadd(@Nonnull K key, V value) {
        final AbstractCell<V> cell = cacheMap.get(key);
        Set<V> valueSet = null;
        if (cell != null && (valueSet = cell.getAll()) != null) {
            valueSet.add(value);
        } else {
            cacheMap.put(key, new AbstractCell<V>(-1, value) {});
        }
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
    public void release() {
        final Iterator<Entry<K, AbstractCell<V>>> iterator = cacheMap.entrySet().iterator();
        while (iterator.hasNext()) {
            final Entry<K, AbstractCell<V>> entry = iterator.next();
            if (entry.getValue().get() == null) {
                iterator.remove();
            }
        }
    }

    @Override
    public Set<K> keySet() {
        return cacheMap.keySet();
    }

    @Override
    public void load(@Nonnull Plugin plugin, @Nonnull String entryName) {
        this.load(plugin, entryName, false);
    }

    @Override
    public void load(@Nonnull Plugin plugin) {
        this.load(plugin, "latest");
    }

    @Override
    public void save(@Nonnull Plugin plugin, @Nonnull String entryName) {
        this.save(plugin, entryName, false);
    }

    @Override
    public void save(@Nonnull Plugin plugin) {
        this.save(plugin, "latest");
    }

}
