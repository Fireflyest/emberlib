package io.fireflyest.emberlib.data;

/**
 * 对，存储一对对象
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class Pair<A, B> {
    
    private A firstValue;
    private B secondValue;

    public Pair(A firstValue, B secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    /**
     * 获取第一个数据
     * 
     * @return 第一个数据
     */
    public A first() {
        return this.firstValue;
    }

    /**
     * 获取第二个数据
     * 
     * @return 第二个数据
     */
    public B second() {
        return this.secondValue;
    }

    /**
     * 设置第一个数据
     * 
     * @param firstValue 第一个数据
     */
    public void setFirstValue(A firstValue) {
        this.firstValue = firstValue;
    }

    /**
     * 设置第二个数据
     * 
     * @param secondValue 第二个数据
     */
    public void setSecondValue(B secondValue) {
        this.secondValue = secondValue;
    }

}
