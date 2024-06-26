package io.fireflyest.spigot.emberlib.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.plugin.Plugin;
import io.fireflyest.spigot.emberlib.cache.api.Organism;

/**
 * 数据缓存组织实现类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class CacheOrganism implements Organism<String, String> {

    private final String name;
    private final Map<String, CacheCell> cacheMap;
    private final Random random = new Random();

    /**
     * 数据组织构造函数，使用普通的HashMap
     * @param name 作为保存时的文件名称
     */
    public CacheOrganism(String name) {
        this(name, false);
    }

    /**
     * 数据组织构造函数
     * @param name 作为保存时的文件名称
     * @param concurrent 是否使用线程安全的Map
     */
    public CacheOrganism(String name, boolean concurrent) {
        this.name = name;
        this.cacheMap = concurrent ? new ConcurrentHashMap<>() : new HashMap<>();
    }

    @Override
    public void del(@Nonnull String key) {
        cacheMap.remove(key);
    }

    @Override
    public void expire(@Nonnull String key, int ms) {
        final CacheCell cell = cacheMap.get(key);
        if (cell != null && cell.get() != null) {
            cell.expire(ms);
        }
    }

    @Override
    public boolean exist(@Nonnull String key) {
        return cacheMap.containsKey(key) && cacheMap.get(key) != null;
    }

    @Override
    public void persist(@Nonnull String key) {
        final CacheCell cell = cacheMap.get(key);
        if (cell != null && cell.get() != null) {
            cell.persist();
        }
    }

    @Override
    public long ttl(@Nonnull String key) {
        final CacheCell cell = cacheMap.get(key);
        long ms = 0;
        if (cell != null && cell.get() != null) {
            ms = cell.ttl();
        }
        return ms;
    }

    @Override
    public void set(@Nonnull String key, String value) {
        cacheMap.put(key, new CacheCell(-1, value));
    }

    @Override
    public void set(@Nonnull String key, Set<String> valueSet) {
        cacheMap.put(key, new CacheCell(-1, valueSet));
    }

    @Override
    public void setex(@Nonnull String key, int ms, String value) {
        cacheMap.put(key, new CacheCell(ms, value));
    }

    @Override
    public void setex(@Nonnull String key, int ms, Set<String> valueSet) {
        cacheMap.put(key, new CacheCell(ms, valueSet));
    }

    @Override
    @Nullable
    public String get(@Nonnull String key) {
        return cacheMap.containsKey(key) ? cacheMap.get(key).get() : null;
    }

    @Override
    public long age(@Nonnull String key) {
        final CacheCell cell = cacheMap.get(key);
        long age = 0;
        if (cell != null && cell.get() != null) {
            age = cell.age();
        }
        return age;
    }

    @Override
    public void sadd(@Nonnull String key, String value) {
        final CacheCell cell = cacheMap.get(key);
        Set<String> valueSet = null;
        if (cell != null && (valueSet = cell.getAll()) != null) {
            valueSet.add(value);
        } else {
            cacheMap.put(key, new CacheCell(-1, value));
        }
    }

    @Override
    @Nullable
    public Set<String> smembers(@Nonnull String key) {
        return cacheMap.containsKey(key) ? cacheMap.get(key).getAll() : null;
    }

    @Override
    public void srem(@Nonnull String key, String value) {
        final CacheCell cell = cacheMap.get(key);
        Set<String> valueSet = null;
        if (cell != null && (valueSet = cell.getAll()) != null) {
            valueSet.remove(value);
        }
    }

    @Override
    @Nullable
    public String spop(@Nonnull String key) {
        final CacheCell cell = cacheMap.get(key);
        Set<String> valueSet = null;
        String value = null;
        if (cell != null && (valueSet = cell.getAll()) != null) {
            final int size;
            if ((size = valueSet.size()) == 0) {
                return value;
            }
            final Iterator<String> iterator = valueSet.iterator();
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
    public int scard(@Nonnull String key) {
        final CacheCell cell = cacheMap.get(key);
        Set<String> valueSet = null;
        int size = 0;
        if (cell != null && (valueSet = cell.getAll()) != null) {
            size = valueSet.size();
        }
        return size;
    }

    @Override
    public Set<String> keySet() {
        return cacheMap.keySet();
    }

    /**
     * 保存缓存到文件
     * @param file 缓存文件
     */
    public void save(@Nonnull File file) {
        try (FileOutputStream fStream = new FileOutputStream(file);
                DataOutputStream dStream = new DataOutputStream(fStream)) {
            // 缓存迭代器
            final Iterator<Entry<String, CacheCell>> iterator = cacheMap.entrySet().iterator(); 
            // 拼接数据   
            while (iterator.hasNext()) {
                final Entry<String, CacheCell> entry = iterator.next();
                final CacheCell cacheCell = entry.getValue();
                final Set<String> valueSet = cacheCell.getAll();
                final Instant deadline = cacheCell.getDeadline();
                // 已失效的不保存
                if (valueSet == null) {
                    iterator.remove();
                    continue;
                }
                // 数据信息拼接
                dStream.writeUTF(entry.getKey()); // key
                dStream.writeLong(cacheCell.getBorn().toEpochMilli()); // 起始时间
                dStream.writeLong(deadline == null ? 0 : deadline.toEpochMilli()); // 失效时间
                dStream.writeInt(valueSet.size()); // 数据数量
                // 数据集拼接
                for (String value : valueSet) {
                    dStream.writeUTF(value); // 数据
                }
            }
            dStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存格式为
     * key(String) born(long) deadline(long) count(int) [obj(String)]
     */
    @Override
    public void save(@Nonnull Plugin plugin) {
        final String fileName = name + ".cache";
        final File cacheFile = new File(plugin.getDataFolder(), fileName);
        this.save(cacheFile);
        
    }

    /**
     * 从文件加载数据到缓存
     * @param file 缓存文件
     */
    public void load(@Nonnull File file) {
        try (FileInputStream fStream = new FileInputStream(file);
                DataInputStream dStream = new DataInputStream(fStream)) {
            // 读取整个文件
            while (dStream.available() > 0) {
                final String key = dStream.readUTF();
                final Instant born = Instant.ofEpochMilli(dStream.readLong());
                final long dl = dStream.readLong();
                final Instant deadline = dl == 0 ? null : Instant.ofEpochMilli(dl);
                final int count = dStream.readInt();
                final Set<String> valueSet = new HashSet<>();
                for (int i = 0; i < count; i++) {
                    valueSet.add(dStream.readUTF());
                }
                if (deadline == null || Instant.now().isBefore(deadline)) {
                    cacheMap.put(key, new CacheCell(born, deadline, valueSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(@Nonnull Plugin plugin) {
        final String fileName = name + ".cache";
        final File cacheFile = new File(plugin.getDataFolder(), fileName);
        this.load(cacheFile);
    }

}
