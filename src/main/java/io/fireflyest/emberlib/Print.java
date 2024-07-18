package io.fireflyest.emberlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 后台打印
 * @author Fireflyest
 * @since 1.0
 */
public enum Print {

    /**
     * 本插件打印
     */
    EMBER_LIB("EmberLib");

    private static final String COLOR_RESET = "\033[0m";
    private static final String COLOR_BLACK = "\033[30m";
    private static final String COLOR_RED = "\033[31m";
    private static final String COLOR_GREEN = "\033[32m";
    private static final String COLOR_YELLOW = "\033[33m";
    private static final String COLOR_BLUE = "\033[34m";
    private static final String COLOR_PURPLE = "\033[35m";
    private static final String COLOR_CYAN = "\033[36m";

    private static final String BACKGROUND_RED = "\033[41m";

    private final String title;
    private final Logger logger;

    private boolean debug;

    private Print(String title) {
        this.title = title;
        this.logger = LogManager.getLogger(title);
    }

    /**
     * 输出错误
     * 
     * @param throwable 错误
     */
    public void catching(Throwable throwable) {
        // TODO: 
        logger.catching(throwable);
    }

    /**
     * 调试打印
     * 
     * @param message 信息
     * @param params 参数
     */
    public void debug(String message, Object... params) {
        if (debug) {
            message = "[" + COLOR_BLUE + title + COLOR_RESET + "] " + message;
            logger.info(message, params);
        }
    }

    /**
     * 信息打印
     * 
     * @param message 信息
     * @param params 参数
     */
    public void info(String message, Object... params) {
        message = "[" + COLOR_GREEN + title + COLOR_RESET + "] " + message;
        logger.info(message, params);
    }

    /**
     * 警告打印
     * 
     * @param message 信息
     * @param params 参数
     */
    public void warn(String message, Object... params) {
        message = "[" + COLOR_YELLOW + title + COLOR_RESET + "] " + message;
        logger.warn(message, params);
    }

    /**
     * 错误打印
     * 
     * @param message 信息
     * @param params 参数
     */
    public void error(String message, Object... params) {
        message = "[" + COLOR_RED + title + COLOR_RESET + "] " + message;
        logger.error(message, params);
    }

    /**
     * 严重错误打印
     * 
     * @param message 信息
     * @param params 参数
     */
    public void fatal(String message, Object... params) {
        message = BACKGROUND_RED + "[" + title + "] " + COLOR_RESET + message;
        logger.fatal(message, params);
    }

    /**
     * 开启调试
     */
    public void onDebug() {
        this.debug = true;
    }

}
