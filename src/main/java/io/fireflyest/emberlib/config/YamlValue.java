package io.fireflyest.emberlib.config;

/**
 * 配置数据
 * 
 * @author Fireflyest
 * @since 
 */
public class YamlValue<T> {

    private T value;

    public YamlValue(T defaultValue) {
        this.value = defaultValue;
    }

    /**
     * 获取数据
     * 
     * @return 数据
     */
    public T get() {
        return value;
    }

    /**
     * 更新数据
     * 
     * @param value 数据
     */
    public void set(T value) {
        this.value = value;
    }

}
