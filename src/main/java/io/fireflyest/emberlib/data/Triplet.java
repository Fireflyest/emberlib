package io.fireflyest.emberlib.data;

/**
 * 三元组
 * 
 * @param <A> 第一个元素的类型
 * @param <B> 第二个元素的类型
 * @param <C> 第三个元素的类型
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class Triplet<A, B, C> {

    private final A first;
    private final B second;
    private final C third;

    /**
     * 构造一个三元组
     * 
     * @param first  第一个元素
     * @param second 第二个元素
     * @param third  第三个元素
     */
    public Triplet(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }

    public C third() {
        return third;
    }

    public static <A, B, C> Triplet<A, B, C> of(A first, B second, C third) {
        return new Triplet<>(first, second, third);
    }

}