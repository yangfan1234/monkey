package com.monkey.home.zk.exception;

/**
 * monkey-zk自定义异常类
 * @author yangfan
 * @createTime 2019-10-19 20:20
 */
public class MonkeyZkException extends RuntimeException{

    public MonkeyZkException() {
        super();
    }

    public MonkeyZkException(String message) {
        super(message);
    }

    public MonkeyZkException(Throwable e) {
        super(e);
    }

    public MonkeyZkException(String message, Throwable e) {
        super(message, e);
    }


}
