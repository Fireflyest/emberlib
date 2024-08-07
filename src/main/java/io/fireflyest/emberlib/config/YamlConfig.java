package io.fireflyest.emberlib.config;

/**
 * 配置类
 * 
 * @author Fireflyest
 * @since 
 */
public abstract class YamlConfig {

    /**
     * 数据箱
     */
    public class Box<T> {

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

}
