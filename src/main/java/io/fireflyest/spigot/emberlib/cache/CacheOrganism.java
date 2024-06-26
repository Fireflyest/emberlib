package io.fireflyest.spigot.emberlib.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nonnull;
import org.bukkit.plugin.Plugin;

/**
 * 数据缓存组织实现类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class CacheOrganism extends AbstractOrganism<String, String> {

    /**
     * 数据组织构造函数，使用普通的HashMap
     * @param name 作为保存时的文件名称
     */
    public CacheOrganism(String name) {
        super(name);
    }

    /**
     * 数据组织构造函数
     * @param name 作为保存时的文件名称
     * @param concurrent 是否使用线程安全的Map
     */
    public CacheOrganism(String name, boolean concurrent) {
        super(name, concurrent);
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
    public void sadd(@Nonnull String key, String value) {
        final AbstractCell<String> cell = cacheMap.get(key);
        Set<String> valueSet = null;
        if (cell != null && (valueSet = cell.getAll()) != null) {
            valueSet.add(value);
        } else {
            cacheMap.put(key, new CacheCell(-1, value));
        }
    }

    /**
     * 保存缓存到文件
     * @param file 缓存文件
     */
    public void save(@Nonnull File file) {
        try (FileOutputStream fStream = new FileOutputStream(file);
                DataOutputStream dStream = new DataOutputStream(fStream)) {
            // 缓存迭代器
            final Iterator<Entry<String, AbstractCell<String>>> iterator 
                = cacheMap.entrySet().iterator(); 
            // 拼接数据   
            while (iterator.hasNext()) {
                final Entry<String, AbstractCell<String>> entry = iterator.next();
                final CacheCell cacheCell = (CacheCell) entry.getValue();
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
