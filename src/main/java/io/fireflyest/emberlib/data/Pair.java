package io.fireflyest.emberlib.data;

/**
 * 对，存储一对对象
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class Pair<A, B> {
    
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * 获取第一个数据
     * 
     * @return 第一个数据
     */
    public A first() {
        return this.first;
    }

    /**
     * 获取第二个数据
     * 
     * @return 第二个数据
     */
    public B second() {
        return this.second;
    }

    public static <A, B> Pair<A, B> of(A firstValue, B secondValue) {
        return new Pair<>(firstValue, secondValue);
    }

}
