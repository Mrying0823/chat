package org.mrying.chat.exception;

/**
 * @author 邓和颖
 * @since 2023/10/14 20:16
 */
public class NoTokenCountException extends RuntimeException {

    // 无参构造
    public NoTokenCountException() {
        super();
    }

    // 将传入的消息作为异常信息传递给父类构造函数
    public NoTokenCountException(String message) {
        super(message);
    }
}
