package io.fireflyest.emberlib.data;

/**
 * 箱子，存储一个对象的地址
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class Box<T> {

    private T value;

    public Box(T defaultValue) {
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