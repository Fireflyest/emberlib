package io.fireflyest.emberlib.cache;

import javax.annotation.Nonnull;

import io.fireflyest.emberlib.util.StrUtils;

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
    public String deserializeKey(@Nonnull String keyStr) {
        return StrUtils.base64Decode(keyStr);
    }

    @Override
    public String deserializeValue(@Nonnull String valueStr) {
        return StrUtils.base64Decode(valueStr);
    }

    @Override
    public String serializeKey(@Nonnull String key) {
        return StrUtils.base64Encode(key);
    }

    @Override
    public String serializeValue(@Nonnull String value) {
        return StrUtils.base64Encode(value);
    }

}
