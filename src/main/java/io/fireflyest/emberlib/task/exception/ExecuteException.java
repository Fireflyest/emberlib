package io.fireflyest.emberlib.task.exception;

/**
 * @author Fireflyest
 * @see io.fireflyest.emberlib.task.Task#execute()
 * @since 1.0
 */
public class ExecuteException extends Exception {

    /**
     * 任务执行错误
     */
    public ExecuteException() {
    }

    /**
     * 任务执行错误，带详细信息
     * 
     * @param message 错误信息
     */
    public ExecuteException(String message) {
        super(message);
    }

}
