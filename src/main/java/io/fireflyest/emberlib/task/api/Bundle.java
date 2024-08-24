package io.fireflyest.emberlib.task.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

/**
 * 任务数据
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class Bundle {
    
    private static final int MAX_CAPACITY = 10;

    private final List<Object> dataList = new ArrayList<>(MAX_CAPACITY);

    /**
     * 构造任务数据
     * 
     * @param objs 多个数据
     */
    public Bundle(Object... objs) {
        Collections.addAll(dataList, objs);
    }

    /**
     * 添加一个数据
     * 
     * @param obj 数据
     */
    public void add(Object obj) {
        if (dataList.size() < MAX_CAPACITY) {
            dataList.add(obj);
        }
    }

    /**
     * 获取一个数据
     * 
     * @param dataClass 数据类型的类
     * @return 数据
     */
    public Object get(Class<?> dataClass) {
        Object ret = null;
        final Iterator<Object> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            final Object next = iterator.next();
            if (dataClass.isAssignableFrom(next.getClass())) {
                ret = next;
                iterator.remove();
                break;
            }
        }
        return ret;
    }

    /**
     * 获取一个字符串数据
     * 
     * @return 字符串数据
     */
    @Nullable
    public String getString() {
        return (String) this.get(String.class);
    }

    /**
     * 获取一个Number数据
     * 
     * @return Number数据
     */
    @Nullable
    public Number getNumber() {
        return (Number) this.get(Number.class);
    }

    /**
     * 获取一个Boolean数据
     * 
     * @return Boolean数据
     */
    @Nullable
    public Boolean getBoolean() {
        return (Boolean) this.get(Boolean.class);
    }

}
